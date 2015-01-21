package ir.rasen.myapplication.helper;

import android.util.Log;

/**
 * Created by android on 12/15/2014.
 */
public class ResultStatus {

    public boolean success;
    public ServerAnswer.Error serverError;

    public ResultStatus(boolean success, ServerAnswer.Error error) {
        this.success = success;
        this.serverError = error;
    }

    public static ResultStatus getResultStatus(ServerAnswer serverAnswer) {
        if (serverAnswer == null)
            return new ResultStatus(false, ServerAnswer.Error.NONE);

        if (serverAnswer.getSuccessStatus())
            return new ResultStatus(true, ServerAnswer.Error.NONE);
        else
            return new ResultStatus(false, serverAnswer.getError());
    }


}
