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
	EditText studentNameEdit;
	EditText studentNameChineseEdit;
	EditText homeTownEdit;
	EditText currentTownEdit;
	EditText notesEdit;

	Realm realm;
	Student student;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		actionBar = activity.getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		realm = Realm.getDefaultInstance();

		String studentId = getArguments().getString(EXTRA_STUDENT_ID);
		student = realm.where(Student.class).equalTo("id", studentId).findFirst();

		View view = inflater.inflate(R.layout.fragment_edit_student, container, false);

		// set up EditTexts
		studentNameEdit = view.findViewById(R.id.studentNameEdit);
		studentNameEdit.setText(student.getName());

		studentNameChineseEdit = view.findViewById(R.id.studentNameChineseEdit);
		studentNameChineseEdit.setText(student.getChineseName());

		homeTownEdit = view.findViewById(R.id.homeTownEdit);
		homeTownEdit.setText(student.getHomeTown());

		currentTownEdit = view.findViewById(R.id.currentTownEdit);
		currentTownEdit.setText(student.getCurrentTown());

		notesEdit = view.findViewById(R.id.notesEdit);
		notesEdit.setText(student.getNotes());

		activity.setTitle("Editing " + student.getName());

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.check, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStackImmediate();
				break;
			case R.id.check:
				realm.executeTransaction(realm -> {
					student.setName(studentNameEdit.getText().toString());
					student.setChineseName(studentNameChineseEdit.getText().toString());
					student.setHomeTown(homeTownEdit.getText().toString());
					student.setCurrentTown(currentTownEdit.getText().toString());
					student.setNotes(notesEdit.getText().toString());
				});
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
