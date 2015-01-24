package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateSetting extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateSetting";

    private WebserviceResponse delegate = null;
    private String userID;
    private Permission permission;
    private ServerAnswer serverAnswer;

    public UpdateSetting(String userID, Permission permission) {
        this.userID = userID;
        this.permission = permission;

    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_SETTING);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.PERMISSION_FOLLOWED_BUSINESSES, String.valueOf(permission.followedBusiness));
        webservicePOST.addParam(Params.PERMISSION_FRIENDS, String.valueOf(permission.friends));
        webservicePOST.addParam(Params.PERMISSION_REVIEWS, String.valueOf(permission.reviews));

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
