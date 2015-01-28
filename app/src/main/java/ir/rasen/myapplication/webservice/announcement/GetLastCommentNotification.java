package ir.rasen.myapplication.webservice.announcement;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import ir.rasen.myapplication.alarm.Alarm_M;
import ir.rasen.myapplication.classes.CommentNotification;
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
    private String userID;
    private Context context;
    private ServerAnswer serverAnswer;

    public GetLastCommentNotification(Context context, String userID,WebserviceResponse delegate) {
        this.userID = userID;
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected CommentNotification doInBackground(Void... voids) {


        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_LAST_COMMENT_NOTIFICATION);
        webservicePOST.addParam(Params.USER_ID, userID);

        try {
            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                CommentNotification commentNotification = new CommentNotification(
                        jsonObject.getString(Params.COMMENT_ID),
                        jsonObject.getString(Params.POST_ID),
                        jsonObject.getString(Params.USER_ID),
                        jsonObject.getString(Params.POST_PICUTE),
                        jsonObject.getString(Params.USER_PICUTE),
                        jsonObject.getString(Params.TEXT),
                        jsonObject.getInt(Params.INTERVAL_TIME));
                return commentNotification;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
       return null;
    }

    @Override
    protected void onPostExecute(CommentNotification result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else {
            Alarm_M alarm_m = new Alarm_M();
            alarm_m.checkInterval(context, result.intervalTime);
            delegate.getResult(result);
        }
    }
}
