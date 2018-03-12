package ch.ralena.aeonlineschedule.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import ch.ralena.aeonlineschedule.MainActivity;
import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.realm.Realm;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ClassAlarmReceiver extends BroadcastReceiver {
	public static final String EXTRA_CLASS_ID = "extra_student_name";
	public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";

	@Override
	public void onReceive(Context context, Intent intent) {
		String classId = intent.getStringExtra(EXTRA_CLASS_ID);
		int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);

		Realm realm = Realm.getDefaultInstance();

		ScheduledClass scheduledClass = realm.where(ScheduledClass.class).equalTo("id", classId).findFirst();

		String contentText = String.format("Class with %s at %s", scheduledClass.getStudent().getName(), scheduledClass.getTime());

		Notification n = new NotificationCompat.Builder(context, classId)
				.setContentTitle("AEOnline Class")
				.setContentText(contentText)
				.setSmallIcon(R.drawable.icon_my_classes)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
				.build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(notificationId, n);

		Toast.makeText(context, "YAY", Toast.LENGTH_SHORT).show();
		Log.d(getClass().getSimpleName(), "I ran!");
	}
}
