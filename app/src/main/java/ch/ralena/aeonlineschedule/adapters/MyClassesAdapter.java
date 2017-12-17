package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;

public class MyClassesAdapter extends AEAdapter<ScheduledClass> {

	public MyClassesAdapter(List<ScheduledClass> scheduledClasses) {
		super(scheduledClasses);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_classes, parent, false);
		return new AEViewHolder(view) {
			TextView monthName;
			TextView studentName;
			TextView time;
			TextView date;
			TextView dayOfWeek;

			@Override
			void loadViews() {
				monthName = itemView.findViewById(R.id.monthNameText);
				monthName.setVisibility(View.GONE);
				studentName = itemView.findViewById(R.id.studentNameText);
				time = itemView.findViewById(R.id.timeText);
				date = itemView.findViewById(R.id.dateText);
				dayOfWeek = itemView.findViewById(R.id.dayOfWeekText);
			}

			@Override
			void bindView(ScheduledClass scheduledClass) {
				int position = objectsList.indexOf(scheduledClass);
				if (position == 0) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(scheduledClass.getDate());
					monthName.setText(scheduledClass.getMonth());
					monthName.setVisibility(View.VISIBLE);
				}
				if (position > 0) {
					Calendar prevCalendar = Calendar.getInstance();
					prevCalendar.setTime(objectsList.get(position - 1).getDate());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(scheduledClass.getDate());
					if (calendar.get(Calendar.MONTH) != prevCalendar.get(Calendar.MONTH)) {
						monthName.setText(scheduledClass.getMonth());
						monthName.setVisibility(View.VISIBLE);
					}
				}
				studentName.setText(scheduledClass.getStudent().getName());
				date.setText(scheduledClass.getDayOfMonth());
				dayOfWeek.setText(scheduledClass.getDayOfWeek());
				time.setText(scheduledClass.getTime());
			}
		};
	}
}
