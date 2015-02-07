package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateReview extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateReview";

    private WebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;
    private int userId;
    private int reviewId;
    private String review;
    private int rate;

    public UpdateReview(int userId,int reviewId,String review,int rate,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.userId = userId;
        this.reviewId = reviewId;
        this.review = review;
        this.rate = rate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UPDATE_REVIEW, new ArrayList<>(
                Arrays.asList(String.valueOf(userId), String.valueOf(reviewId),String.valueOf(review),String.valueOf(rate))));

        try {

            serverAnswer = webserviceGET.execute();
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
