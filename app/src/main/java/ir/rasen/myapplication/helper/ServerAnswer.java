package ir.rasen.myapplication.helper;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerAnswer {
    private boolean success;
    private JSONObject result;
    private JSONArray resultList;
    private int errorCode;

    public enum Error {
        USERNAME_NOT_EXIST,
        USERNAME_IS_EXIST,
        PASSWORD_INCORRECT,
        EMAIL_NOT_EXIST,
        EMAIL_IS_EXIST,
        FAIL,
        NONE
    }

    public  Error getError() {
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
}
