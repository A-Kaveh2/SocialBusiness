package ir.rasen.myapplication.webservice.comment;

import android.os.AsyncTask;
import android.util.Log;

import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class SendComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "SendComment";

    private WebserviceResponse delegate = null;
    private String userID;
    private String postID;
    private String comment;
    private ServerAnswer serverAnswer;

    public SendComment(String userID, String postID, String comment) {
        this.userID = userID;
        this.postID = postID;
        this.comment = comment;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.SEND_COMMENT);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.POST_ID, postID);
        webservicePOST.addParam(Params.COMMENT, comment);

        try {
            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
