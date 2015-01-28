package ir.rasen.myapplication.webservice.friend;

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
public class AnswerRequestFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "AnswerRequestFriendship";

    private WebserviceResponse delegate = null;
    private String applicatorUserID;
    private String requestedUserID;
    private boolean answer;
    private ServerAnswer serverAnswer;

    public AnswerRequestFriendship(String requestedUserID, String applicatorUserID, boolean answer,WebserviceResponse delegate) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        this.answer = answer;
        this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.ANSWER_REQUEST_FRIENDSHIP, new ArrayList<>(
                Arrays.asList(applicatorUserID,requestedUserID,String.valueOf(answer))));


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
