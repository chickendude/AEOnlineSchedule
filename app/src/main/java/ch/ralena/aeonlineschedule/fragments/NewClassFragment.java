package ch.ralena.aeonlineschedule.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ch.ralena.aeonlineschedule.R;

/**
 * Fragment for creating a new class.
 */

public class NewClassFragment extends Fragment {
	TextView classDateValue;
	TextView classTimeValue;
	Calendar calendar;

	View.OnClickListener classTimeClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
					// if an erroneous time was chosen, just reset it to 0
					if (minute != 0 && minute != 30) {
						minute = 0;
					}
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);
					Calendar calendarHere = Calendar.getInstance(TimeZone.getDefault());
					calendarHere.setTime(calendar.getTime());

					String classTime = String.format("%d:%d/%d:%d", hourOfDay, minute, calendarHere.get(Calendar.HOUR_OF_DAY), calendarHere.get(Calendar.MINUTE));

					classTimeValue.setText(classTime);
				}
			};
			TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, 0, 0, false);
			timePickerDialog.show();
		}
	};

	View.OnClickListener classDateClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
					// convert returned integer values into a calendar object to pass into the SimpleDateFormat object
					calendar.set(year, month, dayOfMonth);
					// Sat. Oct. 21, 2017
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E. MMM. d, yyyy", Locale.US);
					// update text
					classDateValue.setText(simpleDateFormat.format(calendar.getTime()));
				}
			};
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
					onDateSetListener,
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
		}
	};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getActivity().setTitle("Add new Class");

		// make sure Calendar object is in CST.
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		calendar = Calendar.getInstance(timeZone);

		View view = inflater.inflate(R.layout.fragment_new_class, container, false);

		// set up click listeners
		TextView classDateLabel = view.findViewById(R.id.classDateLabel);
		classDateLabel.setOnClickListener(classDateClickListener);
		classDateValue = view.findViewById(R.id.classDateValue);
		classDateValue.setOnClickListener(classDateClickListener);

		TextView classTimeLabel = view.findViewById(R.id.classTimeLabel);
		classTimeLabel.setOnClickListener(classTimeClickListener);
		classTimeValue = view.findViewById(R.id.classTimeValue);
		classTimeValue.setOnClickListener(classTimeClickListener);

		return view;
	}
}
