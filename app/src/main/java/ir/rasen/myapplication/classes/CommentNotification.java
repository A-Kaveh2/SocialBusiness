package ir.rasen.myapplication.classes;

/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int commentID;
    public int postID;
    public int userID;
    public String postPicture;
    public String userPicture;
    public String text;
    public int intervalTime;//in millisecond.

    public CommentNotification(int commentID, int postID, int userID, String postPicture, String userPicture, String text,int intervalTime) {
        this.commentID = commentID;
        this.postID = postID;
        this.userID = userID;
        this.postPicture = postPicture;
        this.userPicture = userPicture;
        this.text = text;
        this.intervalTime = intervalTime;
    }
}
