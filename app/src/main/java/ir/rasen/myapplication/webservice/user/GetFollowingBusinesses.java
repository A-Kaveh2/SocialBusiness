package ir.rasen.myapplication.webservice.user;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetFollowingBusinesses extends AsyncTask<Void, Void, ArrayList<SearchItemUserBusiness>> {
    private static final String TAG = "GetFollowingBusinesses";

    private WebserviceResponse delegate = null;
    private int userID;
    private ServerAnswer serverAnswer;

    public GetFollowingBusinesses(int userID,WebserviceResponse delegate) {
        this.userID = userID;
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<SearchItemUserBusiness>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_FOLLOWING_BUSINESSES, new ArrayList<>(
                Arrays.asList(String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemUserBusiness(jsonObject.getInt(Params.BUSINESS_ID),
                            jsonObject.getInt(Params.SEARCH_PICTURE_ID),
                            jsonObject.getString(Params.BUSINESS_USER_NAME)));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<SearchItemUserBusiness> result) {
        /*if (result == null)
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
