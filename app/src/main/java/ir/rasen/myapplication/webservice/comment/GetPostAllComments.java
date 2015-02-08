package ir.rasen.myapplication.webservice.comment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetPostAllComments extends AsyncTask<Void, Void, ArrayList<Comment>> {
    private static final String TAG = "GetAllComments";
    private WebserviceResponse delegate = null;
    private int postID;
    private int beforThisId;
    private int limitaion;
    private ServerAnswer serverAnswer;

    public GetPostAllComments(int postID, int beforThisId, int limitaion, WebserviceResponse delegate) {
        this.delegate = delegate;
        this.postID = postID;
        this.beforThisId = beforThisId;
        this.limitaion = limitaion;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... voids) {
        ArrayList<Comment> list = new ArrayList<Comment>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_COMMENTS, new ArrayList<>(
                Arrays.asList(String.valueOf(postID), String.valueOf(beforThisId),String.valueOf(limitaion))));

        try {
              serverAnswer = webserviceGET.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Comment comment = new Comment();
                    comment.id = jsonObject.getInt(Params.COMMENT_ID);
                    comment.userID = jsonObject.getInt(Params.USER_ID);
                    comment.username = jsonObject.getString(Params.USER_NAME);
                    comment.userProfilePictureID = jsonObject.getInt(Params.USER_PROFILE_PICTURE_ID);
                    comment.text = jsonObject.getString(Params.COMMENT);

                    list.add(comment);
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Comment> result) {
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
