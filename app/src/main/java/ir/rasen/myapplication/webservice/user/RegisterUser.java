package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import controller.User;
import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class RegisterUser extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RegisterUser";

    private WebserviceResponse delegate = null;
    private User user;
    private ServerAnswer serverAnswer;

    public RegisterUser(User user,WebserviceResponse delegate) {
        this.user = user;
        this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REGISTER_USER);
        webservicePOST.addParam(Params.USER_ID, user.userID);
        webservicePOST.addParam(Params.NAME, user.name);
        webservicePOST.addParam(Params.EMAIL, user.email);
        webservicePOST.addParam(Params.PASSWORD, user.password);
        webservicePOST.addParam(Params.PROFILE_PICTURE, user.profilePicture);

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
        if (result == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (result.success)
            delegate.getResult(result);
        else
            delegate.getError(result.errorCode);
    }
}
