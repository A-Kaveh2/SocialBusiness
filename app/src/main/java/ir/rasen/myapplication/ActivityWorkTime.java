package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingWorkTime;
import ir.rasen.myapplication.helper.WorkTime;

public class ActivityWorkTime extends Activity {
    private static String TAG = "ACTIVITY_WORK_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_work_time);

        if(PassingWorkTime.getInstance().getValue()!=null) {
            WorkTime workTime = PassingWorkTime.getInstance().getValue();
            ((CheckBox) findViewById(R.id.check1)).setChecked(workTime.getWorkDays()[0]);
            ((CheckBox) findViewById(R.id.check2)).setChecked(workTime.getWorkDays()[1]);
            ((CheckBox) findViewById(R.id.check3)).setChecked(workTime.getWorkDays()[2]);
            ((CheckBox) findViewById(R.id.check4)).setChecked(workTime.getWorkDays()[3]);
            ((CheckBox) findViewById(R.id.check5)).setChecked(workTime.getWorkDays()[4]);
            ((CheckBox) findViewById(R.id.check6)).setChecked(workTime.getWorkDays()[5]);
            ((CheckBox) findViewById(R.id.check7)).setChecked(workTime.getWorkDays()[6]);
            ((TimePicker) findViewById(R.id.timePicker_working_time_start)).setCurrentHour(workTime.getTime_open()/60);
            ((TimePicker) findViewById(R.id.timePicker_working_time_start)).setCurrentMinute(workTime.getTime_open()%60);
            ((TimePicker) findViewById(R.id.timePicker_working_time_stop)).setCurrentHour(workTime.getTime_close() / 60);
            ((TimePicker) findViewById(R.id.timePicker_working_time_stop)).setCurrentMinute(workTime.getTime_close() % 60);
        }
    }

    // SUBMIT TOUCHED
    public void submit(View view) {
        WorkTime workTime = new WorkTime();
        try {
            if (((CheckBox) findViewById(R.id.check1)).isChecked())
                workTime.addWorkDay(1);
            if (((CheckBox) findViewById(R.id.check2)).isChecked())
                workTime.addWorkDay(2);
            if (((CheckBox) findViewById(R.id.check3)).isChecked())
                workTime.addWorkDay(3);
            if (((CheckBox) findViewById(R.id.check4)).isChecked())
                workTime.addWorkDay(4);
            if (((CheckBox) findViewById(R.id.check5)).isChecked())
                workTime.addWorkDay(5);
            if (((CheckBox) findViewById(R.id.check6)).isChecked())
                workTime.addWorkDay(6);
            if (((CheckBox) findViewById(R.id.check7)).isChecked())
                workTime.addWorkDay(7);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        workTime.time_open =
                ((TimePicker) findViewById(R.id.timePicker_working_time_start)).getCurrentHour()*60
                + ((TimePicker) findViewById(R.id.timePicker_working_time_start)).getCurrentMinute();
        workTime.time_close =
                ((TimePicker) findViewById(R.id.timePicker_working_time_stop)).getCurrentHour()*60
                        + ((TimePicker) findViewById(R.id.timePicker_working_time_stop)).getCurrentMinute();
        PassingWorkTime.getInstance().setValue(workTime);
        Intent i = new Intent();
        setResult(Params.INTENT_OK , i);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void onBackPressed() {
        Intent i = new Intent();
        setResult(Params.INTENT_ERROR , i);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

    // week day clicked
    public void weekDayClicked(View v) {
        CheckBox check = (CheckBox) (((RelativeLayout) v).getChildAt(0));
        if(check.isChecked())
            check.setChecked(false);
        else
            check.setChecked(true);
    }

}
