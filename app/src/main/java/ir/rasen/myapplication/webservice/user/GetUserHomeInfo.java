package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.FriendshipRelation;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserHomeInfo extends AsyncTask<Void, Void, User> {
    private static final String TAG = "GetUserHomeInfo";
    private WebserviceResponse delegate = null;
    private int userID;
    private ServerAnswer serverAnswer;

    public GetUserHomeInfo(int userID,WebserviceResponse delegate) {
        this.userID = userID;
        this.delegate = delegate;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User user = new User();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_HOME_INFO,new ArrayList<>(
                Arrays.asList(String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                user.name = jsonObject.getString(Params.NAME);
                user.aboutMe = jsonObject.getString(Params.ABOUT_ME);
                user.profilePicture = jsonObject.getString(Params.PROFILE_PICTURE);
                user.coverPicture = jsonObject.getString(Params.COVER_PICTURE);
                user.friendRequestNumber = jsonObject.getInt(Params.FRIEND_REQUEST_NUMBER);
                user.reviewsNumber = jsonObject.getInt(Params.REVIEWS_NUMBER);
                user.followedBusinessesNumber = jsonObject.getInt(Params.FOLLOWED_BUSINESSES_NUMBER);
                user.friendsNumber = jsonObject.getInt(Params.FRIENDS_NUMBER);

                JSONObject jsonObjectPermission = jsonObject.getJSONObject(Params.PERMISSION);
                Permission permission = new Permission();
                permission.getFromJsonObject(jsonObjectPermission);
                user.permissions = permission;
                user.friendshipRelationStatus = FriendshipRelation.getFromCode(jsonObject.getInt(Params.FRIENDSHIP_RELATION_STATUS));

                return user;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
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
