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
public class DeleteComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeleteComment ";

    private WebserviceResponse delegate = null;
    private int businessID;
    private int commentID;
    private ServerAnswer serverAnswer;

    public DeleteComment(int businessID, int commentID) {
        this.businessID = businessID;
        this.commentID = commentID;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.DELETE_COMMENT);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID, String.valueOf(businessID));
            webservicePOST.addParam(Params.COMMENT_ID, String.valueOf(commentID));

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
