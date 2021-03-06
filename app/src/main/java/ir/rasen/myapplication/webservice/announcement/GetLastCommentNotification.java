package ir.rasen.myapplication.webservice.announcement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.alarm.Alarm_M;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.CommentNotification;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.MyNotification;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetLastCommentNotification extends AsyncTask<Void, Void, CommentNotification> {
    private static final String TAG = "GetCommentAnnouncement";

    private WebserviceResponse delegate = null;
    private int userID;
    private Context context;
    private ServerAnswer serverAnswer;

    public GetLastCommentNotification(Context context, int userID, WebserviceResponse delegate) {
        this.userID = userID;
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected CommentNotification doInBackground(Void... voids) {


        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_LAST_COMMENT_NOTIFICATION, new ArrayList<>(
                Arrays.asList(String.valueOf(userID))));


        try {
            serverAnswer = webserviceGET.execute();
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();

                String userPicture = jsonObject.getString(Params.USER_PROFILE_PICTURE);
                JSONObject userJson = new JSONObject(userPicture);
                userPicture = userJson.getString(Params.IMAGE);

                String postPicture = jsonObject.getString(Params.POST_PICUTE);
                JSONObject postJson = new JSONObject(postPicture);
                postPicture = postJson.getString(Params.IMAGE);

                CommentNotification commentNotification = new CommentNotification(
                        jsonObject.getInt(Params.COMMENT_ID),
                        jsonObject.getInt(Params.POST_ID),
                        jsonObject.getString(Params.USER_NAME),
                        userPicture,
                        postPicture,
                        jsonObject.getString(Params.TEXT),
                        jsonObject.getInt(Params.INTERVAL_TIME));
                return commentNotification;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(CommentNotification result) {

        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());

    }
}
