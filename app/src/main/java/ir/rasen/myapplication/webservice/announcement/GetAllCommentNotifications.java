package ir.rasen.myapplication.webservice.announcement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.CommentNotification;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetAllCommentNotifications extends AsyncTask<Void, Void, ArrayList<CommentNotification>> {
    private static final String TAG = "GetAllCommentNotifications";

    private WebserviceResponse delegate = null;
    private int userID;
    private int fromIndex;
    private int untilIndex;
    private ServerAnswer serverAnswer;


    public GetAllCommentNotifications(int userID, int fromIndex, int untilIndex) {
        this.userID = userID;
        this.fromIndex = fromIndex;
        this.untilIndex = untilIndex;
    }

    @Override
    protected ArrayList<CommentNotification> doInBackground(Void... voids) {
        ArrayList<CommentNotification> list = new ArrayList<CommentNotification>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_ALL_COMMENT_NOTIFICATIONS);


        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(userID));
            webservicePOST.addParam(Params.FROM_INDEX, String.valueOf(fromIndex));
            webservicePOST.addParam(Params.UNTIL_INDEX, String.valueOf(untilIndex));

            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new CommentNotification(jsonObject.getInt(Params.COMMENT_ID),
                            jsonObject.getInt(Params.POST_ID),
                            jsonObject.getInt(Params.USER_ID),
                            jsonObject.getString(Params.POST_PICUTE),
                            jsonObject.getString(Params.USER_PICUTE),
                            jsonObject.getString(Params.TEXT),
                            jsonObject.getInt(Params.INTERVAL_TIME)));

                }
                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<CommentNotification> result) {
       /* if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
*/
        //if webservice.execute() throws exception
        if (result == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
