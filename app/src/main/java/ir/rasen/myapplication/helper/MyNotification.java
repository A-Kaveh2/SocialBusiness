package ir.rasen.myapplication.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import ir.rasen.myapplication.ActivityWelcome;


public class MyNotification {

	

	public Activity activity_running;
	public int icon;
	public String title;
	public String text;
	public int light_color;
	
	public void Notify(Activity activity, int icon, String title, String text,
			int color) {


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			Lower_Honycomb(activity, icon, title, text, color);
		else
			Upper_Honycomb(activity, icon, title, text, color);
	}

	@SuppressLint("NewApi")
	private void Upper_Honycomb(Activity activity, int icon, String title,
			String text, int color) {
		Notification.Builder noteBuilder = new Notification.Builder(activity)
				.setAutoCancel(true).setPriority(Notification.PRIORITY_DEFAULT)
				.setSmallIcon(icon).setContentTitle(title).setContentText(text)
				.setLights(color, 500, 500); 

   		Intent noteIntent = new Intent(activity,
				ActivityWelcome.class);
		PendingIntent notePendingIntent = PendingIntent.getActivity(activity,
				0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		noteBuilder.setContentIntent(notePendingIntent);

		Notification note = noteBuilder.build();
		note.defaults = 0;

		NotificationManager mgr = (NotificationManager) activity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify(0, note);
	}

	@SuppressWarnings("deprecation")
	private void Lower_Honycomb(Activity activity, int icon, String title,
			String text, int color) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) activity
				.getSystemService(ns);

		CharSequence tickerText = "تقویم بانو";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE;
		;

		Intent notificationIntent = new Intent(activity,
                ActivityWelcome.class);
		PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(activity, title, text, contentIntent);
		mNotificationManager.notify(0, notification);
	}
}
