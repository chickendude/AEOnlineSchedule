package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.StudentSelectAdapter;
import ch.ralena.aeonlineschedule.objects.Student;
import io.realm.Realm;

public class StudentSelectFragment extends Fragment {
	Realm realm;
	List<Student> students;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();
		students = realm.where(Student.class).findAllSorted("name");
		View view = inflater.inflate(R.layout.fragment_student_select, container, false);

		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		StudentSelectAdapter adapter = new StudentSelectAdapter(students);
		recyclerView.setAdapter(adapter);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
		recyclerView.addItemDecoration(itemDecoration);
		return view;
	}
}
