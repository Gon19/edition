package pt.ist.socialsoftware.edition.ldod.controller.api;

import java.util.*;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;

@RestController
@RequestMapping("/api/services/ldod-game")
public class ClassificationGameController {
	private static Logger logger = LoggerFactory.getLogger(ClassificationGameController.class);

	@Autowired
	private SimpMessagingTemplate broker;

	private Map<String, ClassificationGameDto> gamesMapDto = new LinkedHashMap<>(100);
	private Map<String, List<GameTagDto>> tagsMapDto = new LinkedHashMap<>(100);


	// ------------- REST Methods ------------- //

	@GetMapping("/{username}/active")
	@PreAuthorize("hasPermission(#username, 'user.logged')")
	public @ResponseBody ResponseEntity<List<ClassificationGameDto>> getActiveGames(@PathVariable(value = "username") String username) {
		//logger.debug("getActiveGames: {}", username);

		List<ClassificationGameDto> result = LdoD.getInstance().getActiveGames4User(username).stream().map(ClassificationGameDto::new).sorted(Comparator.comparingLong(ClassificationGameDto::getDateTime)).collect(Collectors.toList());

		for (ClassificationGameDto gameDto : result) {
			if (!gamesMapDto.containsKey(gameDto.getGameExternalId())) {
				gamesMapDto.put(gameDto.getGameExternalId(), gameDto);
				tagsMapDto.put(gameDto.getGameExternalId(), new ArrayList<>(100));
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/end/{gameId}")
	public @ResponseBody ResponseEntity<?> end(@PathVariable(value = "gameId") String gameId) {
		//logger.debug("end: {}", gameId);

		String winner = getCurrentParticipantWinner(gameId);
		String winningTag = getCurrentTagWinner(gameId);

		List<Object> response = new ArrayList<>();
		response.add(winner);
		response.add(winningTag);

		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		game.finish(winner, winningTag);

		response.add(game.getClassificationGameParticipantSet());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/leaderboard")
	public @ResponseBody ResponseEntity<?> getLeaderboard() {
		//logger.debug("getLeaderboard: {}", gameId);

		List<Object> response = new ArrayList<>();

		List<Player> players = LdoD.getInstance().getUsersSet().stream().map(LdoDUser::getPlayer).collect(Collectors.toList());

		Map<String, Double> playersScores = new LinkedHashMap<>();
		for (Player p: players) {
			if (p != null) {
				playersScores.put(p.getUser().getUsername(), p.getScore());
			}
		}

		List<String> users = playersScores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry
				.comparingByValue())).map(Map.Entry::getKey).collect(Collectors.toList());
		List<Double> scores = playersScores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry
				.comparingByValue())).map(Map.Entry::getValue).collect(Collectors.toList());

		response.add(users);
		response.add(scores);

		return new ResponseEntity<>(response.toArray(), HttpStatus.OK);
	}

	// ------------- WebSocket Methods ------------- //

	@MessageMapping("/{gameId}/connect")
	public @ResponseBody void handleConnect(@Payload Map<String, String> payload) {
		//logger.debug(" handleConnect keys: {}, value: {}", payload.keySet(), payload.values());

		String userId = payload.get("userId");
		String gameId = payload.get("gameId");

		ClassificationGameDto currentGame = gamesMapDto.get(gameId);
		currentGame.addPlayer(userId, 1.0);

		createGameParticipant(gameId, userId);
	}

	@MessageMapping("/{gameId}/tags")
	@SendTo("/topic/ldod-game/{gameId}/tags")
	public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
		//logger.debug("handleTags keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		String authorId = payload.get("authorId");
		String tag = payload.get("msg");
		Object number = payload.get("paragraph");
		int finalNumber  = Integer.parseInt((String) number);

		changeGameState(gameId, ClassificationGame.ClassificationGameState.TAGGING);
		saveTag(gameId, authorId, tag, finalNumber);

		this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/tags", payload.values());
	}

	@Atomic(mode = TxMode.READ)
	@MessageMapping("/{gameId}/votes")
	@SendTo("/topic/ldod-game/{gameId}/votes")
	public @ResponseBody void handleVotes(@Payload Map<String, String> payload) {
		//logger.debug("handleVotes keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		String voterId = payload.get("voterId");
		String tagMsg = payload.get("msg");
		Object vote = payload.get("vote");
		double finalVote = Double.parseDouble((String) vote);
		Object number = payload.get("paragraph");
		int finalNumber  = Integer.parseInt((String) number);
		double v = 0;

		changeGameState(gameId, ClassificationGame.ClassificationGameState.VOTING);
		v = saveVote(gameId, voterId, tagMsg, finalVote, finalNumber);

		payload.remove("paragraph");
		String currentTopTag = getCurrentTagWinner(gameId);
		String currentWinner = getCurrentParticipantWinner(gameId);

		payload.put("vote", String.valueOf(v));
		payload.put("top", currentTopTag);
		payload.put("winner", currentWinner);

		this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/votes", payload.values());
	}

	@Atomic(mode = TxMode.READ)
	@MessageMapping("/{gameId}/review")
	@SendTo("/topic/ldod-game/{gameId}/review")
	public @ResponseBody void handleReview(@Payload Map<String, String> payload) {
		//logger.debug("handleReview keys: {}, values: {}", payload.keySet(), payload.values());

		String gameId = payload.get("gameId");
		Object limit = payload.get("limit");
		int finalLimit = Integer.parseInt((String)limit);
		finalLimit = finalLimit == 1 ? 2 : finalLimit;

		changeGameState(gameId, ClassificationGame.ClassificationGameState.REVIEWING);
		Map<String, Double> topRounds = getTopTags(gameId, finalLimit);

		List<Map<String, String>> response = new ArrayList<>();

		for (Map.Entry<String, Double > e : topRounds.entrySet()) {
			Map<String, String> map = new LinkedHashMap<>();
			map.put("tag", e.getKey());
			map.put("vote", String.valueOf(e.getValue()));
			response.add(map);
		}

		String currentTopTag = getCurrentTagWinner(gameId);
		String currentWinner = getCurrentParticipantWinner(gameId);

		payload.put("top", currentTopTag);
		payload.put("winner", currentWinner);

		Map<String, String> map = new LinkedHashMap<>();
		map.put(currentWinner, currentTopTag);
		response.add(map);

		this.broker.convertAndSend("/topic/ldod-game/" + gameId + "/review", response);
	}

	@MessageMapping("/{gameId}/sync")
	public void syncGame(@Payload Map<String, String> payload) {
		//logger.debug("syncGame: {}", payload.values());
		String gameId = payload.get("gameId");
		changeToSync(gameId);
	}

	// ------------- DB Transactions ------------- //

	@Atomic(mode = TxMode.WRITE)
	private void createGameParticipant(String gameId, String userId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		game.addParticipant(userId);
	}

	@Atomic(mode = TxMode.WRITE)
	private void saveTag(String gameId, String userId, String tag, int number) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		ClassificationGameParticipant participant = game.getParticipant(userId);
		ClassificationGameRound round = new ClassificationGameRound();

		round.setNumber(number);
		round.setRound(1);
		round.setTag(tag);
		round.setVote(1);
		round.setClassificationGameParticipant(participant);
		round.setTime(DateTime.now());
		participant.setScore(participant.getScore() + ClassificationGame.SUBMIT);

		if (game.getTags().containsKey(tag)) {
			// if tag exists increment vote
			game.addTag(tag, game.getTags().get(tag) + 1);
		}
		else {
			// if tag does NOT exists only put vote
			game.addTag(tag, round.getVote());
		}
	}

	@Atomic(mode = TxMode.WRITE)
	private double saveVote(String gameId, String userId, String tag, double vote, int number) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		ClassificationGameParticipant participant = game.getParticipant(userId);
		ClassificationGameRound round = new ClassificationGameRound();

		//find tag and vote
		game.addTag(tag, game.getTags().get(tag) + vote);

		round.setNumber(number);

		if (getGameState(gameId).equals(ClassificationGame.ClassificationGameState.REVIEWING)) {
			round.setRound(3);
		}
		else if (getGameState(gameId).equals(ClassificationGame.ClassificationGameState.VOTING)) {
			round.setRound(2);
		}

		round.setTag(tag);
		round.setVote(game.getTags().get(tag));
		round.setClassificationGameParticipant(participant);
		round.setTime(DateTime.now());

		return round.getVote();
	}

	@Atomic(mode = TxMode.WRITE)
	private void changeToSync(String gameId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		game.setSync(true);
	}

	@Atomic(mode = TxMode.WRITE)
	private void changeGameState(String gameId, ClassificationGame.ClassificationGameState state) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);

		// If we reached reviewing state it should not switch to voting again
		if (game.getState().equals(ClassificationGame.ClassificationGameState.REVIEWING)) {
			return;
		}
		game.setState(state);
	}

	@Atomic(mode = TxMode.READ)
	private Map<String, Double> getTopTags(String gameId, int limit) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		return game.getCurrentTopTags(limit);
	}

	@Atomic(mode = TxMode.READ)
	private String getCurrentTagWinner(String gameId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		return game.getCurrentTagWinner();
	}

	@Atomic(mode = TxMode.READ)
	private String getCurrentParticipantWinner(String gameId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		return game.getCurrentParticipantWinner().getPlayer().getUser().getUsername();
	}

	@Atomic(mode = TxMode.READ)
	private ClassificationGame.ClassificationGameState getGameState(String gameId) {
		ClassificationGame game = FenixFramework.getDomainObject(gameId);
		return game.getState();
	}
}
