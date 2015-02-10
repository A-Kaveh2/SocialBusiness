package ir.rasen.myapplication.webservice.friend;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class RequestCancelFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RequestCancelFriendship";

    private WebserviceResponse delegate = null;
    private int applicatorUserID;
    private int requestedUserID;
    private ServerAnswer serverAnswer;

    public RequestCancelFriendship(int applicatorUserID, int requestedUserID, WebserviceResponse delegate) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.REQUEST_CANCEL_FRIENDSHIP, new ArrayList<>(
                Arrays.asList(String.valueOf(applicatorUserID),
                        String.valueOf(requestedUserID))));

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
