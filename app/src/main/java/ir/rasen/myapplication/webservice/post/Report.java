package ir.rasen.myapplication.webservice.post;

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
public class Report extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Report";

    private WebserviceResponse delegate = null;
    private String userID;
    private String postID;
    private ServerAnswer serverAnswer;

    public Report(String userID, String postID) {
        this.userID = userID;
        this.postID = postID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REPORT);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.POST_ID, postID);

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
