package ir.rasen.myapplication.webservice.friend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import helper.Params;
import helper.SearchItemUserBusiness;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetUserFriendRequests extends AsyncTask<Void, Void, ArrayList<SearchItemUserBusiness>> {
    private static final String TAG = "GetUserFriendRequests";

    private WebserviceResponse delegate = null;
    private String userID;
    private ServerAnswer serverAnswer;

    public GetUserFriendRequests(String userID) {
        this.userID = userID;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<SearchItemUserBusiness>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_USER_FRIEND_REQUEST);
        webservicePOST.addParam(Params.USER_ID, userID);

        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemUserBusiness(jsonObject.getString(Params.USER_ID),
                            jsonObject.getString(Params.PICTURE),
                            jsonObject.getString(Params.NAME)));
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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
