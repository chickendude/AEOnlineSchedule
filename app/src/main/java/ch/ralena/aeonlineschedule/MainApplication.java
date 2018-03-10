package ch.ralena.aeonlineschedule;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

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
//				.deleteRealmIfMigrationNeeded()
				.build();
		Realm.setDefaultConfiguration(config);

		// Initialize Stetho
		Stetho.initialize(
				Stetho.newInitializerBuilder(this)
						.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
						.enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
						.build());
	}
}
