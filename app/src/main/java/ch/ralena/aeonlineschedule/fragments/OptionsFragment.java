package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ch.ralena.aeonlineschedule.R;

public class OptionsFragment extends com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat {

	@Override
	public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.settings);
	}
}
