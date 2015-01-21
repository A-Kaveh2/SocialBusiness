package ir.rasen.myapplication.helper;

/**
 * Created by android on 12/24/2014.
 */
public class SearchItemAnnouncementComment {
    public String userID;
    public String postID;
    public String user_picture;
    public String post_picture;


    public SearchItemAnnouncementComment(String userID, String postID, String user_picture, String post_picture){
        this.userID = userID;
        this.postID = postID;
        this.user_picture = user_picture;
        this.post_picture = post_picture;
    }
}
