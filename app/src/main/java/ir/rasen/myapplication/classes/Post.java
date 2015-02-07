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
    public int businessProfilePictureId;
    public String creationDate;
    //if post doesn't have picture, it must have title, actually it is not a regular post.
    //in this case, post is follow or review notice.
    public String title;
    public String picture;
    public int pictureId;
    public String description;
    public String price;
    public String code;
    public boolean isLiked;
    public ArrayList<Comment> lastThreeComments = new ArrayList<Comment>();
    public ArrayList<String> hashtagList = new ArrayList<String>();

    public static Post getFromJSONObjectShare(JSONObject jsonObject) throws Exception{
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID);
        post.businessUserName = jsonObject.getString(Params.BUSINESS_USER_NAME);
        post.businessProfilePictureId = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID);
        post.title = jsonObject.getString(Params.TITLE);
        post.creationDate = jsonObject.getString(Params.CREATION_DATAE);
        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID);
        post.description = jsonObject.getString(Params.DESCRIPTION);
        post.code = jsonObject.getString(Params.CODE);
        post.price = jsonObject.getString(Params.PRICE);

        String comments = jsonObject.getString(Params.COMMENTS);
        JSONArray jsonArrayComments = new JSONArray(comments);
        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        return post;
    }
    public static Post getFromJSONObjectBusiness(JSONObject jsonObject) throws Exception{
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID);
        post.title = jsonObject.getString(Params.TITLE);
        post.creationDate = jsonObject.getString(Params.CREATION_DATAE);
        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID);
        post.description = jsonObject.getString(Params.DESCRIPTION);
        post.code = jsonObject.getString(Params.CODE);
        post.price = jsonObject.getString(Params.PRICE);

        String comments = jsonObject.getString(Params.COMMENTS);
        JSONArray jsonArrayComments = new JSONArray(comments);
        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        return post;
    }
    public static Post getFromJSONObjectWall(JSONObject jsonObject) throws Exception{
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID);
        post.businessUserName = jsonObject.getString(Params.BUSINESS_USER_NAME);
        post.businessProfilePictureId  = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID);
        post.title = jsonObject.getString(Params.TITLE);
        post.creationDate = jsonObject.getString(Params.CREATION_DATAE);
        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID);
        post.description = jsonObject.getString(Params.DESCRIPTION);
        post.code = jsonObject.getString(Params.CODE);
        post.price = jsonObject.getString(Params.PRICE);
        post.isLiked = jsonObject.getBoolean(Params.IS_LIKED);
        String comments = jsonObject.getString(Params.COMMENTS);
        JSONArray jsonArrayComments = new JSONArray(comments);
        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        return post;
    }

}
