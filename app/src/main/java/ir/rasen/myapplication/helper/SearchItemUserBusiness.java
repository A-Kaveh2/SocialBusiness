package ir.rasen.myapplication.helper;

/**
 * Created by android on 12/16/2014.
 */

public class SearchItemUserBusiness {
    public int id;
    public int pictureId;
    public String username;
    public double distance;

    public SearchItemUserBusiness(int id, int pictureId, String username){
        this.id = id;
        this.pictureId = pictureId;
        this.username = username;

    }
    public SearchItemUserBusiness(int id, int pictureId, String username,double distance){
        this.id = id;
        this.pictureId = pictureId;
        this.username = username;
        this.distance = distance;

    }
}
