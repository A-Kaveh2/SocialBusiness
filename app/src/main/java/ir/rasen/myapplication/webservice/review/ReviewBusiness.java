package ir.rasen.myapplication.webservice.review;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class ReviewBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "ReviewBusiness";

    private WebserviceResponse delegate = null;
    private String userID;
    private String businessID;
    private String review;
    private ServerAnswer serverAnswer;

    public ReviewBusiness(String userID, String businessID, String review) {
        this.userID = userID;
        this.businessID = businessID;
        this.review = review;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_REVIEW);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.BUSINESS_ID, businessID);
        webservicePOST.addParam(Params.TEXT, review);

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
