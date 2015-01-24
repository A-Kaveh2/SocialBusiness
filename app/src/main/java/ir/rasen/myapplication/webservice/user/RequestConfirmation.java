package ir.rasen.myapplication.webservice.user;

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
public class RequestConfirmation extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RequestConfirmation";

    private WebserviceResponse delegate = null;
    private String userID;
    private ServerAnswer serverAnswer;

    public RequestConfirmation(String userID) {
        this.userID = userID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REQUEST_CONFIRMATION);
        webservicePOST.addParam(Params.USER_ID, userID);

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
