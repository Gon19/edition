package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import pt.ist.socialsoftware.edition.ldod.dto.GameDTO;
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class GameWebSocketController {
    private static Logger logger = LoggerFactory.getLogger(GameWebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;
    private Map<String, List<GameTagDto>> submittedTags = new HashMap<>();
    private Map<String, Double> participantScores = new HashMap<>();
    private Map<String, GameDTO> games = new HashMap<>();
    private static int currentFragmentIndex = 0;

    @GetMapping("/api/services/ldod-game/load/{user}/{acronym}")
    public @ResponseBody ResponseEntity<?> loadResources(@PathVariable(value = "user") String user, @PathVariable(value = "acronym") String acronym){
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        List<String> res = virtualEdition.getIntersSet().stream().sorted().map(FragInter::getUrlId).collect(Collectors.toList());
        for(String id: res){
            submittedTags.put(id, new ArrayList<>());
        }
        participantScores.put(user, 0.0);
        String uniqueID = UUID.randomUUID().toString();
        GameDTO gameDTO = new GameDTO(uniqueID, user, acronym, true);
       // gameDTO.setFragmentId(res.get(currentFragmentIndex));
        games.put(uniqueID, gameDTO);
        return new ResponseEntity<>(new APIResponse(true, "Server loaded resources successfully."), HttpStatus.OK);

    }

    @MessageMapping("/connect")
    @SendTo("/topic/config")
    public @ResponseBody void handleConnect(@Payload Map<String,String> payload) {
        /* TODO: Queria chamar o método handleReady mas da erro se for um metodo da classe e nao um getMapping, coniderar portar
            algum codigo para o dominio*/
        logger.debug("connect received: {}", payload.values());
        String user = payload.get("userId");
        String acronym = payload.get("virtualEdition");
        payload.remove("userId");
        payload.remove("virtualEdition");
        participantScores.put(user, 1.0);
        GameDTO game = games.values().stream().filter(GameDTO::isActive).filter(gameDTO -> !gameDTO.hasEnded()).limit(1).collect(Collectors.toList()).get(0);
        game.addParticipant(user);
        //payload.put("currentUsers", String.valueOf(participantScores.size()));
        payload.put("currentUsers", String.valueOf(game.getParticipants().size()));
        payload.put("command", "ready");
        payload.put("gameId", game.getGameId());
        try {
            //Thread.sleep(1 * 60 *1000);
            while(game.getParticipants().size() <= 1){
                Thread.sleep(1000); // TODO: use the above time
            }
            game.setActive(false);
            broker.convertAndSend("/topic/config", payload.values());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/api/services/ldod-game/end/{gameId}")
    public @ResponseBody ResponseEntity<?> handleEnd(@PathVariable(value = "gameId") String gameId) {
        logger.debug("end of game: {}", gameId);
        // TODO: make transaction,i.e, save to DB
        Map<String, List<GameTagDto>> result = new HashMap<>();
        Set<String> fragmentsTagged = submittedTags.keySet();
        for(String fragment: fragmentsTagged){
            List<GameTagDto> topTags = submittedTags.get(fragment).stream().sorted(((g1, g2) -> (int) g2.getScore())).limit(1).collect(Collectors.toList());

            result.put(fragment, topTags);
        }
        //return new ResponseEntity<>(new APIResponse(true, "ending game"), HttpStatus.OK);
        games.get(gameId).setEnded(true);
        /*
        String fragmentId = games.get(gameId).getFragmentId();
        submittedTags.get(fragmentId).stream().sorted(((g1, g2) -> (int) g2.getScore())).limit(1).collect(Collectors.toList());
         */
        List<Object> response = new ArrayList<>();
        response.add(result);
        response.add(participantScores);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @MessageMapping("/tags")
    @SendTo("/topic/tags")
    public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
        //logger.debug("Tags received {}", payload.values());
        String urlId = payload.get("urlId");
        String authorId = payload.get("authorId");
        GameTagDto tag = new GameTagDto(authorId, urlId, payload.get("msg"), 1);
        List<GameTagDto> res = submittedTags.get(urlId);
        if(res.stream().noneMatch(t -> t.getContent().equals(tag.getContent()))){
            // if tag is submitted for the first we add it
            res.add(tag);
            submittedTags.put(urlId, res);
            participantScores.put(authorId, participantScores.get(authorId) + 1);
        }
        else{
            // if tag already exists increment score and update
            res.stream().filter(t -> t.getContent().equals(tag.getContent())).forEach(t->{
                t.setScore(tag.getScore()+t.getScore());
                payload.put("vote", String.valueOf(t.getScore()));
                // since the tag has been suggested, it now has a co-author / voter because of the suggestion
                t.addCoAuthor(authorId);
                t.addVoter(authorId);
                participantScores.put(authorId, participantScores.get(authorId) + 1);
            });

            submittedTags.put(urlId, res);
        }
        broker.convertAndSend("/topic/tags", payload.values());

    }

    @MessageMapping("/votes")
    @SendTo("/topic/votes")
    public @ResponseBody void handleVotes(@Payload Map<String,String> payload) {
        //logger.debug("Votes received {}", payload.values());
        String urlId = payload.get("urlId");
        String voterId = payload.get("voterId");
        List<GameTagDto> res = submittedTags.get(urlId);
        String tagMsg = payload.get("msg");
        Object vote = payload.get("vote");
        double finalVote = Double.parseDouble((String) vote);
        res.stream().filter(t -> t.getContent().equals(tagMsg)).
                forEach(tagDto ->{
                    tagDto.setScore(tagDto.getScore() + finalVote);
                    tagDto.addVoter(voterId);
                    participantScores.put(tagDto.getAuthorId(), participantScores.get(tagDto.getAuthorId()) + finalVote);
                    payload.put("vote", String.valueOf(tagDto.getScore()));
                    //logger.debug("tag: {} voters: {}", tagDto.getContent(), tagDto.getVoters().toArray());
                });
        //logger.debug("voting sending: {}", payload.values());
        broker.convertAndSend("/topic/votes", payload.values());

    }

    @MessageMapping("/review")
    @SendTo("/topic/review")
    public @ResponseBody void handleReview(@Payload Map<String,String> payload) {
        //logger.debug("Review received {}", payload.values());
        String urlId = payload.get("urlId");
        Object limit = payload.get("limit");
        int finalLimit = (Integer) limit;
        List<GameTagDto> res = submittedTags.get(urlId);
        //List<String> topTags = res.stream().sorted(((g1, g2) -> (int) g2.getScore())).limit(finalLimit).map(GameTagDto::getContent).collect(Collectors.toList());
        List<GameTagDto> topTags = res.stream().sorted(((g1, g2) -> (int) g2.getScore())).limit(finalLimit).collect(Collectors.toList());
        //payload.put("topTags", topTags);
        List<Map<String, String>> response = new ArrayList<>();
        for(GameTagDto gameTagDto: topTags){
            Map<String, String> map = new HashMap<>();
            map.put("tag", gameTagDto.getContent());
            response.add(map);
        }
        try {
            //Thread.sleep(1 * 60 *1000);
            Thread.sleep(1000); // TODO: use the above time
            //logger.debug("Review sending {} ", response);
            broker.convertAndSend("/topic/review", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/api/services/ldod-game/leaderboard")
    public @ResponseBody ResponseEntity<?> getLeaderboard() {
        //logger.debug("get leaderboard");
        List<String> users = participantScores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Double> scores = participantScores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        List<Object> response = new ArrayList<>();
        response.add(users);
        response.add(scores);
        return new ResponseEntity<>(response.toArray(), HttpStatus.OK);
    }
}
