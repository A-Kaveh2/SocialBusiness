package ir.rasen.myapplication.webservice.user;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.Sex;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserProfileInfo extends AsyncTask<Void, Void, User> {
    private static final String TAG = "GetUserProfileInfo";
    private WebserviceResponse delegate = null;
    private int userID;
    private ServerAnswer serverAnswer;
    private ArrayList<String> params;

    public GetUserProfileInfo(int userID, WebserviceResponse delegate) {
        this.userID = userID;
        this.delegate = delegate;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User user = new User();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_PROFILE_INFO,
                new ArrayList<>(Arrays.asList(String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                user.name = jsonObject.getString(Params.NAME);
                user.email = jsonObject.getString(Params.EMAIL);
                user.password = jsonObject.getString(Params.PASSWORD);
                user.aboutMe = jsonObject.getString(Params.ABOUT_ME);
                user.sex = Sex.getSex(jsonObject.getString(Params.SEX));
                user.birthDate = jsonObject.getString(Params.BIRTH_DATE);
                user.profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);
                user.isEmailConfirmed = jsonObject.getBoolean(Params.IS_EMAIL_COMFIRMED);

                return user;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;

        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {

        //if webserviceGET.execute() throws exception
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
