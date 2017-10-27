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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ClassType;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import ch.ralena.aeonlineschedule.objects.Student;
import io.realm.Realm;

/**
 * Fragment for creating a new class.
 */
public class NewClassFragment extends Fragment {
	View rootView;
	TextView classDateValue;
	TextView classTimeValue;
	FlexboxLayout classTypeFlexbox;
	Calendar calendar;
	Realm realm;
	List<ClassType> classTypes;
	int checkedBoxIndex;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// set bar title
		getActivity().setTitle("Add new Class");

		// load realm and class type from db
		realm = Realm.getDefaultInstance();
		classTypes = realm.where(ClassType.class).equalTo("isEnabled", true).findAll().sort("name");

		rootView = inflater.inflate(R.layout.fragment_new_class, container, false);

		classTypeFlexbox = rootView.findViewById(R.id.classTypeLayout);

		TextView editLabel = rootView.findViewById(R.id.editLabel);
		editLabel.setOnClickListener(view -> {
			ClassTypeFragment classTypeFragment = new ClassTypeFragment();
			getFragmentManager().beginTransaction()
					.replace(R.id.fragmentContainer, classTypeFragment)
					.addToBackStack(null)
					.commit();
		});


		loadClassTypes();
		setUpCalendar();
		setUpDateAndTime();
		setUpSubmitButton();

		return rootView;
	}

	private void loadClassTypes() {
		List<CheckBox> classTypeBoxes = new ArrayList<>();
		for (ClassType classType : classTypes) {
			CheckBox checkBox = new CheckBox(classTypeFlexbox.getContext());
			checkBox.setText(classType.getName());
			checkBox.setOnClickListener(view -> {
				// on click uncheck all other boxes and save index of currently clicked box
				for (CheckBox classTypeBox : classTypeBoxes) {
					classTypeBox.setChecked(false);
				}
				checkBox.setChecked(true);
				checkedBoxIndex = classTypeBoxes.indexOf(checkBox);
			});
			classTypeBoxes.add(checkBox);
			int numViews = classTypeFlexbox.getChildCount();
			classTypeFlexbox.addView(checkBox, numViews - 1);
		}
	}


	/**
	 * Create calendar object and initialize it's values.
	 */
	private void setUpCalendar() {
		// make sure Calendar object is in CST.
		calendar = GregorianCalendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		// clear out minutes, seconds, and ms places
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Sets up onClickListener for the submit button and handles creating/saving the objects in the database.
	 */
	private void setUpSubmitButton() {
		// edit texts for student name and notes
		EditText studentEdit = rootView.findViewById(R.id.studentNameEdit);
		EditText notesEdit = rootView.findViewById(R.id.notesEdit);

		// find button and set up onclick listener
		Button button = rootView.findViewById(R.id.createClassButton);
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
	}

	/**
	 * Sets up onClickListeners for both textviews of date and time.
	 */
	private void setUpDateAndTime() {
		// date
		classDateValue = rootView.findViewById(R.id.classDateValue);
		classDateValue.setOnClickListener(view -> {
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
		});

		// time
		classTimeValue = rootView.findViewById(R.id.classTimeValue);
		classTimeValue.setOnClickListener(view -> {
			TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hourOfDay, minute) -> {
				// if an erroneous time was chosen, just reset it to 0
				if (minute != 0 && minute != 30) {
					minute = 0;
				}
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				Calendar calendarHere = Calendar.getInstance(TimeZone.getDefault());
				calendarHere.setTime(calendar.getTime());

				String classTime = String.format(Locale.ENGLISH, "%02d:%02d CST - %02d:%02d local", hourOfDay, minute, calendarHere.get(Calendar.HOUR_OF_DAY), calendarHere.get(Calendar.MINUTE));

				classTimeValue.setText(classTime);
			};
			TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
			timePickerDialog.show();
		});
	}
}
