package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.realm.Realm;

import static ch.ralena.aeonlineschedule.fragments.ScheduleFragment.EXTRA_CLASS_ID;

public class ClassDetailFragment extends Fragment {
	private Realm realm;
	private ScheduledClass scheduledClass;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();

		String classId = getArguments().getString(EXTRA_CLASS_ID, null);
		scheduledClass = realm.where(ScheduledClass.class).equalTo("id", classId).findFirst();

		View view = inflater.inflate(R.layout.fragment_class_detail, container, false);


		// find views
		TextView studentName = view.findViewById(R.id.studentNameLabel);
		TextView classTime = view.findViewById(R.id.classTimeLabel);
		TextView classNotes = view.findViewById(R.id.classNotesLabel);
		TextView classSummary = view.findViewById(R.id.classSummaryLabel);
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

		studentName.setText(scheduledClass.getStudent().getName());
		String notes = !scheduledClass.getNotes().equals("") ?
				scheduledClass.getNotes() : "No notes for this class.";
		classNotes.setText(notes);
		String summary = scheduledClass.getSummary() == null || scheduledClass.getSummary().isEmpty() ?
						"No summary for this class yet." : scheduledClass.getSummary();
		classSummary.setText(summary);

		return view;
	}
}
