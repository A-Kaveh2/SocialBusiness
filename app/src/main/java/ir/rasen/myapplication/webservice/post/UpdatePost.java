package ir.rasen.myapplication.webservice.post;

import android.os.AsyncTask;
import android.util.Log;

import controller.Post;
import helper.Hashtag;
import helper.Params;
import helper.ResultStatus;
import helper.ServerAnswer;
import helper.URLs;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class UpdatePost extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdatePost";

    private WebserviceResponse delegate = null;
    private Post post;
    private ServerAnswer serverAnswer;

    public UpdatePost(Post post) {
        this.post = post;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_POST);
        webservicePOST.addParam(Params.POST_ID, post.id);
        webservicePOST.addParam(Params.TITLE, post.title);
        webservicePOST.addParam(Params.PICTURE, post.picture);
        webservicePOST.addParam(Params.DESCRIPTION, post.description);
        webservicePOST.addParam(Params.PRICE, post.price);
        webservicePOST.addParam(Params.CODE, post.code);
        webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(post.hashtagList));

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
