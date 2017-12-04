package ch.ralena.aeonlineschedule.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.Student;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

public class StudentSelectAdapter extends AEAdapter<Student> {
	PublishSubject<Student> studentObservable = PublishSubject.create();
	PublishSubject<Student> editObservable = PublishSubject.create();
	PublishSubject<View> buttonObservable = PublishSubject.create();

	public PublishSubject<Student> asObservableStudent() {
		return studentObservable;
	}

	public PublishSubject<Student> asObservableEdit() {
		return editObservable;
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
		if (viewType == TYPE_NORMAL) {
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
					view.setOnLongClickListener(v -> {
						PopupMenu popup = new PopupMenu(view.getContext(), view);
						popup.getMenuInflater().inflate(R.menu.student, popup.getMenu());
						popup.setOnMenuItemClickListener(menuItem -> {
							switch (menuItem.getItemId()) {
								case R.id.edit:
									editObservable.onNext(student);
									break;
								case R.id.delete:
									if (student.deleteStudent(Realm.getDefaultInstance())) {
										notifyDataSetChanged();
									}else{
										Snackbar snackbar = Snackbar.make(view, "This student still has classes associated to him/her. Delete all classes?", Snackbar.LENGTH_INDEFINITE);
										snackbar.setAction("Delete", view1 -> {
											student.deleteStudentAndClasses(Realm.getDefaultInstance());
											notifyDataSetChanged();
										});
										snackbar.show();
									}
									break;
							}
							return true;
						});
						popup.show();
						return true;
					});
				}
			};
		} else {
			// create student button
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_end, parent, false);
			return new AEViewHolderBottom(view) {
				Button button;
				@Override
				void loadViews() {
					button = itemView.findViewById(R.id.addStudentButton);
					button.setOnClickListener(v -> buttonObservable.onNext(v));
				}
			};
		}
	}
}