package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.Student;
import io.realm.Realm;

public class EditStudentFragment extends Fragment {
	public static final String EXTRA_STUDENT_ID = "extra_student_id";

	ActionBar actionBar;

	Realm realm;
	Student student;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		realm = Realm.getDefaultInstance();

		String studentId = getArguments().getString(EXTRA_STUDENT_ID);
		student = realm.where(Student.class).equalTo("id", studentId).findFirst();

		View view = inflater.inflate(R.layout.fragment_edit_student, container, false);

		EditText studentNameEdit = view.findViewById(R.id.studentNameEdit);
		studentNameEdit.setText(student.getName());

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStackImmediate();
				break;
		}
		return true;
	}

	@Override
	public void onStop() {
		super.onStop();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);
	}
}
