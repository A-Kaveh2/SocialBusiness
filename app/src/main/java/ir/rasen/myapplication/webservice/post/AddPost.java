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
public class AddPost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "AddPost";

    private WebserviceResponse delegate = null;
    private Post post;
    private ServerAnswer serverAnswer;

    public AddPost(Post post,WebserviceResponse delegate) {
        this.post = post;
        this.delegate = delegate;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.ADD_POST);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID, String.valueOf(post.businessID));
            webservicePOST.addParam(Params.TITLE, post.title);
            webservicePOST.addParam(Params.PICTURE, post.picture);
            webservicePOST.addParam(Params.DESCRIPTION, post.description);
            webservicePOST.addParam(Params.PRICE, post.price);
            webservicePOST.addParam(Params.CODE, post.code);
            webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(post.hashtagList));

            serverAnswer = webservicePOST.execute();
            if (serverAnswer.getSuccessStatus()) {
                post.id = Integer.valueOf(serverAnswer.getResult().getString(Params.POST_ID));
                return post;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Post result) {


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
