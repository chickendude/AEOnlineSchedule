package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.NewClassTypeAdapter;
import ch.ralena.aeonlineschedule.objects.ClassType;

/**
 * Fragment for viewing and editing/creating class types.
 */

public class ClassTypeFragment extends Fragment {
	RecyclerView classTypeRecyclerView;
	RecyclerView newClassTypeRecyclerView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_class_type, container, false);

		classTypeRecyclerView = view.findViewById(R.id.classTypeRecyclerView);

		setUpNewClassRecyclerView();

		return view;
	}

	private void setUpNewClassRecyclerView() {
		List<ClassType> classTypes = new ArrayList<>();
		// classes that take 40 minutes
		String[] classType40Names = {"GES", "GE", "BE", "IELTS", "TOEFL", "SAT", "ACT", "EENG",
				"MKTADV", "FIN", "HR", "IT", "Demo"};
		for (String classTypeName : classType40Names) {
			ClassType classType = new ClassType();
			classType.setName(classTypeName);
			classType.setNumMinutes(40);
			classType.setWage(15f);
			classTypes.add(classType);
		}
		// classes that take 45 minutes
		String[] classType45Names = {"Group"};
		for (String classTypeName : classType45Names) {
			ClassType classType = new ClassType();
			classType.setName(classTypeName);
			classType.setNumMinutes(40);
			classType.setWage(15f);
			classTypes.add(classType);
		}
		Collections.sort(classTypes, (cT1, cT2) -> cT1.getName().compareTo(cT2.getName()));
		// create adapter and set layout manager
		NewClassTypeAdapter adapter = new NewClassTypeAdapter(classTypes);
		classTypeRecyclerView.setAdapter(adapter);
		classTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
	}
}
