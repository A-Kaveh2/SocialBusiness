package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class RateBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RateBusiness";

    private WebserviceResponse delegate = null;
    private String businessID;
    private String userID;
    private int rate;
    private ServerAnswer serverAnswer;

    public RateBusiness(String businessID, String userID, int rate) {
        this.businessID = businessID;
        this.userID = userID;
        this.rate = rate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.RATE_BUSINESS);
        webservicePOST.addParam(Params.BUSINESS_ID, businessID);
        webservicePOST.addParam(Params.USER_ID, userID);
        webservicePOST.addParam(Params.RATE, String.valueOf(rate));

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
