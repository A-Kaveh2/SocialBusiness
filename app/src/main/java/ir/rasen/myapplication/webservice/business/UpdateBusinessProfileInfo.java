package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.myapplication.classes.Business;
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
public class UpdateBusinessProfileInfo extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateProfileInfo";

    private WebserviceResponse delegate = null;
    private Business business;
    private ServerAnswer serverAnswer;

    public UpdateBusinessProfileInfo(Business business,WebserviceResponse delegate) {
        this.business = business;
        this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PROFILE_BUSINESS);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID,String.valueOf( business.id));
            webservicePOST.addParam(Params.NAME, business.name);
            webservicePOST.addParam(Params.EMAIL, business.email);
            if (business.coverPicture != null)
                webservicePOST.addParam(Params.COVER_PICTURE, business.coverPicture);
            else
                webservicePOST.addParam(Params.COVER_PICTURE, "");
            if (business.profilePicture != null)
                webservicePOST.addParam(Params.PROFILE_PICTURE, business.profilePicture);
            else
                webservicePOST.addParam(Params.PROFILE_PICTURE, "");
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
