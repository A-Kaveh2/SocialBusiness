package ir.rasen.myapplication.webservice.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class Login extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Login";

    public WebserviceResponse delegate = null;
    private Context context;
    private String email;
    private String password;
    private ServerAnswer serverAnswer;

    public Login(Context context, String email, String password, WebserviceResponse delegate) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.delegate = delegate;


    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {

        //params: email,password
        WebserviceGET webserviceGET = new WebserviceGET(URLs.LOGIN ,new ArrayList<>(
                Arrays.asList(email, password)));


        try {
            serverAnswer = webserviceGET.execute();
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();

                //save user_id and access token
                int user_id = 0;
                String access_token = null;
                if (jsonObject != null) {
                    user_id = jsonObject.getInt(Params.USER_ID);
                    access_token = jsonObject.getString(Params.ACCESS_TOKEN);
                    LoginInfo.login(context, user_id, access_token);
                }


                return new ResultStatus(true, ServerAnswer.NONE_ERROR);
            } else
                return new ResultStatus(false, serverAnswer.getErrorCode());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (result.success)
            delegate.getResult(result);
        else
            delegate.getError(result.errorCode);
    }
}
