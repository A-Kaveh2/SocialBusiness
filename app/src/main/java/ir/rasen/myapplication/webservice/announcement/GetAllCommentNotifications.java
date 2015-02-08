package ir.rasen.myapplication.webservice.announcement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
public class GetAllCommentNotifications extends AsyncTask<Void, Void, ArrayList<CommentNotification>> {
    private static final String TAG = "GetAllCommentNotifications";

    private WebserviceResponse delegate = null;
    private int businessId;
    private int beforeThisId;
    private int limitation;
    private ServerAnswer serverAnswer;


    public GetAllCommentNotifications(int businessId, int beforeThisId, int limitation, WebserviceResponse delegate) {
        this.delegate = delegate;
        this.businessId = businessId;
        this.beforeThisId = beforeThisId;
        this.limitation = limitation;
    }

    @Override
    protected ArrayList<CommentNotification> doInBackground(Void... voids) {
        ArrayList<CommentNotification> list = new ArrayList<CommentNotification>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_ALL_COMMENT_NOTIFICATIONS, new ArrayList<>(
                Arrays.asList(String.valueOf(businessId), String.valueOf(beforeThisId), String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String userPicture = jsonObject.getString(Params.USER_PROFILE_PICTURE);
                    JSONObject userJson = new JSONObject(userPicture);
                    userPicture = userJson.getString(Params.IMAGE);

                    String postPicture = jsonObject.getString(Params.POST_PICUTE);
                    JSONObject postJson = new JSONObject(postPicture);
                    postPicture = postJson.getString(Params.IMAGE);

                    list.add(new CommentNotification(
                            jsonObject.getInt(Params.COMMENT_ID),
                            jsonObject.getInt(Params.POST_ID),
                            jsonObject.getString(Params.USER_NAME),
                            userPicture,
                            postPicture,
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
