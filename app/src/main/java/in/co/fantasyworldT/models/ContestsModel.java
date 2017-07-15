package in.co.fantasyworldT.models;

/**
 * Created by C5245675 on 6/2/2017.
 */

public class ContestsModel {


    String Value;
    String NoOfusersJoined;
    String TotalUsersLimit;


    String Entryfee;

    public ContestsModel() {
    }


    public String getEntryfee() {
        return Entryfee;
    }

    public void setEntryfee(String entryfee) {
        Entryfee = entryfee;
    }

    public String getNoOfusersJoined() {
        return NoOfusersJoined;
    }

    public void setNoOfusersJoined(String noOfusersJoined) {
        NoOfusersJoined = noOfusersJoined;
    }

    public String getTotalUsersLimit() {
        return TotalUsersLimit;
    }

    public void setTotalUsersLimit(String totalUsersLimit) {
        TotalUsersLimit = totalUsersLimit;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
