package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
		private TextView studentNameText;

		public ScheduleViewHolder(View itemView) {
			super(itemView);
			dateText = itemView.findViewById(R.id.dateText);
			studentNameText = itemView.findViewById(R.id.studentNameText);
		}

		public void bindView(ScheduledClass scheduledClass) {
			String studentName = scheduledClass.getStudent().getName();
			studentNameText.setText(studentName);
		}
	}
}
