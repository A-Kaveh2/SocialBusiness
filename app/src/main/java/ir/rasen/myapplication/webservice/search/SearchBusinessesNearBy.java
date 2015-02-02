package ir.rasen.myapplication.webservice.search;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class SearchBusinessesNearBy extends AsyncTask<Void, Void, ArrayList<SearchItemUserBusiness>> {
    private static final String TAG = "SearchBusinessesNearBy";

    private WebserviceResponse delegate = null;

    private int  userID;
    private String searchText;
    private String nearBy;
    private ServerAnswer serverAnswer;

    public SearchBusinessesNearBy(int userID, String searchText, String nearBy,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.userID = userID;
        this.searchText = searchText;
        this.nearBy = nearBy;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<SearchItemUserBusiness>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.SEARCH_BUSINESS_NEAR_BY);

        try {
            webservicePOST.addParam(Params.USER_ID,String.valueOf( userID));
            webservicePOST.addParam(Params.SEARCH_TEXT, searchText);
            webservicePOST.addParam(Params.NEAR_BY, nearBy);

            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemUserBusiness(jsonObject.getInt(Params.BUSINESS_ID),
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
