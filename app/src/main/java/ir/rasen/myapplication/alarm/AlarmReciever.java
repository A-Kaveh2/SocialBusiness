package ir.rasen.myapplication.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.internal.id;

import ir.rasen.myapplication.ActivityLogin;
import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.ActivityWelcome;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.CommentNotification;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.MyNotification;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.announcement.GetLastCommentNotification;

/**
 * Created by android on 12/29/2014.
 */
public class AlarmReciever extends BroadcastReceiver implements WebserviceResponse {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        new GetLastCommentNotification(context, LoginInfo.getUserId(context), AlarmReciever.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof CommentNotification) {
            CommentNotification commentNotification = (CommentNotification) result;

            if (CommentNotification.isDisplayed(context, commentNotification.id))
                return;

            //save comment.id in sharePreferences storage to check isDisplayed before
            CommentNotification.insertLastCommentId(context, commentNotification.id);

            //TODO check if activity is on top
            Intent intent = new Intent(context, ActivityLogin.class);
            intent.putExtra(Params.NOTIFICATION, true);
            MyNotification.displayNotificationCustomView(context, intent, commentNotification.getCommentNotificationContentView(context), R.drawable.ic_launcher);
        }
    }

    @Override
    public void getError(Integer errorCode) {

    }


}
