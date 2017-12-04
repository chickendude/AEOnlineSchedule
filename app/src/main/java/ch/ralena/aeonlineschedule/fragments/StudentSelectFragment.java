package ch.ralena.aeonlineschedule.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.StudentSelectAdapter;
import ch.ralena.aeonlineschedule.objects.Student;
import io.realm.Case;
import io.realm.Realm;

public class StudentSelectFragment extends Fragment {
	Realm realm;
	List<Student> students;
	EditText studentEdit;
	StudentSelectAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();
		SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

		students = realm.where(Student.class).findAllSorted("name");
		View view = inflater.inflate(R.layout.fragment_student_select, container, false);

		// text watcher to update student names in RecyclerView
		studentEdit = view.findViewById(R.id.studentNameText);
		studentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				adapter.updateStudents(realm.where(Student.class).contains("name", charSequence.toString(), Case.INSENSITIVE).findAllSorted("name"));
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		// set up recyclerview
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		adapter = new StudentSelectAdapter(students);
		recyclerView.setAdapter(adapter);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
		recyclerView.addItemDecoration(itemDecoration);

		// set up rxjava subscriptions to clicks
		adapter.asObservableStudent().subscribe(student -> {
			sharedPreferences.edit().putString(NewClassFragment.KEY_STUDENT_ID, student.getId()).apply();
			getFragmentManager().popBackStackImmediate();
		});
		adapter.asObservableButton().subscribe(v -> {
			String name = studentEdit.getText().toString().trim();
			if (name.isEmpty()) {
				Toast.makeText(getContext(), "Student name must not be empty!", Toast.LENGTH_SHORT).show();
			} else {
				realm.executeTransaction(realm -> {
					Student student = realm.createObject(Student.class, UUID.randomUUID().toString());
					student.setName(name);
					sharedPreferences.edit().putString(NewClassFragment.KEY_STUDENT_ID, student.getId()).apply();
				});
				getFragmentManager().popBackStackImmediate();
			}
		});


		return view;
	}
}
