package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import controller.Review;
import helper.Params;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
    private static final String TAG = "GetBusinessReviews";
    private WebserviceResponse delegate = null;
    private String businessID;
    private int fromIndex;
    private int untilIndex;
    private ServerAnswer serverAnswer;

    public GetBusinessReviews(String businessID, int fromIndex, int untilIndex) {
        this.businessID = businessID;
        this.fromIndex = fromIndex;
        this.untilIndex = untilIndex;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        ArrayList<Review> list = new ArrayList<Review>();
        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_BUSINESS_REVIEWS);
        webservicePOST.addParam(Params.BUSINESS_ID, businessID);
        webservicePOST.addParam(Params.FROM_INDEX, String.valueOf(fromIndex));
        webservicePOST.addParam(Params.UNTIL_INDEX, String.valueOf(untilIndex));

        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Review review = new Review();
                    review.id = jsonObject.getString(Params.ID);
                    review.businessID = jsonObject.getString(Params.BUSINESS_ID);
                    review.userID = jsonObject.getString(Params.USER_ID);
                    review.userPicutre = jsonObject.getString(Params.USER_PICUTE);
                    review.text = jsonObject.getString(Params.REVIEW);
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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
