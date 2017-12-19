package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ch.ralena.aeonlineschedule.R;

public class OptionsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.settings);
	}
}
