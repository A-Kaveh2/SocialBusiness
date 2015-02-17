package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class UpdatePassword extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdatePassword";

    private WebserviceResponse delegate = null;
    private int userID;
    private String newPassword;
    private ServerAnswer serverAnswer;

    public UpdatePassword(int userID, String newPassword,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.userID = userID;
        this.newPassword = newPassword;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PASSWORD);

        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(userID));
            webservicePOST.addParam(Params.PASSWORD_NEW, newPassword);

            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {

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
