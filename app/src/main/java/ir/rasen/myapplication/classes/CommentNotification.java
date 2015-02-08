package ir.rasen.myapplication.classes;

/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int id;
    public int postID;
    public int userID;
    public String userName;

    public String postPicture;
    public String userPicture;
    public String text;
    public int intervalTime;//in millisecond.

    public CommentNotification(int id, int postID,String userName ,String userPicture,String postPicture, String text,int intervalTime) {
        this.id = id;
        this.postID = postID;
        this.userPicture = userPicture;
        this.postPicture = postPicture;
        this.text = text;
        this.intervalTime = intervalTime;
        this.userName = userName;
    }
}
