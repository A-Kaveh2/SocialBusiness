package ir.rasen.myapplication.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import ir.rasen.myapplication.ActivityWelcome;
import ir.rasen.myapplication.R;


public class MyNotification {


	public void notify(Context context,String userName,Bitmap postPicture,Bitmap userPicture) {


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			notifyLowerHonycomb(context,userName,postPicture,userPicture);
		else
			notifyUpperHonycomb(context,userName,postPicture,userPicture);
	}



    public void notifyLowerHonycomb(Context context,String userName,Bitmap postPicture,Bitmap userPicture){
        int NOTIFICATION_ID = 1;
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "", when);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewBitmap(R.id.img_notification_user,userPicture);
        contentView.setImageViewBitmap(R.id.img_notification_post,postPicture);
        contentView.setTextViewText(R.id.txt_notification, userName);

        notification.contentView = contentView;

        Intent notificationIntent = new Intent(context, ActivityWelcome.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.contentIntent = contentIntent;

        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notifyUpperHonycomb(Context context,String userName,Bitmap postPicture,Bitmap userPicture){
        RemoteViews contentView = new RemoteViews(context.getPackageName(),
                R.layout.custom_notification);
        
        contentView.setImageViewBitmap(R.id.img_notification_user,userPicture);
        contentView.setImageViewBitmap(R.id.img_notification_post,postPicture);
        contentView.setTextViewText(R.id.txt_notification, userName);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher).setContent(
                contentView);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, ActivityWelcome.class);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack( ActivityWelcome.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //remoteViews.setOnClickPendingIntent(R.id.button1, resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(100, mBuilder.build());
    }
}
