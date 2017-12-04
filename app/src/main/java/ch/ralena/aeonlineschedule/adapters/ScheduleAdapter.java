package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Adapter for the Schedule Fragment recycler view
 */
public class ScheduleAdapter extends AEAdapter<ScheduledClass> {
	PublishSubject<String> classPublishSubject = PublishSubject.create();

	public Observable<String> asObservable() {
		return classPublishSubject;
	}

	public ScheduleAdapter(List<ScheduledClass> scheduledClasses) {
		super(scheduledClasses);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
		return new AEViewHolder(view) {
			private TextView dateText;
			private TextView dayOfWeekText;
			private TextView timeText;
			private TextView hoursLeftText;
			private TextView studentNameText;

			@Override
			void loadViews() {
				dateText = itemView.findViewById(R.id.dateText);
				dayOfWeekText = itemView.findViewById(R.id.dayOfWeekText);
				timeText = itemView.findViewById(R.id.timeText);
				hoursLeftText = itemView.findViewById(R.id.hoursLeft);
				studentNameText = itemView.findViewById(R.id.studentNameText);
			}

			@Override
			void bindView(ScheduledClass scheduledClass) {
				// pass item clicks back to the ScheduleFragment
				itemView.setOnClickListener(view -> classPublishSubject.onNext(scheduledClass.getId()));
				String studentName = scheduledClass.getStudent().getName();
				studentNameText.setText(studentName);

				// get strings from Date
				String dayOfWeek = scheduledClass.getDayOfWeek();
				String dayOfMonth = scheduledClass.getDayOfMonth();
				String time = scheduledClass.getTime();

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
		};
	}
}
