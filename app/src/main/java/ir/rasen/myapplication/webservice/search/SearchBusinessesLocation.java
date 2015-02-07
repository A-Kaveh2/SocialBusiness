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

    private int userID;
    private String searchText;
    private String location_latitude;
    private String location_longitude;
    private int subcategoryId;
    private ServerAnswer serverAnswer;
    private int beforThisId;
    private int limitataion;


    public SearchBusinessesLocation(int userID, String searchText,int subcategoryId, String location_latitude,String location_longitude,int beforThisId,int limitation,WebserviceResponse delegate) {
        this.userID = userID;
        this.searchText = searchText;
        this.location_latitude = location_latitude;
        this.location_longitude = location_longitude;
        this.delegate = delegate;
        this.subcategoryId = subcategoryId;
        this.beforThisId = beforThisId;
        this.limitataion = limitation;
    }

    @Override
    protected ArrayList<SearchItemUserBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemUserBusiness> list = new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SEARCH_BUSINESS_LOCATION,new ArrayList<>(
                Arrays.asList( searchText,String.valueOf(subcategoryId),
                        location_latitude,location_longitude,
                        String.valueOf(beforThisId),String.valueOf(limitataion))));

        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemUserBusiness(jsonObject.getInt(Params.BUSINESS_ID),
                            jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID),
                            jsonObject.getString(Params.BUSINESS_USER_NAME),
                            jsonObject.getDouble(Params.DISTANCE)));
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
