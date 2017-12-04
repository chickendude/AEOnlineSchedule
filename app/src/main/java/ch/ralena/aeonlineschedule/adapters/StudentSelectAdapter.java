package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.Student;
import io.reactivex.subjects.PublishSubject;

public class StudentSelectAdapter extends AEAdapter<Student> {
	PublishSubject<Student> studentObservable = PublishSubject.create();
	PublishSubject<View> buttonObservable = PublishSubject.create();

	public PublishSubject<Student> asObservableStudent() {
		return studentObservable;
	}

	public PublishSubject<View> asObservableButton() {
		return buttonObservable;
	}

	public StudentSelectAdapter(List<Student> studentList) {
		super(studentList);
	}

	public void updateStudents(List<Student> studentList) {
		objectsList = studentList;
		notifyDataSetChanged();
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
			// students
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
					view.setOnClickListener(v -> studentObservable.onNext(student));
				}
			};
		} else {
			// create student button
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_end, parent, false);
			return new AEViewHolderBottom(view) {
				TextView studentName;

				@Override
				void loadViews() {
					view.setOnClickListener(v -> buttonObservable.onNext(v));
					studentName = view.findViewById(R.id.studentNameText);
					studentName.setText("Create New Student");
				}
			};
		}
	}
}