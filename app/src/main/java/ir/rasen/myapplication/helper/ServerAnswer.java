package ir.rasen.myapplication.helper;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ir.rasen.myapplication.R;


public class ServerAnswer {
    private boolean success;
    private JSONObject result;
    private JSONArray resultList;
    private int errorCode;

    public static int EXECUTION_ERROR = -2;
    public static int NONE_ERROR = -1;

   /* public enum Error {
        USERNAME_NOT_EXIST,
        USERNAME_IS_EXIST,
        PASSWORD_INCORRECT,
        EMAIL_NOT_EXIST,
        EMAIL_IS_EXIST,
        FAIL,
        NONE
    }*/

    /*public  Error getError() {
        switch (errorCode) {
            case 401:
                return Error.USERNAME_IS_EXIST;
            case 402:
                return Error.USERNAME_NOT_EXIST;
            case 403:
                return Error.EMAIL_IS_EXIST;
            case 404:
                return Error.EMAIL_NOT_EXIST;
            case 405:
                return Error.PASSWORD_INCORRECT;
            case 406:
                return Error.FAIL;
        }
        return Error.NONE;
    }*/

    public static String getError(Context context, int errorCode) {
        switch (errorCode) {
            case -2:
                return context.getResources().getString(R.string.err_execution);
            case -1:
                return context.getResources().getString(R.string.err_none);
            case 0:
                return context.getResources().getString(R.string.err_unknown);
            case 1:
                return context.getResources().getString(R.string.err_database_connection);
            case 2:
                return context.getResources().getString(R.string.err_user_dose_not_exist);
            case 3:
                return context.getResources().getString(R.string.err_password_incorrect);
            case 4:
                return context.getResources().getString(R.string.err_business_does_not_exist);
            case 5:
                return context.getResources().getString(R.string.err_post_does_not_exist);
            case 6:
                return context.getResources().getString(R.string.err_json_serialization);
            case 7:
                return context.getResources().getString(R.string.err_further_development);
            case 8:
                return context.getResources().getString(R.string.err_empty_post_request);
            case 9:
                return context.getResources().getString(R.string.err_invalid_base64_string);
            case 10:
                return context.getResources().getString(R.string.err_database_constraints_violation);

        }

        return "Undefined";
    }

    public boolean getSuccessStatus() {
        return success;
    }

    public void setSuccessStatus(boolean successStatus) {
        this.success = successStatus;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public JSONArray getResultList() {
        return resultList;
    }

    public void setResultList(JSONArray resultList) {
        this.resultList = resultList;
    }

    public static ServerAnswer getList(HttpResponse httpResponse) throws Exception {
        if(httpResponse == null){
            ServerAnswer serverAnswer = new ServerAnswer();
            serverAnswer.setSuccessStatus(false);
            serverAnswer.setResult(null);
            serverAnswer.setErrorCode(EXECUTION_ERROR);

            return serverAnswer;
        }

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        JSONObject json = new JSONObject(responseBody);

        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setSuccessStatus(json.getBoolean(Params.SUCCESS));
        serverAnswer.setResultList(new JSONArray(json.getString(Params.RESULT)));
        serverAnswer.setErrorCode(json.getInt(Params.ERROR));

        return serverAnswer;
    }

    public static ServerAnswer get(HttpResponse httpResponse) throws Exception {

        if(httpResponse == null){
            ServerAnswer serverAnswer = new ServerAnswer();
            serverAnswer.setSuccessStatus(false);
            serverAnswer.setResult(null);
            serverAnswer.setErrorCode(EXECUTION_ERROR);

            return serverAnswer;
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        JSONObject json = new JSONObject(responseBody);

        //get success status
        boolean sucessStatus = json.getBoolean(Params.SUCCESS);

        //get result
        String resultString = json.getString(Params.RESULT);
        JSONObject resultJsonObject = null;
        if (resultString.length() > 4) {
            try {
                resultJsonObject = new JSONObject(resultString);
            } catch (Exception e) {

            }
        }

        //get error code


        JSONObject errorObject = json.getJSONObject(Params.ERROR);
        String errorCodeString = errorObject.getString(Params.ERROR_CODE);

        int errorCode = NONE_ERROR;
        if (!errorCodeString.equals("null") && !errorCodeString.equals("Null")) {
            errorCode = Integer.valueOf(errorCodeString);
        }


        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setSuccessStatus(sucessStatus);
        serverAnswer.setResult(resultJsonObject);
        serverAnswer.setErrorCode(errorCode);

        return serverAnswer;
    }
}
