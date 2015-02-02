package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
    private static final String TAG = "GetUserReviews";
    private WebserviceResponse delegate = null;
    private int userID;
    private int afterThisIndex;
    private int limitation;
    private ServerAnswer serverAnswer;

    public GetUserReviews(int userID, int afterThisIndex, int limitation, WebserviceResponse delegate) {
        this.userID = userID;
        this.afterThisIndex = afterThisIndex;
        this.limitation = limitation;
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        ArrayList<Review> list = new ArrayList<Review>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_USER_REVIEWS, new ArrayList<>(
                Arrays.asList(String.valueOf(userID), String.valueOf(afterThisIndex), String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Review review = new Review();
                    review.id = jsonObject.getInt(Params.ID);
                    review.businessID = jsonObject.getInt(Params.BUSINESS_ID);
                    review.businessPicutre = jsonObject.getString(Params.BUSINESS_PICUTE);
                    review.text = jsonObject.getString(Params.TEXT);
                    list.add(review);
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> result) {
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
