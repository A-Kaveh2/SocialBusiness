package ir.rasen.myapplication.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.google.android.gms.internal.id;

import ir.rasen.myapplication.ActivityWelcome;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.CommentNotification;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.MyNotification;
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
        new GetLastCommentNotification(context, LoginInfo.getUserId(context),AlarmReciever.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if(result instanceof CommentNotification){
            CommentNotification commentNotification = (CommentNotification)result;

            if (Comment.isDisplayed(context, commentNotification.id))
                return;

            Comment.insertLastCommentId(context, commentNotification.id);

            MyNotification notification = new MyNotification();
            notification.notify(context, commentNotification.userName,
                    Image_M.getBitmapFromString(commentNotification.postPicture),
                    Image_M.getBitmapFromString(commentNotification.userPicture));
        }
    }

    @Override
    public void getError(Integer errorCode) {

    }

    @SuppressLint("NewApi")

    public void displayNotification (Context context){

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(context,ActivityWelcome.class);
        PendingIntent  pending=PendingIntent.getActivity(context, 0, intent, 0);
        Notification noti;
        if (Build.VERSION.SDK_INT < 11) {
            noti = new Notification(icon, "Title", when);
            noti.setLatestEventInfo(
                    context,
                    "Title",
                    "Text",
                    pending);
        } else {
            noti = new Notification.Builder(context)
                    .setContentTitle("Title")
                    .setContentText(
                            "Text").
                            setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pending).setWhen(when).setAutoCancel(true)
                    .build();
        }

        noti.contentView = new RemoteViews(context.getPackageName(),
                R.layout.notification);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        nm.notify(0, noti);
    }
}
