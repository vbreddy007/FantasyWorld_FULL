package in.co.fantasyworld.models;

/**
 * Created by C5245675 on 6/11/2017.
 */

public class LeaderBoardModel {

    public String name;
    public String points;
    public String rank;


    public LeaderBoardModel() {
    }

    public LeaderBoardModel(String name, String points, String rank) {
        this.name = name;
        this.points = points;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
