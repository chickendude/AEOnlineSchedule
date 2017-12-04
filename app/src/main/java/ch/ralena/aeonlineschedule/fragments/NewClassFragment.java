package ch.ralena.aeonlineschedule.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

/**
 * Fragment for creating a new class.
 */
public class NewClassFragment extends Fragment {
	public static final String EXTRA_IS_NEW = "extra_is_new";
	public static final String KEY_DATE = "key_date";
	public static final String KEY_TIME = "key_time";

	View rootView;
	TextView classDateValue;
	TextView classTimeValue;
	FlexboxLayout classTypeFlexbox;

	Calendar calendar;
	Realm realm;
	List<ClassType> classTypes;
	int checkedBoxIndex;

	SharedPreferences sharedPreferences;
	PublishSubject<Calendar> datePublish = PublishSubject.create();
	PublishSubject<Calendar> timePublish = PublishSubject.create();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// set bar title
		getActivity().setTitle("Add new Class");

		sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

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

		TextView studentNameText = rootView.findViewById(R.id.studentNameText);
		studentNameText.setOnClickListener(view -> {
			StudentSelectFragment fragment = new StudentSelectFragment();
			getFragmentManager().beginTransaction()
					.replace(R.id.fragmentContainer, fragment)
					.addToBackStack(null)
					.commit();
		});


		loadClassTypes();
		setUpCalendar();
		setUpDateAndTime();
		setUpSubmitButton();

		// check if we need to load saved data
		boolean is_new = getArguments().getBoolean(EXTRA_IS_NEW);
		if (is_new) {
			getArguments().putBoolean(EXTRA_IS_NEW, false);
			sharedPreferences.edit()
					.remove(KEY_DATE)
					.remove(KEY_TIME)
					.apply();
		} else {
			long dateMillis = sharedPreferences.getLong(KEY_DATE, 0);
			long timeMillis = sharedPreferences.getLong(KEY_TIME, 0);
			TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
			Calendar dateCal = GregorianCalendar.getInstance();
			Calendar timeCal = Calendar.getInstance();
			dateCal.setTimeZone(timeZone);
			timeCal.setTimeZone(timeZone);
			dateCal.setTimeInMillis(dateMillis);
			timeCal.setTimeInMillis(timeMillis);
			int year = dateCal.get(Calendar.YEAR);
			int month = dateCal.get(Calendar.MONTH);
			int date = dateCal.get(Calendar.DATE);
			int hour = timeCal.get(Calendar.HOUR_OF_DAY);
			int minute = timeCal.get(Calendar.MINUTE);
			calendar.set(year, month, date, hour, minute);
			if (dateMillis > 0)
				datePublish.onNext(calendar);
			if (timeMillis > 0)
				timePublish.onNext(calendar);
		}

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
		TextView studentName = rootView.findViewById(R.id.studentNameText);
		EditText notesEdit = rootView.findViewById(R.id.notesEdit);

		// find button and set up onclick listener
		Button button = rootView.findViewById(R.id.createClassButton);
		button.setOnClickListener(v -> {
			// create class
			realm.executeTransaction(realm -> {
				// create student object
				Student student = realm.createObject(Student.class, UUID.randomUUID().toString());
				student.setName(studentName.getText().toString());
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
		// set up observables for date and time
		datePublish.subscribe(cal -> {
			// set up date
			// Sat. Oct. 21, 2017
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E. MMM. d, yyyy", Locale.US);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			// update text
			classDateValue.setText(simpleDateFormat.format(calendar.getTime()));
			sharedPreferences.edit().putLong(KEY_DATE, cal.getTimeInMillis()).apply();
		});
		timePublish.subscribe(cal -> {
			// set up time
			Calendar calendarHere = Calendar.getInstance(TimeZone.getDefault());
			calendarHere.setTime(calendar.getTime());
			String classTime = String.format(
					Locale.ENGLISH,
					"%02d:%02d CST - %02d:%02d local",
					cal.get(Calendar.HOUR_OF_DAY),
					cal.get(Calendar.MINUTE),
					calendarHere.get(Calendar.HOUR_OF_DAY),
					calendarHere.get(Calendar.MINUTE));
			classTimeValue.setText(classTime);
			sharedPreferences.edit().putLong(KEY_TIME, cal.getTimeInMillis()).apply();
		});
		// date
		classDateValue = rootView.findViewById(R.id.classDateValue);
		classDateValue.setOnClickListener(view -> {
			// date set listener
			DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, dayOfMonth) -> {
				// convert returned integer values into a calendar object to pass into the SimpleDateFormat object
				calendar.set(year, month, dayOfMonth);
				datePublish.onNext(calendar);
			};
			// date picker
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
				timePublish.onNext(calendar);
			};
			TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
			timePickerDialog.show();
		});
	}
}
