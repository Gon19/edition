package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class TagDto {
    private String authorId;
    private String fragmentUrlId;
    private String content;
    private int score = 0;
    private List<String> voters = new ArrayList<>();

    public TagDto() {
    }

    public TagDto(String authorId, String fragmentUrlId, String content, int score) {
        this.authorId = authorId;
        this.fragmentUrlId = fragmentUrlId;
        this.content = content;
        this.score = score;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getFragmentUrlId() {
        return fragmentUrlId;
    }

    public void setFragmentUrlId(String fragmentUrlId) {
        this.fragmentUrlId = fragmentUrlId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getVoters() {
        return voters;
    }

    public void setVoters(List<String> voters) {
        this.voters = voters;
    }

    public void addVoter(String voter){
        this.voters.add(voter);
    }

    public void removeVoter(String voter){
        this.voters.remove(this.voters.indexOf(voter));
    }
}
