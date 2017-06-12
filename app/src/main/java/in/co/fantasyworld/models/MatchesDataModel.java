package in.co.fantasyworld.models;

/**
 * Created by C5245675 on 6/11/2017.
 */

public class MatchesDataModel {


    String match_title;
    String match_score;
    String team_one;
    String team_two;
    String team_one_icon;
    String team_two_icon;
    String matchid;
    String countdown;

    public MatchesDataModel() {
    }

    public MatchesDataModel(String match_id, String match_title, String team_one, String team_two, String team_one_icon, String team_two_icon, String countdown) {
        this.matchid = match_id;
        this.match_title = match_title;
        this.match_score = match_score;
        this.team_one = team_one;
        this.team_two = team_two;
        this.team_one_icon = team_one_icon;
        this.team_two_icon = team_two_icon;
        this.countdown = countdown;
    }
    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getMatchid() {
        return matchid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public String getTeam_one_icon() {
        return team_one_icon;
    }

    public void setTeam_one_icon(String team_one_icon) {
        this.team_one_icon = team_one_icon;
    }

    public String getTeam_two_icon() {
        return team_two_icon;
    }

    public void setTeam_two_icon(String team_two_icon) {
        this.team_two_icon = team_two_icon;
    }

    public String getMatch_score() {
        return match_score;
    }

    public String getTeam_one() {
        return team_one;
    }

    public void setTeam_one(String team_one) {
        this.team_one = team_one;
    }

    public String getTeam_two() {
        return team_two;
    }

    public void setTeam_two(String team_two) {
        this.team_two = team_two;
    }

    public void setMatch_score(String match_score) {
        this.match_score = match_score;
    }

    public String getMatch_title() {
        return match_title;
    }

    public void setMatch_title(String match_title) {
        this.match_title = match_title;
    }


}
