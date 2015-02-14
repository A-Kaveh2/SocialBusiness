package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Hashtag;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.helper.WorkTime;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebservicePOST;
import ir.rasen.myapplication.webservice.WebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessProfileInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetBusinessProfileInfo";
    private WebserviceResponse delegate = null;
    private int businessID;
    private ServerAnswer serverAnswer;


    public GetBusinessProfileInfo(int businessID,WebserviceResponse delegate) {
        this.delegate = delegate;
        this.businessID = businessID;
    }

    @Override
    protected Business doInBackground(Void... voids) {
        Business business = new Business();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_PROFILE_INFO, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID))));

        try {

            serverAnswer = webserviceGET.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                business.name = jsonObject.getString(Params.NAME);
                business.profilePicture = jsonObject.getString(Params.PROFILE_PICTURE);
                business.coverPicture = jsonObject.getString(Params.COVER_PICTURE);
                business.category = jsonObject.getString(Params.CATEGORY);
                business.subcategory = jsonObject.getString(Params.SUBCATEGORY);
                business.description = jsonObject.getString(Params.DESCRIPTION);
                WorkTime workTime = new WorkTime();
                workTime.setWorkDaysFromString(jsonObject.getString(Params.WORK_DAYS));
                workTime.setTimeWorkOpenFromString(jsonObject.getString(Params.WORK_TIME_OPEN));
                workTime.setTimeWorkCloseFromString(jsonObject.getString(Params.WORK_TIME_CLOSE));
                business.workTime = workTime;
                business.phone = jsonObject.getString(Params.PHONE);
                business.state = jsonObject.getString(Params.STATE);
                business.city = jsonObject.getString(Params.CITY);
                business.address = jsonObject.getString(Params.ADDRESS);
                business.location_m = new Location_M(jsonObject.getString(Params.LOCATION_LATITUDE),
                        jsonObject.getString(Params.LOCATION_LONGITUDE));
                business.email = jsonObject.getString(Params.EMAIL);
                business.mobile = jsonObject.getString(Params.MOBILE);
                business.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

                return business;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Business result) {
      /*  if (result == null)
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
