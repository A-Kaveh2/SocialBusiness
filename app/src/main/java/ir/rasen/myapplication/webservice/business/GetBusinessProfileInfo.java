package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import controller.Business;
import helper.Hashtag;
import helper.Location_M;
import helper.Params;
import helper.ServerAnswer;
import helper.URLs;
import helper.WorkTime;
import webservice.WebservicePOST;
import webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessProfileInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetBusinessProfileInfo";
    private WebserviceResponse delegate = null;
    private String businessID;
    private ServerAnswer serverAnswer;


    public GetBusinessProfileInfo(String businessID) {
        this.businessID = businessID;
    }

    @Override
    protected Business doInBackground(Void... voids) {
        Business business = new Business();
        WebservicePOST webservicePOST = new WebservicePOST(URLs.GET_BUSINESS_PROFILE_INFO);
        webservicePOST.addParam(Params.BUSINESS_ID, businessID);
        try {
            serverAnswer = webservicePOST.execute();

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
                workTime.time_open = Integer.valueOf(jsonObject.getString(Params.WORK_TIME_OPEN));
                workTime.time_close = Integer.valueOf(jsonObject.getString(Params.WORK_TIME_CLOSE));
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
        if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
