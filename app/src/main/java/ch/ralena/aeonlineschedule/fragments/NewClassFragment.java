package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.ralena.aeonlineschedule.R;

/**
 * Fragment for creating a new class.
 */

public class NewClassFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle("Add New Class");
		View view = inflater.inflate(R.layout.fragment_new_class, container, false);
		return view;
	}
}
