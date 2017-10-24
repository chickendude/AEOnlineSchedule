package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.ScheduleAdapter;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import ch.ralena.aeonlineschedule.objects.Student;

/**
 * Fragment that lists your schedule.
 */
public class ScheduleFragment extends Fragment {
	List<ScheduledClass> scheduledClasses;
	ScheduleAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		// load data
		scheduledClasses = new ArrayList<>();

		//////////////////////////////////////

		Student student = new Student("Chris");
		Student student2 = new Student("Christopher");
		ScheduledClass scheduledClass = new ScheduledClass(new Date(), "", student);
		ScheduledClass scheduledClass2 = new ScheduledClass(new Date(), "", student2);
		scheduledClasses.add(scheduledClass);
		scheduledClasses.add(scheduledClass2);
		scheduledClasses.add(scheduledClass);

		///////////////////////////////////////

		// inflate layout
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		// load FAB
		FloatingActionButton fab = view.findViewById(R.id.fab);
		fab.setOnClickListener(v -> addNewClass());

		// load recycler view
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		adapter = new ScheduleAdapter(scheduledClasses);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		return view;
	}

	private void addNewClass() {
		NewClassFragment fragment = new NewClassFragment();
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit();
	}
}
