package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;

public class MyClassesAdapter extends RecyclerView.Adapter<MyClassesAdapter.MyClassesViewHolder> {

	List<ScheduledClass> scheduledClasses;

	public MyClassesAdapter(List<ScheduledClass> scheduledClasses) {
		this.scheduledClasses = scheduledClasses;
	}

	@Override
	public MyClassesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_classes, parent, false);
		return new MyClassesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyClassesViewHolder holder, int position) {
		holder.bindView(scheduledClasses.get(position));
	}

	@Override
	public int getItemCount() {
		return scheduledClasses.size();
	}

	class MyClassesViewHolder extends RecyclerView.ViewHolder {
		TextView studentName;
		TextView time;
		TextView date;
		TextView dayOfWeek;

		public MyClassesViewHolder(View itemView) {
			super(itemView);
			studentName = itemView.findViewById(R.id.studentNameText);
			time = itemView.findViewById(R.id.timeText);
			date = itemView.findViewById(R.id.dateText);
			dayOfWeek = itemView.findViewById(R.id.dayOfWeekText);
		}

		public void bindView(ScheduledClass scheduledClass) {
			studentName.setText(scheduledClass.getStudent().getName());
			date.setText(scheduledClass.getDayOfMonth());
			dayOfWeek.setText(scheduledClass.getDayOfWeek());
			time.setText(scheduledClass.getTime());
		}
	}
}
