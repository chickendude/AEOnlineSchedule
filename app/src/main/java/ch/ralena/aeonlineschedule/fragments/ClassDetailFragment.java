package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.ClassDetailAdapter;
import ch.ralena.aeonlineschedule.objects.ClassType;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.realm.Realm;
import io.realm.Sort;

import static ch.ralena.aeonlineschedule.fragments.ScheduleFragment.EXTRA_CLASS_ID;

public class ClassDetailFragment extends Fragment {
	private Realm realm;
	private ScheduledClass scheduledClass;

	TextView studentName;
	TextView classTypeAndPrice;
	TextView classTime;
	TextView classNotes;
	TextView classSummary;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();

		String classId = getArguments().getString(EXTRA_CLASS_ID, null);
		scheduledClass = realm.where(ScheduledClass.class).equalTo("id", classId).findFirst();

		View view = inflater.inflate(R.layout.fragment_class_detail, container, false);

		// find views
		studentName = view.findViewById(R.id.studentNameLabel);
		classTypeAndPrice = view.findViewById(R.id.classTypeAndPriceLabel);
		classTime = view.findViewById(R.id.classTimeLabel);
		classNotes = view.findViewById(R.id.classNotesLabel);
		classSummary = view.findViewById(R.id.classSummaryLabel);
		// recycler view
		List<ScheduledClass> classes = realm.where(ScheduledClass.class).equalTo("student.id", scheduledClass.getStudent().getId()).findAllSorted("date", Sort.DESCENDING);
		RecyclerView recyclerView = view.findViewById(R.id.classRecyclerView);
		ClassDetailAdapter adapter = new ClassDetailAdapter(classes);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
		// onclick listener for recyclerview adapter
		adapter.asObservable().subscribe(schedClass -> {
			scheduledClass = schedClass;
			loadClassInformation();
		});
		// delete button
		Button deleteButton = view.findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(v -> {
			Snackbar snackbar = Snackbar.make(v, "Are you sure you want to delete? Cannot be undone.", Snackbar.LENGTH_INDEFINITE);
			snackbar.setAction("Delete", view1 -> {
				realm.executeTransaction(
						realm -> realm.where(ScheduledClass.class).equalTo("id", classId).findFirst().deleteFromRealm()
				);
				getFragmentManager().popBackStackImmediate();
			});
			snackbar.show();
		});

		loadClassInformation();

		return view;
	}

	private void loadClassInformation() {
		studentName.setText(scheduledClass.getStudent().getName());
		// set up date
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, hh:mm a", Locale.ENGLISH);
		String dateAndTime = sdf.format(scheduledClass.getDate());
		classTime.setText(dateAndTime);
		// load notes
		String notes = !scheduledClass.getNotes().equals("") ?
				scheduledClass.getNotes() : "No notes for this class.";
		classNotes.setText(notes);
		// load class summary
		String summary = scheduledClass.getSummary() == null || scheduledClass.getSummary().isEmpty() ?
						"No summary for this class yet." : scheduledClass.getSummary();
		classSummary.setText(summary);
		// class price and type
		ClassType cType = scheduledClass.getClassType();
		String classType = String.format(Locale.US, "%s - $%.2f", cType.getName(), cType.getWage());
		classTypeAndPrice.setText(classType);
	}
}
