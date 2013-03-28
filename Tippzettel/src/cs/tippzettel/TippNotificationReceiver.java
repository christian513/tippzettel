package cs.tippzettel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import cs.tippzettel.model.TippabgabeStatus;

public class TippNotificationReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		TippabgabeStatus status = TippConnection.getTippabgabeStatus();
		if (!status.isGetippt()) {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.tippzettel).setContentTitle("Tippen")
					.setContentText("Tipps für " + status.getSpieltag() + ". Spieltag abgeben");
			Intent resultIntent = new Intent(context, TippabgabeActivity.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(TippabgabeActivity.class);
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = mBuilder.build();
			mNotificationManager.notify(NOTIFICATION_ID, notification);

		}
	}

}
