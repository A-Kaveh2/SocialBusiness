package ir.rasen.myapplication.helper;

/**
 * Created by android on 12/15/2014.
 */
public class ResultStatus {

    public boolean success;
    public int errorCode;

    public ResultStatus(boolean success, int errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public static ResultStatus getResultStatus(ServerAnswer serverAnswer) {
        if (serverAnswer == null)
            return new ResultStatus(false, ServerAnswer.EXECUTION_ERROR);

        if (serverAnswer.getSuccessStatus())
            return new ResultStatus(true, ServerAnswer.NONE_ERROR);
        else
            return new ResultStatus(false, serverAnswer.getErrorCode());

    }


}
