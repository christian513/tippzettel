package cs.tippzettel;

import cs.tippzettel.model.Tipprunde;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class TippNotificationReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		//TODO nur notifizieren wenn noch nicht getippt
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.tippzettel)
				.setContentTitle("Tippen")
				.setContentText("Tipps für x. Spieltag abgeben");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, TippabgabeActivity.class);
		
		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(TippabgabeActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		Notification notification = mBuilder.build();
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

}
