package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateSetting extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateSetting";

    private WebserviceResponse delegate = null;
    private int userID;
    private Permission permission;
    private ServerAnswer serverAnswer;

    public UpdateSetting(int userID, Permission permission,WebserviceResponse delegate) {
        this.userID = userID;
        this.permission = permission;
        this.delegate = delegate;

    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UPDATE_SETTING,new ArrayList<>(
                Arrays.asList(String.valueOf(userID),
                        String.valueOf(permission.followedBusiness),
                        String.valueOf(permission.friends),
                        String.valueOf(permission.reviews))));

        try {
            serverAnswer = webserviceGET.execute();
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        /*if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/

        //if webservice.execute() throws exception
        if (result == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
