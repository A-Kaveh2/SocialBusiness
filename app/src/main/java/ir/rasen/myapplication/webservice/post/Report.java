package ir.rasen.myapplication.webservice.post;

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
public class Report extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Report";

    private WebserviceResponse delegate = null;
    private String userID;
    private String postID;
    private ServerAnswer serverAnswer;

    public Report(String userID, String postID,WebserviceResponse delegate) {
        this.userID = userID;
        this.postID = postID;
        this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.REPORT,new ArrayList<>(
                Arrays.asList(userID, postID)));


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
