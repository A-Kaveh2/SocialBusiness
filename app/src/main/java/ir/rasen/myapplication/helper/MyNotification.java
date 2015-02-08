package ir.rasen.myapplication.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import ir.rasen.myapplication.ActivityWelcome;
import ir.rasen.myapplication.R;


public class MyNotification {



	public Activity activity_running;
	public int icon;
	public String title;
	public String text;
	public int light_color;

	public void notify(Context context, int icon, String title, String text,
			int color) {


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			Lower_Honycomb(context, icon, title, text, color);
		else
			Upper_Honycomb(context, icon, title, text, color);
	}

	@SuppressLint("NewApi")
	private void Upper_Honycomb(Context context, int icon, String title,
			String text, int color) {

		Notification.Builder noteBuilder = new Notification.Builder(context)
				.setAutoCancel(true).setPriority(Notification.PRIORITY_DEFAULT)
				.setSmallIcon(icon).setContentTitle(title).setContentText(text)
				.setLights(color, 500, 500);

   		Intent noteIntent = new Intent(context,
				ActivityWelcome.class);
		PendingIntent notePendingIntent = PendingIntent.getActivity(context,
				0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		noteBuilder.setContentIntent(notePendingIntent);

		Notification note = noteBuilder.build();
		note.defaults = 0;

		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify(0, note);
	}

	@SuppressWarnings("deprecation")
	private void Lower_Honycomb(Context context, int icon, String title,
			String text, int color) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);

		CharSequence tickerText = "تقویم بانو";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE;
		;

		Intent notificationIntent = new Intent(context,
                ActivityWelcome.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, text, contentIntent);
		mNotificationManager.notify(0, notification);
	}


    /*public void noti(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher).setContent(
                remoteViews);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, test.class);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(test.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button1, resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(100, mBuilder.build());
    }*/


    public void noti(Context context){
        int NOTIFICATION_ID = 1;
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "Title", when);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.notifiation_image, R.drawable.ic_menu_like);
        contentView.setTextViewText(R.id.notification_title, "My custom notification title");
        contentView.setTextViewText(R.id.notification_text, "My custom notification text");
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

    public void notiU(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.custom_notification);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher).setContent(
                remoteViews);
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
