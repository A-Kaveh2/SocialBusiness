package ir.rasen.myapplication.classes;

/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int id;
    public int postID;
    public int userID;
    public String userName;
    public int postPictureId;
    public int userPictureId;
    public String text;
    public int intervalTime;//in millisecond.

    public CommentNotification(int id, int postID, int  postPictureId,int userID,String userName ,int userPictureId, String text,int intervalTime) {
        this.id = id;
        this.postID = postID;
        this.postPictureId = postPictureId;
        this.userPictureId = userPictureId;
        this.text = text;
        this.intervalTime = intervalTime;
        this.userID = userID;
        this.userName = userName;
    }
}
