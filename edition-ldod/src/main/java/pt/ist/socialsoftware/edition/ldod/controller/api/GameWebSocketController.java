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
import pt.ist.socialsoftware.edition.ldod.dto.GameTagDto;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class GameWebSocketController {
    private static Logger logger = LoggerFactory.getLogger(GameWebSocketController.class);
    @Autowired
    private SimpMessagingTemplate broker;
    private Map<String, List<GameTagDto>> submittedTags = new HashMap<>();

    @GetMapping("/api/services/ldod-game/ready/{acronym}")
    public @ResponseBody ResponseEntity<?> handleReady(@PathVariable(value = "acronym") String acronym) {
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        List<String> res = virtualEdition.getIntersSet().stream().sorted().map(FragInter::getUrlId).collect(Collectors.toList());
        for(String id: res){
            submittedTags.put(id, new ArrayList<>());
        }
        return new ResponseEntity<>(new APIResponse(true, "starting game"), HttpStatus.OK);

    }

    @GetMapping("/api/services/ldod-game/end")
    public @ResponseBody ResponseEntity<?> handleEnd() {
        logger.debug("end of game");
        return new ResponseEntity<>(new APIResponse(true, "ending game"), HttpStatus.OK);

    }

    @MessageMapping("/tags")
    @SendTo("/topic/tags")
    public @ResponseBody void handleTags(@Payload Map<String, String> payload) {
        String urlId = payload.get("urlId");
        GameTagDto tag = new GameTagDto(payload.get("authorId"), urlId, payload.get("msg"), 1);
        List<GameTagDto> res = submittedTags.get(urlId);
        if(res.stream().noneMatch(t -> t.getContent().equals(tag.getContent()))){
            // if tag is submitted for the first we add it
            res.add(tag);
            submittedTags.put(urlId, res);
        }
        else{
            // if tag already exists increment score and update
            res.stream().filter(t -> t.getContent().equals(tag.getContent())).forEach(t->{
                t.setScore(tag.getScore()+t.getScore());
                payload.put("vote", String.valueOf(t.getScore()));
                // since the tag has been suggested, it now has already a voter because of the suggestion
                t.addVoter(payload.get("authorId"));
            });

            submittedTags.put(urlId, res);
        }
        broker.convertAndSend("/topic/tags", payload.values());

    }

    @MessageMapping("/votes")
    @SendTo("/topic/votes")
    public @ResponseBody void handleVotes(@Payload Map<String,String> payload) {
        String urlId = payload.get("urlId");
        String voterId = payload.get("voterId");
        List<GameTagDto> res = submittedTags.get(urlId);
        String tagMsg = payload.get("msg");
        Object vote = payload.get("vote");
        res.stream().filter(t -> t.getContent().equals(tagMsg)).
                forEach(tagDto ->{
                    tagDto.setScore(tagDto.getScore() + (int) vote);
                    tagDto.addVoter(voterId);
                    payload.put("vote", String.valueOf(tagDto.getScore()));
                    logger.debug("tag: {} voters: {}", tagDto.getContent(), tagDto.getVoters().toArray());
                });

        broker.convertAndSend("/topic/votes", payload.values());

    }

}
