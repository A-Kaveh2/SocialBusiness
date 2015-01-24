package ir.rasen.myapplication.webservice.post;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessPosts extends AsyncTask<Void, Void, ArrayList<Post>> {
    private static final String TAG = "GetPosts";
    private WebserviceResponse delegate = null;
    private String businessID;
    private int fromIndex;
    private int untilIndex;
    private ServerAnswer serverAnswer;

    public GetBusinessPosts(String businessID, int fromIndex, int untilIndex) {
        this.businessID = businessID;
        this.fromIndex = fromIndex;
        this.untilIndex = untilIndex;
    }

    @Override
    protected ArrayList<Post> doInBackground(Void... voids) {
        ArrayList<Post> list = new ArrayList<Post>();
        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_POSTS);
        webservicePOST.addParam(Params.BUSINESS_ID, businessID);
        webservicePOST.addParam(Params.FROM_INDEX, String.valueOf(fromIndex));
        webservicePOST.addParam(Params.UNTIL_INDEX, String.valueOf(untilIndex));

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
