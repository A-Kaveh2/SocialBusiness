package ir.rasen.myapplication.classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.Params;

/**
 * Created by android on 12/17/2014.
 */
public class Comment {
    public int id;
    public int businessID;
    public int postID;
    public int userID;
    public String username;
    public int userProfilePictureID;
    public String text;

    public static ArrayList<Comment> getFromJSONArray(JSONArray jsonArray)throws  Exception{
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObjectComment = jsonArray.getJSONObject(j);

            Comment comment = new Comment();
            comment.id = jsonObjectComment.getInt(Params.COMMENT_ID);
            comment.userID = jsonObjectComment.getInt(Params.USER_ID);
            comment.username = jsonObjectComment.getString(Params.USER_NAME);
            comment.userProfilePictureID = jsonObjectComment.getInt(Params.PROFILE_PICTURE_ID);
            comment.text = jsonObjectComment.getString(Params.TEXT);
            comments.add(comment);
        }

        return comments;
    }
}
