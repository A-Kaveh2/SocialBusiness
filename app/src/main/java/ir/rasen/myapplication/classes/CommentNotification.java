package ir.rasen.myapplication.classes;

/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int commentID;
    public int postID;
    public int userID;
    public int postPictureId;
    public int userPictureId;
    public String text;
    public int intervalTime;//in millisecond.

    public CommentNotification(int commentID, int postID, int  postPictureId, int userPictureId, String text,int intervalTime) {
        this.commentID = commentID;
        this.postID = postID;
        this.postPictureId = postPictureId;
        this.userPictureId = userPictureId;
        this.text = text;
        this.intervalTime = intervalTime;
    }
}
