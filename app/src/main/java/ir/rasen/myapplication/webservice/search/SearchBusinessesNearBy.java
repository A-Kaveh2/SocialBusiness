package ir.rasen.myapplication.webservice.search;

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
public class SearchBusinessesNearBy extends AsyncTask<Void, Void, ArrayList<SearchItemUserBusiness>> {
    private static final String TAG = "SearchBusinessesNearBy";

    private WebserviceResponse delegate = null;

    private String userID;
    private String searchText;
    private String nearBy;
    private ServerAnswer serverAnswer;

    public SearchBusinessesNearBy(String userID, String searchText, String nearBy) {
        this.userID = userID;
        this.searchText = searchText;
        this.nearBy = nearBy;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<SearchItemUserBusiness>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.SEARCH_BUSINESS_NEAR_BY);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.SEARCH_TEXT, searchText);
        webservicePOST.addParam(Params.NEAR_BY, nearBy);

        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemUserBusiness(jsonObject.getString(Params.BUSINESS_ID),
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
