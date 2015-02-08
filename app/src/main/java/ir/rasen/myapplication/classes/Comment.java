package ir.rasen.myapplication.classes;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static ArrayList<Comment> getFromJSONArray(JSONArray jsonArray) throws Exception {
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

    public static boolean isDisplayed(Context context, int commendId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        int lastCommentId = preferences.getInt(Params.COMMENT_ID, 0);

        if (lastCommentId != 0 && lastCommentId == commendId)
            return true;
        return false;
    }

    public static void insertLastCommentId(Context context, int lastCommentId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(Params.COMMENT_ID, lastCommentId);
        edit.commit();
    }
}
