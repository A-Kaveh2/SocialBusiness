package ir.rasen.myapplication.webservice.search;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import controller.Post;
import helper.Params;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class SearchPost extends AsyncTask<Void, Void, ArrayList<Post>> {
    private static final String TAG = "SearchPost";

    private WebserviceResponse delegate = null;

    private String userID;
    private String searchText;
    private ServerAnswer serverAnswer;

    public SearchPost(String userID, String searchText) {
        this.userID = userID;
        this.searchText = searchText;

    }

    @Override
    protected ArrayList<Post> doInBackground(Void... voids) {
        ArrayList<Post> list = new ArrayList<Post>();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.SEARCH_POST);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.SEARCH_TEXT, searchText);

        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(Post.getFromJSONObject(jsonObject));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
