package ch.ralena.aeonlineschedule.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.ChangeBounds;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
	public static final String EXTRA_CLASS_ID = "extra_class_id";
	List<ScheduledClass> scheduledClasses;
	ScheduleAdapter adapter;
	FloatingActionButton fab;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		// load data
		Realm realm = Realm.getDefaultInstance();

		Date date = new Date();
		scheduledClasses = realm.where(ScheduledClass.class).greaterThan("date", date).findAllSorted("date");

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

	private void viewClassDetail(ScheduleAdapter.StudentIdView idView) {
		String classId = idView.getId();
		ClassDetailFragment fragment = new ClassDetailFragment();

		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_CLASS_ID, classId);
		fragment.setArguments(bundle);

		fragment.setSharedElementEnterTransition(new ChangeBounds());



		getFragmentManager().beginTransaction()
				.addSharedElement(idView.getStudentNameView(), "student_name_transition")
				.addSharedElement(idView.getDateView(), "student_date_transition")
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
		bundle.putBoolean(NewClassFragment.EXTRA_IS_NEW, true);
		fragment.setArguments(bundle);

		// for current fragment transition
		Explode fabExplode = new Explode();
		fabExplode.addTarget(fab);
		fabExplode.setDuration(500);
		TransitionSet curTransition = new TransitionSet();
		curTransition.addTransition(fabExplode);
		curTransition.addTransition(new Fade());
		setExitTransition(curTransition);
		setReturnTransition(curTransition);

		// transition for new fragment
		Explode explode = new Explode();
		explode.setEpicenterCallback(new Transition.EpicenterCallback() {
			@Override
			public Rect onGetEpicenter(@NonNull Transition transition) {
				Rect rect = new Rect();
				fab.getGlobalVisibleRect(new Rect());
				return rect;
			}
		});
		TransitionSet transitionSet = new TransitionSet();
		transitionSet.addTransition(explode);
		transitionSet.addTransition(new Fade());
		fragment.setEnterTransition(transitionSet);

		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit();
	}
}
