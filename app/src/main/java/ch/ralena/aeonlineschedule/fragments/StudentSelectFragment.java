package ch.ralena.aeonlineschedule.fragments;

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
		students = realm.where(Student.class).findAllSorted("name");
		View view = inflater.inflate(R.layout.fragment_student_select, container, false);

		studentEdit = view.findViewById(R.id.studentNameEdit);
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
			Toast.makeText(getContext(), student.getName(), Toast.LENGTH_SHORT).show();
		});
		adapter.asObservableButton().subscribe(v -> {
			String name = studentEdit.getText().toString().trim();
			if (name.isEmpty()) {
				Toast.makeText(getContext(), "Student name must not be empty!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "Creating student!", Toast.LENGTH_SHORT).show();
			}
		});


		return view;
	}
}
