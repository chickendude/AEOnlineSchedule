package ch.ralena.aeonlineschedule.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import ch.ralena.aeonlineschedule.R;

public class OptionsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

	SharedPreferences sharedPreferences;

	@Override
	public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
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
//			Map<String, ?> map = sharedPreferences.getAll();
//			EditTextPreference editTextPreference = (EditTextPreference) map.get(key);
//			EditTextPreference payRate = (EditTextPreference) sharedPreferences.getAll().get(key);
//			payRate.setSummary(String.format("$%s", payRate.getText()));
		}
	}
}
