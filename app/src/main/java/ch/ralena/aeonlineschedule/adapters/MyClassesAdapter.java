package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;

public class MyClassesAdapter extends AEAdapter<ScheduledClass> {

	List<ScheduledClass> scheduledClasses;

	public MyClassesAdapter(List<ScheduledClass> scheduledClasses) {
		super(scheduledClasses);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_classes, parent, false);
		return new AEViewHolder(view) {
			TextView studentName;
			TextView time;
			TextView date;
			TextView dayOfWeek;

			@Override
			void loadViews() {
				studentName = itemView.findViewById(R.id.studentNameText);
				time = itemView.findViewById(R.id.timeText);
				date = itemView.findViewById(R.id.dateText);
				dayOfWeek = itemView.findViewById(R.id.dayOfWeekText);
			}

			@Override
			void bindView(ScheduledClass scheduledClass) {
				studentName.setText(scheduledClass.getStudent().getName());
				date.setText(scheduledClass.getDayOfMonth());
				dayOfWeek.setText(scheduledClass.getDayOfWeek());
				time.setText(scheduledClass.getTime());
			}
		};
	}
}
