package ir.rasen.myapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.Hashtag;
import ir.rasen.myapplication.helper.Params;

/**
 * Created by android on 12/17/2014.
 */
public class Post {

    public int id;
    public int businessID;
    public String businessUserName;
    public String creationDate;
    //if post doesn't have picture, it must have title, actually it is not a regular post.
    //in this case, post is follow or review notice.
    public String title;
    public String picture;
    public String description;
    public String price;
    public String code;
    public ArrayList<Comment> lastThreeComments = new ArrayList<Comment>();
    public ArrayList<String> hashtagList = new ArrayList<String>();

    public static Post getFromJSONObject(JSONObject jsonObject) throws Exception{
        Post post = new Post();
        post.id = jsonObject.getInt(Params.ID);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID);
        post.businessUserName = jsonObject.getString(Params.BUSINESS_USER_NAME);
        post.title = jsonObject.getString(Params.TITLE);
        post.creationDate = jsonObject.getString(Params.CREATION_DATAE);
        post.picture = jsonObject.getString(Params.PICTURE);
        post.description = jsonObject.getString(Params.DESCRIPTION);
        post.code = jsonObject.getString(Params.CODE);
        post.price = jsonObject.getString(Params.PRICE);

        ArrayList<Comment> comments = new ArrayList<Comment>();
        JSONArray jsonArrayComments = jsonObject.getJSONArray(Params.LAST_THREE_COMMENTS);
        for (int j = 0; j < jsonArrayComments.length(); j++) {
            JSONObject jsonObjectComment = jsonArrayComments.getJSONObject(j);

            Comment comment = new Comment();
            comment.id = jsonObjectComment.getInt(Params.ID);
            comment.businessID = jsonObjectComment.getInt(Params.BUSINESS_ID);
            comment.userID = jsonObjectComment.getInt(Params.USER_ID);
            comment.postID = jsonObjectComment.getInt(Params.POST_ID);
            comment.text = jsonObjectComment.getString(Params.TEXT);
            comments.add(comment);
        }
        post.lastThreeComments = comments;
        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        return post;
    }

}
