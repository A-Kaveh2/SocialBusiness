package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessHomeInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetUserHomeInfo";
    private WebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private ServerAnswer serverAnswer;

    public GetBusinessHomeInfo(int businessID,int userID,WebserviceResponse delegate) {
        this.businessID = businessID;
        this.delegate = delegate;
        this.userID = userID;
    }

    @Override
    protected Business doInBackground(Void... voids) {
        Business business = new Business();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_HOME_INFO,new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),String.valueOf(userID))));
        try {
            serverAnswer = webserviceGET.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                business.userID = jsonObject.getInt(Params.USER_ID);
                business.name = jsonObject.getString(Params.NAME);
                business.profilePicture = jsonObject.getString(Params.PROFILE_PICTURE);
                business.coverPicture = jsonObject.getString(Params.COVER_PICTURE);
                business.category = jsonObject.getString(Params.CATEGORY);
                business.subcategory = jsonObject.getString(Params.SUBCATEGORY);
                business.description = jsonObject.getString(Params.DESCRIPTION);
                business.reviewsNumber = jsonObject.getInt(Params.REVIEWS_NUMBER);
                business.followersNumber = jsonObject.getInt(Params.FOLLOWERS_NUMBER);
                business.isFollowing = jsonObject.getBoolean(Params.IS_FOLLOWING);
                business.rate = jsonObject.getInt(Params.RATE);
                return business;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Business result) {
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
