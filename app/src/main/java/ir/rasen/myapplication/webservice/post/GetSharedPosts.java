package ir.rasen.myapplication.webservice.post;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Hashtag;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetSharedPosts extends AsyncTask<Void, Void, ArrayList<Post>> {
    private static final String TAG = "GetSharedPosts";
    private WebserviceResponse delegate = null;
    private int userID;
    private int afterThisID, limitation;
    private ServerAnswer serverAnswer;

    public GetSharedPosts(int userID, int afterThisID, int limitation, WebserviceResponse delegate) {
        this.userID = userID;
        this.afterThisID = afterThisID;
        this.limitation = limitation;
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Post> doInBackground(Void... voids) {
        ArrayList<Post> list = new ArrayList<Post>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_SHARED_POSTS, new ArrayList<>(
                Arrays.asList(String.valueOf(userID),
                        String.valueOf(afterThisID),
                        String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList();
            /*Post post = new Post();
            post.id = 27;
            post.pictureId = 2022;

            post.businessID = 2013;
            post.businessUserName = "dhhhhhh";
            post.businessProfilePictureId = 2022;
            post.title = "title";
            post.creationDate = -2025;
            post.description = "dess";
            post.isLiked = true;
            post.code = "code";
            post.price = "price";

            ArrayList<Comment> comments = new ArrayList<>();
            post.lastThreeComments = comments;
            ArrayList<String> hash = new ArrayList<>();
            post.hashtagList = hash;

            post.isLiked = false;
            post.likeNumber = 0;
            post.commentNumber = 0;
            post.shareNumber = 0;

            list.add(post);
            return list;*/


            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(Post.getFromJSONObjectShare(jsonObject));
                }
                return list;
            }

            //for the test todo.xlsx record number 185
            //return null;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> result) {
        //for the test todo.xlsx record number 185
        //delegate.getResult(result);

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
