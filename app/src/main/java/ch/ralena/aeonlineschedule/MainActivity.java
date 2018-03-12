package ch.ralena.aeonlineschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Date;
import java.util.List;

import ch.ralena.aeonlineschedule.fragments.MyClassesFragment;
import ch.ralena.aeonlineschedule.fragments.OptionsFragment;
import ch.ralena.aeonlineschedule.fragments.ScheduleFragment;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import ch.ralena.aeonlineschedule.receivers.ClassAlarmReceiver;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navigationView = findViewById(R.id.navigationView);

		// load proper fragment when menu item is pressed
		navigationView.setOnNavigationItemSelectedListener(item -> {
			Fragment fragment = null;
			// check which menu item was pressed and load fragment object
			switch (item.getItemId()) {
				case R.id.menu_schedule:
					fragment = new ScheduleFragment();
					break;
				case R.id.menu_my_classes:
					fragment = new MyClassesFragment();
					break;
				case R.id.menu_invoice:
					fragment = new ScheduleFragment();
					break;
				case R.id.menu_options:
					fragment = new OptionsFragment();
					break;
			}
			// if a proper press was registered, load that fragment
			if (fragment != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragmentContainer, fragment)
						.commit();
			}
			return true;
		}); // end of item selected listener
		navigationView.setSelectedItemId(R.id.menu_schedule);

		loadAlarms();
	}

	public void loadAlarms() {
		Realm realm = Realm.getDefaultInstance();

		Date date = new Date();
		List<ScheduledClass> classes = realm.where(ScheduledClass.class).greaterThan("date", date).findAll();

		int requestCode = 0;
		for (ScheduledClass scheduledClass : classes) {
			Log.d("MainActivity", String.format("%d - %s", requestCode, scheduledClass.getStudent().getName()));
			Intent intent = new Intent(this, ClassAlarmReceiver.class);
			intent.putExtra(ClassAlarmReceiver.EXTRA_CLASS_ID, scheduledClass.getId());
			intent.putExtra(ClassAlarmReceiver.EXTRA_NOTIFICATION_ID, requestCode);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode++, intent, 0);

			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			long time = scheduledClass.getDate().getTime() - 30 * 60 * 1000;
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
		}

	}
}
