package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
	private static final String TAG = ScheduleFragment.class.getSimpleName();
	List<ScheduledClass> scheduledClasses;
	ScheduleAdapter adapter;
	FloatingActionButton fab;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");

		// load data
		Realm realm = Realm.getDefaultInstance();

		Date date = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
		scheduledClasses = realm.where(ScheduledClass.class).greaterThan("date", date).sort("date").findAll();

		// inflate layout
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		fab = view.findViewById(R.id.fab);

		// if there are classes, hide the empty message
		if (scheduledClasses.size() > 0) {
			TextView emptyText = view.findViewById(R.id.emptyText);
			emptyText.setVisibility(View.GONE);
		}

		// load FAB
		FloatingActionButton fab = view.findViewById(R.id.fab);
		fab.setOnClickListener(v -> addNewClass());

		// load recycler view
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		adapter = new ScheduleAdapter(scheduledClasses);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		// click on recyclerview item loads that class's detail page
		adapter.asObservable().subscribe(this::viewClassDetail);

		// set up swipe refresh layout
		SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(() -> {
			adapter.notifyDataSetChanged();
			swipeRefreshLayout.setRefreshing(false);
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		// get current date
		Calendar calendar = Calendar.getInstance();
		// create our date format
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM. d", Locale.ENGLISH);
		// get the starting date
		String startDate = simpleDateFormat.format(calendar.getTime());
		// move one week ahead for end date
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		String endDate = simpleDateFormat.format(calendar.getTime());
		// set title
		getActivity().setTitle(String.format("Schedule: %s - %s", startDate, endDate));
	}

	private void viewClassDetail(ScheduledClass scheduledClass) {
		String classId = scheduledClass.getId();
		ClassDetailFragment fragment = new ClassDetailFragment();

		Bundle bundle = new Bundle();
		bundle.putString(ClassDetailFragment.EXTRA_CLASS_ID, classId);
		fragment.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit();
	}

	/**
	 * Loads the add new class fragment.
	 */
	private void addNewClass() {
		NewClassFragment fragment = new NewClassFragment();
		Bundle bundle = new Bundle();
		// let new class fragment know we are loading it for the first time.
		bundle.putBoolean(NewClassFragment.EXTRA_IS_NEW, true);
		fragment.setArguments(bundle);

		// hide fab because it looks a little weird scrolling
		fab.setVisibility(View.INVISIBLE);
		// set up schedule exit transition
		setExitTransition(new Slide(Gravity.TOP));
		// transition for new fragment
		fragment.setEnterTransition(new Slide(Gravity.BOTTOM));

		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit();
	}
}
