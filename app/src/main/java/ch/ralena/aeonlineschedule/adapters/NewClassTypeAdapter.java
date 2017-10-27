package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ClassType;

/**
 * Recycler View adapter for new class types.
 */
// TODO: 10/27/2017 Add extra row at the bottom for a "new class type"
public class NewClassTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private List<ClassType> classTypes;

	public NewClassTypeAdapter(List<ClassType> classTypes) {
		this.classTypes = classTypes;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType==0) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_class_type, parent, false);
			return new NCTViewHolder(view);
		} else {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_class_type_end_button, parent, false);
			return new RecyclerView.ViewHolder(view) {};
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position < classTypes.size()) {
			ClassType classType = classTypes.get(position);
			((NCTViewHolder) holder).bindView(classType);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position < classTypes.size()) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getItemCount() {
		return classTypes.size() + 1;
	}

	class NCTViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView abbreviation;
		TextView rateValue;
		TextView lengthValue;

		NCTViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			rateValue = itemView.findViewById(R.id.rateValue);
			lengthValue = itemView.findViewById(R.id.lengthValue);
		}

		void bindView(ClassType classType) {
			name.setText(classType.getName());
			rateValue.setText(String.format(Locale.US, "$%.2f", classType.getWage()));
			lengthValue.setText(String.format(Locale.US, "%d minutes", classType.getNumMinutes()));
		}
	}
}