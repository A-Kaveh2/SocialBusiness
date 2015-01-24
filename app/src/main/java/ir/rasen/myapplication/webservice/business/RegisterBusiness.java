package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import controller.Business;
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
public class RegisterBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RegisterBusiness";

    private WebserviceResponse delegate = null;
    private Business business;
    private ServerAnswer serverAnswer;

    public RegisterBusiness(Business business) {
        this.business = business;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REGISTER_BUSINESS);
        webservicePOST.addParam(Params.USER_ID,business.userID);
        webservicePOST.addParam(Params.BUSINESS_ID, business.businessID);
        webservicePOST.addParam(Params.NAME, business.name);
        webservicePOST.addParam(Params.EMAIL, business.email);
        webservicePOST.addParam(Params.COVER_PICTURE, business.coverPicture);
        webservicePOST.addParam(Params.PROFILE_PICTURE, business.profilePicture);
        webservicePOST.addParam(Params.CATEGORY, business.category);
        webservicePOST.addParam(Params.SUBCATEGORY, business.subcategory);
        webservicePOST.addParam(Params.DESCRIPTION, business.description);
        webservicePOST.addParam(Params.WORK_DAYS, business.workTime.getWorkDaysString());
        webservicePOST.addParam(Params.WORK_TIME_OPEN, String.valueOf(business.workTime.time_open));
        webservicePOST.addParam(Params.WORK_TIME_CLOSE, String.valueOf(business.workTime.time_close));
        webservicePOST.addParam(Params.PHONE, business.phone);
        webservicePOST.addParam(Params.STATE, business.state);
        webservicePOST.addParam(Params.CITY, business.city);
        webservicePOST.addParam(Params.ADDRESS, business.address);
        webservicePOST.addParam(Params.LOCATION_LATITUDE, business.location_m.getLatitude());
        webservicePOST.addParam(Params.LOCATION_LONGITUDE, business.location_m.getLongitude());
        webservicePOST.addParam(Params.EMAIL, business.email);
        webservicePOST.addParam(Params.MOBILE, business.mobile);
        webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(business.hashtagList));

        try {
            serverAnswer = webservicePOST.execute();
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
