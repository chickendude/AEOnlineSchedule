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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.ScheduleAdapter;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.realm.Realm;

/**
 * Fragment that lists your schedule.
 * You can also add new schedules by clicking the FAB button.
 */
public class ScheduleFragment extends Fragment {
	List<ScheduledClass> scheduledClasses;
	ScheduleAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		// load data
		Realm realm = Realm.getDefaultInstance();

		Date date = new Date();
		scheduledClasses = realm.where(ScheduledClass.class).greaterThan("date", date).findAllSorted("date");

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

	@Override
	public void onStart() {
		super.onStart();
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.MONTH);
		String month = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date());
		getActivity().setTitle("Schedule for " + month);
	}

	/**
	 * Loads the add new class fragment.
	 */
	private void addNewClass() {
		NewClassFragment fragment = new NewClassFragment();
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit();
	}
}
