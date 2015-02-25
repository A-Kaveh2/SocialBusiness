package ir.rasen.myapplication.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.Params;

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

    public  RemoteViews getCommentNotificationContentView(Context context){
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewBitmap(R.id.img_notification_user, Image_M.getBitmapFromString(userPicture));
        contentView.setImageViewBitmap(R.id.img_notification_post,Image_M.getBitmapFromString(postPicture));
        contentView.setTextViewText(R.id.txt_notification,text);

        return contentView;
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
