package ir.rasen.myapplication.webservice.user;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.Sex;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserProfileInfo extends AsyncTask<Void, Void, User> {
    private static final String TAG = "GetUserProfileInfo";
    private WebserviceResponse delegate = null;
    private String userID;
    private ServerAnswer serverAnswer;

    public GetUserProfileInfo(String userID) {
        this.userID = userID;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User user = new User();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_PROFILE_INFO);
        webservicePOST.addParam(Params.USER_ID, userID);
        try {
            serverAnswer = webservicePOST.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                user.name = jsonObject.getString(Params.NAME);
                user.email = jsonObject.getString(Params.EMAIL);
                user.password = jsonObject.getString(Params.PASSWORD);
                user.aboutMe = jsonObject.getString(Params.ABOUT_ME);
                user.sex = Sex.getSex(jsonObject.getString(Params.SEX));
                user.birthDate = jsonObject.getString(Params.BIRTH_DATE);
                user.profilePicture = jsonObject.getString(Params.PROFILE_PICTURE);
                user.isEmailConfirmed = jsonObject.getBoolean(Params.IS_EMAIL_COMFIRMED);

                return user;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
