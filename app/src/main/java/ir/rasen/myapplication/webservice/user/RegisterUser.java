package ir.rasen.myapplication.webservice.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class RegisterUser extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RegisterUser";

    private WebserviceResponse delegate = null;
    private User user;
    private ServerAnswer serverAnswer;
    private Context context;

    public RegisterUser(Context context, User user, WebserviceResponse delegate) {
        this.user = user;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REGISTER_USER);

        try {
            webservicePOST.addParam(Params.USER_ID, user.userName);
            webservicePOST.addParam(Params.NAME, user.name);
            webservicePOST.addParam(Params.EMAIL, user.email);
            webservicePOST.addParam(Params.PASSWORD, user.password);
            if (user.profilePicture != null)
                webservicePOST.addParam(Params.PROFILE_PICTURE, user.profilePicture);
            else
                webservicePOST.addParam(Params.PROFILE_PICTURE, "");

            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();

                //save user_id and access token
                String user_id = null;
                String access_token = null;
                if (jsonObject != null) {
                    user_id = jsonObject.getString(Params.USER_ID);
                    access_token = jsonObject.getString(Params.ACCESS_TOKEN);
                    LoginInfo.login(context, user_id, access_token);
                }
            }
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
