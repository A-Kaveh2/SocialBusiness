package ir.rasen.myapplication.helper;

/**
 * Created by android on 12/16/2014.
 */

public class SearchItemUserBusiness {
    public int userID;
    public int pictureId;
    public String username;
    public double distance;

    public SearchItemUserBusiness(int userID, int pictureId, String username){
        this.userID = userID;
        this.pictureId = pictureId;
        this.username = username;

    }
    public SearchItemUserBusiness(int userID, int pictureId, String username,double distance){
        this.userID = userID;
        this.pictureId = pictureId;
        this.username = username;
        this.distance = distance;

    }
}
