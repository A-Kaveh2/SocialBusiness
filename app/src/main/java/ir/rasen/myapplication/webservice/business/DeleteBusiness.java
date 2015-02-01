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
public class DeleteBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeleteBusiness";

    private WebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private ServerAnswer serverAnswer;

    public DeleteBusiness(int userID, int businessID) {
        this.businessID = businessID;
        this.userID = userID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.DELETE_BUSINESS, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),String.valueOf( userID))));

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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
