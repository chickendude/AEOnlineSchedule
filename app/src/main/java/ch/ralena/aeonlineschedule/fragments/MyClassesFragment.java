package ch.ralena.aeonlineschedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.ralena.aeonlineschedule.R;
import ch.ralena.aeonlineschedule.adapters.MyClassesAdapter;
import ch.ralena.aeonlineschedule.objects.ScheduledClass;
import io.realm.Realm;
import io.realm.Sort;

public class MyClassesFragment extends Fragment {
	Realm realm;
	List<ScheduledClass> scheduledClasses;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();
		scheduledClasses = realm.where(ScheduledClass.class).findAllSorted("date", Sort.DESCENDING);

		View view = inflater.inflate(R.layout.fragment_my_classes, container, false);

		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		MyClassesAdapter adapter = new MyClassesAdapter(scheduledClasses);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle(String.format("My Classes"));
	}
}
