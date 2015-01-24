package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
    private static final String TAG = "GetUserReviews";
    private WebserviceResponse delegate = null;
    private String userID;
    private int fromIndex;
    private int untilIndex;
    private ServerAnswer serverAnswer;

    public GetUserReviews(String userID, int fromIndex, int untilIndex) {
        this.userID = userID;
        this.fromIndex = fromIndex;
        this.untilIndex = untilIndex;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        ArrayList<Review> list = new ArrayList<Review>();
        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_USER_REVIEWS);
        webservicePOST.addParam(Params.USER_ID, userID);
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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
