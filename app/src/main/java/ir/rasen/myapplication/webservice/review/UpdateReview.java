package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import controller.Review;
import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class UpdateReview extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateReview";

    private WebserviceResponse delegate = null;
    private Review review;
    private ServerAnswer serverAnswer;

    public UpdateReview(Review review) {
        this.review = review;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_REVIEW);
        webservicePOST.addParam(Params.USER_ID, review.userID);
        webservicePOST.addParam(Params.REVIEW_ID, review.id);
        webservicePOST.addParam(Params.TEXT, review.text);

        try {
            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
