package ir.rasen.myapplication.webservice.post;

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
public class Share extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Share";

    private WebserviceResponse delegate = null;
    private int userID;
    private int postID;
    private ServerAnswer serverAnswer;

    public Share(int userID, int postID) {
        this.userID = userID;
        this.postID = postID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.SHARE);

        try {
            webservicePOST.addParam(Params.USER_ID,String.valueOf( userID));
            webservicePOST.addParam(Params.POST_ID,String.valueOf( postID));

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
        /*if (result == null)
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
