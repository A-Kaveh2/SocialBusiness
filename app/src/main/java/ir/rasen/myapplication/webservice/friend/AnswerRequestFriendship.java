package ir.rasen.myapplication.webservice.friend;

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
public class AnswerRequestFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "AnswerRequestFriendship";

    private WebserviceResponse delegate = null;
    private String applicatorUserID;
    private String requestedUserID;
    private boolean answer;
    private ServerAnswer serverAnswer;

    public AnswerRequestFriendship(String requestedUserID, String applicatorUserID, boolean answer) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        this.answer = answer;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.ANSWER_REQUEST_FRIENDSHIP);
        webservicePOST.addParam(Params.APPLICATOR_USER_ID, applicatorUserID);
        webservicePOST.addParam(Params.REQUESTED_USER_ID, requestedUserID);
        webservicePOST.addParam(Params.ANSWER, String.valueOf(answer));

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
