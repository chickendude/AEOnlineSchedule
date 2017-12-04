package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.Student;

public class StudentSelectAdapter extends AEAdapter<Student> {
	public StudentSelectAdapter(List<Student> studentList) {
		super(studentList);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
	}

	@Override
	public int getItemCount() {
		return objectsList.size() + 1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == TYPE_NORMAL) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
			return new AEViewHolder(view) {
				TextView studentName;

				@Override
				void loadViews() {
					studentName = view.findViewById(R.id.studentNameText);
				}

				@Override
				void bindView(Student student) {
					studentName.setText(student.getName());
				}
			};
		} else {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_end, parent, false);
			return new AEViewHolderBottom(view) {
				TextView studentName;

				@Override
				void loadViews() {
					studentName = view.findViewById(R.id.studentNameText);
					studentName.setText("Create New Student");
				}
			};
		}
	}
}
