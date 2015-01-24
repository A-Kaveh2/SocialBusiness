package ir.rasen.myapplication.webservice.post;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Hashtag;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class AddPost extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "AddPost";

    private WebserviceResponse delegate = null;
    private Post post;
    private ServerAnswer serverAnswer;

    public AddPost(Post post) {
        this.post = post;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.ADD_POST);
        webservicePOST.addParam(Params.BUSINESS_ID, post.businessID);
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
