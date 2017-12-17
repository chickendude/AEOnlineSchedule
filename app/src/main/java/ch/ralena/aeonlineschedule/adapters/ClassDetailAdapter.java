package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.ClassViewHolder> {
	private List<ScheduledClass> classes;

	public ClassDetailAdapter(List<ScheduledClass> classes) {
		this.classes = classes;
	}

	@Override
	public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_detail, parent, false);
		return new ClassViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ClassViewHolder holder, int position) {
		holder.bindView(classes.get(position));
	}

	@Override
	public int getItemCount() {
		return classes.size();
	}

	class ClassViewHolder extends RecyclerView.ViewHolder {
		TextView monthDate;
		TextView classTime;

		ClassViewHolder(View itemView) {
			super(itemView);
			monthDate = itemView.findViewById(R.id.monthDateValue);
			classTime = itemView.findViewById(R.id.classTimeValue);
		}

		void bindView(ScheduledClass scheduledClass) {
			String monthAndDate = String.format("%s. %s", scheduledClass.getMonth(), scheduledClass.getDayOfMonth());
			monthDate.setText(monthAndDate);
			classTime.setText(scheduledClass.getTime());
		}
	}

}
