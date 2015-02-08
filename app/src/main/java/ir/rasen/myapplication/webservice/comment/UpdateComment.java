package ir.rasen.myapplication.webservice.comment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Comment;
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
public class UpdateComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateComment";

    private WebserviceResponse delegate = null;
    private Comment comment;
    private ServerAnswer serverAnswer;

    public UpdateComment(Comment comment,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.comment = comment;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UPDATE_COMMENT,new ArrayList<>(
                Arrays.asList(String.valueOf(comment.userID), String.valueOf(comment.id),comment.text)));

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
