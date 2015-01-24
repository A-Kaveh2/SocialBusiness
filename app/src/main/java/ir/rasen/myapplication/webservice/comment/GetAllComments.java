package ir.rasen.myapplication.webservice.comment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetAllComments extends AsyncTask<Void, Void, ArrayList<Comment>> {
    private static final String TAG = "GetAllComments";
    private WebserviceResponse delegate = null;
    private String postID;
    private int fromIndex;
    private int untilIndex;
    private ServerAnswer serverAnswer;

    public GetAllComments(String postID, int fromIndex, int untilIndex) {
        this.postID = postID;
        this.fromIndex = fromIndex;
        this.untilIndex = untilIndex;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... voids) {
        ArrayList<Comment> list = new ArrayList<Comment>();
        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_COMMENTS);
        webservicePOST.addParam(Params.POST_ID, postID);
        webservicePOST.addParam(Params.FROM_INDEX, String.valueOf(fromIndex));
        webservicePOST.addParam(Params.UNTIL_INDEX, String.valueOf(untilIndex));

        try {
            serverAnswer = webservicePOST.executeList();
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Comment comment = new Comment();
                    comment.id = jsonObject.getString(Params.ID);
                    comment.businessID = jsonObject.getString(Params.BUSINESS_ID);
                    comment.userID = jsonObject.getString(Params.USER_ID);
                    comment.postID = jsonObject.getString(Params.POST_ID);
                    comment.text = jsonObject.getString(Params.TEXT);

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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
