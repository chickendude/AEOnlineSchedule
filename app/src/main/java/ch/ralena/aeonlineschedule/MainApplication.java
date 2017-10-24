package ch.ralena.aeonlineschedule;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		AndroidThreeTen.init(this);
		Realm.init(this);
		RealmConfiguration config = new RealmConfiguration.Builder()
				.name("myrealm.realm")
				.schemaVersion(0)
				.deleteRealmIfMigrationNeeded()
				.build();
		Realm.setDefaultConfiguration(config);
	}
}
