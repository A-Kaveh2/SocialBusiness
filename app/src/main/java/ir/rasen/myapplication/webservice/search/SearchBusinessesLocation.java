package ir.rasen.myapplication.webservice.search;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.helper.Location_M;
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
public class SearchBusinessesLocation extends AsyncTask<Void, Void, ArrayList<SearchItemUserBusiness>> {
    private static final String TAG = "SearchBusinessesLocation";

    private WebserviceResponse delegate = null;

    private String userID;
    private String searchText;
    private String location_latitude;
    private String location_longitude;
    private ServerAnswer serverAnswer;

    public SearchBusinessesLocation(String userID, String searchText, String location_latitude,String location_longitude,WebserviceResponse delegate) {
        this.userID = userID;
        this.searchText = searchText;
        this.location_latitude = location_latitude;
        this.location_longitude = location_longitude;
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<SearchItemUserBusiness>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SEARCH_BUSINESS_LOCATION,new ArrayList<>(
                Arrays.asList(userID, searchText,location_latitude,location_longitude)));


        try {
            serverAnswer = webserviceGET.executeList();
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
