package ir.rasen.myapplication.webservice.post;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetPost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "GetPosts";
    private WebserviceResponse delegate = null;
    private int postId;
    private int userId;

    private ServerAnswer serverAnswer;

    public GetPost(int userId,int postId, WebserviceResponse delegate) {
        this.postId = postId;
        this.delegate = delegate;
        this.userId = userId;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        Post post = new Post();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_POST, new ArrayList<>(
                Arrays.asList(String.valueOf(userId),String.valueOf(postId))));


        try {
            serverAnswer = webserviceGET.execute();
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                post = Post.getFromJSONObjectShare(jsonObject);
                return post;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Post result) {

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
