package ir.rasen.myapplication.webservice.comment;

import android.os.AsyncTask;
import android.util.Log;

import controller.Comment;
import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class UpdateComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateComment";

    private WebserviceResponse delegate = null;
    private Comment comment;
    private ServerAnswer serverAnswer;

    public UpdateComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_COMMENT);
        webservicePOST.addParam(Params.ID, comment.id);
        webservicePOST.addParam(Params.TEXT, comment.text);

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
