package ir.rasen.myapplication.helper;

public class PassingWorkTime {

    /**
     * Created by 'SINA KH'.
     */
    private static final PassingWorkTime instance = new PassingWorkTime();

    private WorkTime workTime = new WorkTime();

    private PassingWorkTime(){}

    public static PassingWorkTime getInstance(){
        return instance;
    }

    public void setValue(WorkTime workTime){
        this.workTime = workTime;
    }

    public WorkTime getValue(){
        return workTime;
    }

}
