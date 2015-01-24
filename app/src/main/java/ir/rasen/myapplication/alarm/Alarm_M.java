package ir.rasen.myapplication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.helper.Params;


/**
 * Created by android on 12/29/2014.
 */
public class Alarm_M {

    private int INTENT_ID = 1234567;

    public void checkInterval(Context context, int intervalTime) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        if (preferences.getInt(Params.INTERVAL_TIME, 0) != intervalTime) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(Params.INTERVAL_TIME, intervalTime);
            edit.commit();
            cancel(context);
            set(context);
        }
    }

    public void set(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        if (preferences.getInt(Params.INTERVAL_TIME, 0) == 0) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(Params.INTERVAL_TIME, context.getResources().getInteger(R.integer.interval_time));
            edit.commit();
        }

        int intervalTime = preferences.getInt(Params.INTERVAL_TIME,0);

        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, INTENT_ID, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalTime, sender);
    }

    private void cancel(Context context){
        Intent intentStop = new Intent(context, AlarmReciever.class);
        PendingIntent senderStop = PendingIntent.getBroadcast(context,
                INTENT_ID, intentStop, 0);
        AlarmManager alarmManagerStop = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerStop.cancel(senderStop);
    }
}
