package ir.rasen.myapplication.webservice.business;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;
import ir.rasen.myapplication.webservice.WebserviceGET;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessContactInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetBusinessContactInfo";
    private WebserviceResponse delegate = null;
    private int businessID;
    private ServerAnswer serverAnswer;

    public GetBusinessContactInfo(int businessID, WebserviceResponse delegate) {
        this.businessID = businessID;
        this.delegate = delegate;
    }

    @Override
    protected Business doInBackground(Void... voids) {


        Business business = new Business();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_HOME_INFO,new ArrayList<>(
                Arrays.asList(String.valueOf(businessID))));

        try {
            serverAnswer = webserviceGET.execute();

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                business.id = businessID;
                business.location_m.setLatitude(jsonObject.getString(Params.LOCATION_LATITUDE));
                business.location_m.setLongitude(jsonObject.getString(Params.LOCATION_LONGITUDE));
                business.workTime.setTimeWorkOpenFromString(jsonObject.getString(Params.WORK_TIME_OPEN));
                business.workTime.setTimeWorkCloseFromString(jsonObject.getString(Params.WORK_TIME_CLOSE));
                business.webSite = jsonObject.getString(Params.WEBSITE);
                business.email = jsonObject.getString(Params.EMAIL);
                business.phone = jsonObject.getString(Params.PHONE);
                business.mobile = jsonObject.getString(Params.MOBILE);

                return business;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Business result) {
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
