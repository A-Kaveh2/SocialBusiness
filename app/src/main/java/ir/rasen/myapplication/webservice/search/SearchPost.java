package ir.rasen.myapplication.webservice.search;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemPost;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class SearchPost extends AsyncTask<Void, Void, ArrayList<SearchItemPost>> {
    private static final String TAG = "SearchPost";

    private WebserviceResponse delegate = null;

    private String searchText;
    private ServerAnswer serverAnswer;
    private int beforeThisId;
    private int limitation;

    public SearchPost(String searchText,int beforeThisId,int limitation,WebserviceResponse delegate) {
        this.searchText = searchText;
        this.delegate = delegate;
        this.beforeThisId = beforeThisId;
        this.limitation = limitation;

    }

    @Override
    protected ArrayList<SearchItemPost> doInBackground(Void... voids) {
        ArrayList<SearchItemPost> list = new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SEARCH_POST,new ArrayList<>(
                Arrays.asList(searchText,String.valueOf(beforeThisId),String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemPost(jsonObject.getInt(Params.POST_ID),
                            jsonObject.getInt(Params.POST_PICTURE_ID)));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<SearchItemPost> result) {

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
