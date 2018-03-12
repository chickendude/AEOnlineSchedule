package ch.ralena.aeonlineschedule;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MainApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		AndroidThreeTen.init(this);
		Realm.init(this);
		RealmConfiguration config = new RealmConfiguration.Builder()
				.name("myrealm.realm")
				.schemaVersion(1)
				.migration(new Migration())
				.build();
		Realm.setDefaultConfiguration(config);

		if (BuildConfig.DEBUG) {
			// Initialize Stetho
			Stetho.initialize(
					Stetho.newInitializerBuilder(this)
							.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
							.enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
							.build());
		}
	}

	public class Migration implements RealmMigration {

		@Override
		public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
			RealmSchema schema = realm.getSchema();

			/**
			 * Migrate to version 1: Add new fields
			 * + String chineseName
			 * + String homeTown
			 * + String currentTown
			 * + ClassType defaultClassType
			 * */
			if (oldVersion == 0) {
				schema.get("Student")
						.addField("chineseName", String.class)
						.addField("homeTown", String.class)
						.addField("currentTown", String.class)
						.addRealmObjectField("defaultClassType", schema.get("ClassType"));
			}
		}
	}
}
