package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


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

        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(review.userID));
            webservicePOST.addParam(Params.REVIEW_ID,String.valueOf( review.id));
            webservicePOST.addParam(Params.TEXT, review.text);

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
       /* if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/
        //if webservice.execute() throws exception
        if (result == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
