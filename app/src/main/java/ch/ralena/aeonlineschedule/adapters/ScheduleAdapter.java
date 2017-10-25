package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;

/**
 * Adapter for the Schedule Fragment recycler view
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
	private List<ScheduledClass> scheduledClasses;

	public ScheduleAdapter(List<ScheduledClass> scheduledClasses) {
		this.scheduledClasses = scheduledClasses;
	}

	@Override
	public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
		return new ScheduleViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ScheduleViewHolder holder, int position) {
		ScheduledClass scheduledClass = scheduledClasses.get(position);
		holder.bindView(scheduledClass);
	}


	@Override
	public int getItemCount() {
		return scheduledClasses.size();
	}

	class ScheduleViewHolder extends RecyclerView.ViewHolder {
		private TextView dateText;
		private TextView dayOfWeekText;
		private TextView timeText;
		private TextView hoursLeftText;
		private TextView studentNameText;

		public ScheduleViewHolder(View itemView) {
			super(itemView);
			dateText = itemView.findViewById(R.id.dateText);
			dayOfWeekText = itemView.findViewById(R.id.dayOfWeekText);
			timeText = itemView.findViewById(R.id.timeText);
			hoursLeftText = itemView.findViewById(R.id.hoursLeft);
			studentNameText = itemView.findViewById(R.id.studentNameText);
		}

		public void bindView(ScheduledClass scheduledClass) {
			String studentName = scheduledClass.getStudent().getName();
			studentNameText.setText(studentName);

			// get strings from Date
			String dayOfWeek = new SimpleDateFormat("E", Locale.ENGLISH).format(scheduledClass.getDate());
			String dayOfMonth = new SimpleDateFormat("d", Locale.ENGLISH).format(scheduledClass.getDate());
			String time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(scheduledClass.getDate());

			// calculate the time left until class starts
			Long timeDifference = scheduledClass.getDate().getTime() - new Date().getTime();
			int minutes = (int) (timeDifference % 3600000) / 60000;
			int hours = (int) (timeDifference / 3600000);
			int days = 0;
			if (hours >= 24) {
				days = hours / 24;
				hours = hours % 24;
			}
			String hoursLeft = days > 0 ? String.format(Locale.ENGLISH, "%dd %dh", days, hours) : String.format(Locale.ENGLISH, "%dh %d m", hours, minutes);

			dayOfWeekText.setText(dayOfWeek);
			dateText.setText(dayOfMonth);
			timeText.setText(time);
			hoursLeftText.setText(hoursLeft);
		}
	}
}
