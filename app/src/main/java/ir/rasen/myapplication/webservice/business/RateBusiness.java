package ir.rasen.myapplication.webservice.business;

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
public class RateBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RateBusiness";

    private WebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private int rate;
    private ServerAnswer serverAnswer;

    public RateBusiness(int businessID, int userID, int rate,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.businessID = businessID;
        this.userID = userID;
        this.rate = rate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.RATE_BUSINESS);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID, String.valueOf(businessID));
            webservicePOST.addParam(Params.USER_ID,String.valueOf( userID));
            webservicePOST.addParam(Params.RATE, String.valueOf(rate));

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
