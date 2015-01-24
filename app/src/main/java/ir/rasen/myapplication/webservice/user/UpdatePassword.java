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
public class UpdatePassword extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdatePassword";

    private WebserviceResponse delegate = null;
    private String userID, newPassword;
    private ServerAnswer serverAnswer;

    public UpdatePassword(String userID, String newPassword) {
        this.userID = userID;
        this.newPassword = newPassword;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PASSWORD);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.PASSWORD_NEW, newPassword);

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
