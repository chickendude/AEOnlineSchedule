package ch.ralena.aeonlineschedule.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ClassType;
import io.realm.Realm;

public class OptionsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

	SharedPreferences sharedPreferences;
	Realm realm;

	@Override
	public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
		realm = Realm.getDefaultInstance();
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	public void onResume() {
		super.onResume();
		sharedPreferences = getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("pref_pay_rate")) {
			String payRate = sharedPreferences.getString(key, "15.00");

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Update All Class Types?");
			builder.setMessage(String.format(Locale.getDefault(), "Would you like to change the pay rate for all class types to $%s?", payRate));
			builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
				List<ClassType> classTypes = realm.where(ClassType.class).findAll();
				Float wage = Float.parseFloat(payRate);
				realm.executeTransaction(r -> {
					for (ClassType classType : classTypes) {
						classType.setWage(wage);
					}
				});
			});
			builder.setNegativeButton(android.R.string.no, null);
			builder.show();
		}
	}


}
