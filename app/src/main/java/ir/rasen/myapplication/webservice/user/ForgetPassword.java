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
public class ForgetPassword extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "ForgetPassword";

    private WebserviceResponse delegate = null;
    private String email;
    private ServerAnswer serverAnswer;
    private int userID;


    public ForgetPassword(int userID,String email,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.email = email;
        this.userID = userID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.FORGET_PASSWORD);

        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(userID));
            webservicePOST.addParam(Params.EMAIL, email);

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
       /* if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/

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
