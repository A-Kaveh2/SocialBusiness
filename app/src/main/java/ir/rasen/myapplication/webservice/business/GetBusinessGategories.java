package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import helper.Params;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessGategories extends AsyncTask<Void, Void, ArrayList<String>> {
    private static final String TAG = "GetBusinessGategories";
    private WebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> list = new ArrayList<String>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_BUSINESS_CATEGORIES);
        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString(Params.CATEGORY));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
