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

    //here is 3 types of post: ordinary post, follow announcement post and review annoucement post
    //follow announcement post fields: businessID,businessUserName,userId,userName,type
    //review announcement post fields: businessID,businessUserName,userId,userName,type,description(review text)

    public int userId;//used when post is follow or review announcement
    public String userName;////used when post is follow or review announcement

    public String creationDate;

    public String title;
    public String picture;
    public int pictureId;
    public String description;
    public String price;
    public String code;
    public boolean isLiked;
    public enum Type {Complete,Follow,Review}
    public Type type;
    public ArrayList<Comment> lastThreeComments = new ArrayList<Comment>();
    public ArrayList<String> hashtagList = new ArrayList<String>();


    public static Type getType(int type){
        switch (type){
            case 1:
                return Type.Complete;
            case 2:
                return Type.Follow;
            case 3:
                return Type.Review;
        }

        return Type.Complete;
    }

    public static int getTypeCode(Type type){
        switch (type){
            case Complete:
                return 1;
            case Follow:
                return 2;
            case Review:
                return 3;
        }

        return 0;
    }


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
    public static Post getFromJSONObjectTimeLine(JSONObject jsonObject) throws Exception{
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID);
        post.businessUserName = jsonObject.getString(Params.BUSINESS_USER_NAME);
        post.userId = jsonObject.getInt(Params.USER_ID);
        post.userName = jsonObject.getString(Params.USER_NAME);
        post.type = getType(jsonObject.getInt(Params.TYPE)) ;
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
