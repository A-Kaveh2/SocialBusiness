package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Category;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessGategories extends AsyncTask<Void, Void, ArrayList<Category>> {
    private static final String TAG = "GetBusinessGategories";
    private WebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;

    public GetBusinessGategories(WebserviceResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected ArrayList<Category> doInBackground(Void... voids) {
        ArrayList<Category> list = new ArrayList<Category>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_CATEGORIES,null);
        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //TODO remove test part
                    //list.add(new Category(jsonObject.getString(Params.ID),jsonObject.getString(Params.CATEGORY)));

                    //for the test. getBusinessCategory doesn't return category.id by the now!
                    list.add(new Category(1,jsonObject.getString(Params.CATEGORY)));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Category> result) {
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (result.size() == 0)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
