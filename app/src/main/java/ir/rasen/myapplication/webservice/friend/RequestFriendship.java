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
public class RequestFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RequestFriendship";

    private WebserviceResponse delegate = null;
    private String applicatorUserID;
    private String requestedUserID;
    private ServerAnswer serverAnswer;

    public RequestFriendship(String applicatorUserID, String requestedUserID) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REQUEST_FRIENDSHIP);
        webservicePOST.addParam(Params.APPLICATOR_USER_ID, applicatorUserID);
        webservicePOST.addParam(Params.REQUESTED_USER_ID, requestedUserID);

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
