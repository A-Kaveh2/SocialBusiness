package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

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
        WebserviceGET webserviceGET = new WebserviceGET(URLs.RATE_BUSINESS, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID), String.valueOf(userID),String.valueOf(rate))));

        try {

            serverAnswer = webserviceGET.execute();
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {


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
