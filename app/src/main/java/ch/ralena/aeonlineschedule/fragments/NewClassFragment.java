package ch.ralena.aeonlineschedule.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import ch.ralena.aeonlineschedule.objects.Student;
import io.realm.Realm;

/**
 * Fragment for creating a new class.
 */

public class NewClassFragment extends Fragment {
	TextView classDateValue;
	TextView classTimeValue;
	Calendar calendar;
	Realm realm;

	/**
	 * OnClickListener to brings up a time dialog when the view is clicked.
	 */
	View.OnClickListener classTimeClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hourOfDay, minute) -> {
				// if an erroneous time was chosen, just reset it to 0
				if (minute != 0 && minute != 30) {
					minute = 0;
				}
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				Calendar calendarHere = Calendar.getInstance(TimeZone.getDefault());
				calendarHere.setTime(calendar.getTime());

				String classTime = String.format(Locale.ENGLISH, "%02d:%02d/%02d:%02d", hourOfDay, minute, calendarHere.get(Calendar.HOUR_OF_DAY), calendarHere.get(Calendar.MINUTE));

				classTimeValue.setText(classTime);
			};
			TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
			timePickerDialog.show();
		}
	};

	View.OnClickListener classDateClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, dayOfMonth) -> {
				// convert returned integer values into a calendar object to pass into the SimpleDateFormat object
				calendar.set(year, month, dayOfMonth);
				// Sat. Oct. 21, 2017
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E. MMM. d, yyyy", Locale.US);
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
				// update text
				classDateValue.setText(simpleDateFormat.format(calendar.getTime()));
			};
			Calendar c = Calendar.getInstance(TimeZone.getDefault());
			DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
					onDateSetListener,
					c.get(Calendar.YEAR),
					c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
		}
	};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// set bar title
		getActivity().setTitle("Add new Class");

		realm = Realm.getDefaultInstance();

		// make sure Calendar object is in CST.
		calendar = GregorianCalendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		// clear out seconds and ms places
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		View view = inflater.inflate(R.layout.fragment_new_class, container, false);

		EditText studentEdit = view.findViewById(R.id.studentNameEdit);
		EditText notesEdit = view.findViewById(R.id.notesEdit);

		// set up click listeners
		TextView classDateLabel = view.findViewById(R.id.classDateLabel);
		classDateLabel.setOnClickListener(classDateClickListener);
		classDateValue = view.findViewById(R.id.classDateValue);
		classDateValue.setOnClickListener(classDateClickListener);

		TextView classTimeLabel = view.findViewById(R.id.classTimeLabel);
		classTimeLabel.setOnClickListener(classTimeClickListener);
		classTimeValue = view.findViewById(R.id.classTimeValue);
		classTimeValue.setOnClickListener(classTimeClickListener);

		// submit button
		Button button = view.findViewById(R.id.createClassButton);
		button.setOnClickListener(v -> {
			// create class
			realm.executeTransaction(realm -> {
				// create student object
				Student student = realm.createObject(Student.class, UUID.randomUUID().toString());
				student.setName(studentEdit.getText().toString());
				// create class object
				ScheduledClass scheduledClass = realm.createObject(ScheduledClass.class, UUID.randomUUID().toString());
				scheduledClass.setStudent(student);
				scheduledClass.setDate(calendar.getTime());
				scheduledClass.setNotes(notesEdit.getText().toString());
				getActivity().onBackPressed();
			});
		});

		return view;
	}
}
