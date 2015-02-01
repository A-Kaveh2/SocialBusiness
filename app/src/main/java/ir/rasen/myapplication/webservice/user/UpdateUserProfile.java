package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.Sex;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateUserProfile extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateProfile";

    private WebserviceResponse delegate = null;
    private User user;
    private ServerAnswer serverAnswer;

    public UpdateUserProfile(User user) {
        this.user = user;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PROFILE_USER);

        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(user.id));
            webservicePOST.addParam(Params.NAME, user.name);
            webservicePOST.addParam(Params.EMAIL, user.email);
            webservicePOST.addParam(Params.ABOUT_ME, user.aboutMe);
            webservicePOST.addParam(Params.SEX, Sex.getName(user.sex));
            webservicePOST.addParam(Params.BIRTH_DATE, user.birthDate);
            webservicePOST.addParam(Params.PROFILE_PICTURE, user.profilePicture);
            webservicePOST.addParam(Params.COVER_PICTURE, user.coverPicture);

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
