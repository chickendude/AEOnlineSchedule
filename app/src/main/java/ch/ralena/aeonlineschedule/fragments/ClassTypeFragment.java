package ch.ralena.aeonlineschedule.fragments;

import android.app.AlertDialog;
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
import ch.ralena.aeonlineschedule.adapters.NewClassTypeAdapter;
import ch.ralena.aeonlineschedule.objects.ClassType;
import io.realm.Realm;

/**
 * Fragment for viewing and editing/creating class types.
 */

public class ClassTypeFragment extends Fragment {
	RecyclerView classTypeRecyclerView;
	NewClassTypeAdapter adapter;
	Realm realm;
	List<ClassType> classTypes;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		realm = Realm.getDefaultInstance();
		View view = inflater.inflate(R.layout.fragment_class_type, container, false);

		classTypeRecyclerView = view.findViewById(R.id.classTypeRecyclerView);

		setUpNewClassRecyclerView();

		return view;
	}

	/**
	 * Sets up the recycler view, loads the class types, and checks whether to load the default class types.
	 */
	private void setUpNewClassRecyclerView() {
		classTypes = realm.where(ClassType.class).findAll().sort("name");
		if (classTypes.size() == 0) {
			askToLoadDefaultClassTypes();
		}
		// create adapter and set layout manager
		adapter = new NewClassTypeAdapter(classTypes);
		classTypeRecyclerView.setAdapter(adapter);
		classTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
	}


	/**
	 * Loads an Alert Dialog box asking user if they want to load the default class types.
	 * If yes, loads default class types, if no, does nothing.
	 */
	private void askToLoadDefaultClassTypes() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("No Class Types found!");
		builder.setMessage("Would you like to load the default class types?");
		builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
			loadDefaultClassTypes();
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	/**
	 * Creates the default class types and saves them in the realm db.
	 */
	private void loadDefaultClassTypes() {
		String[] classType40Names = {"GES", "GE", "BE", "IELTS", "TOEFL", "SAT", "ACT", "EENG",
				"MKTADV", "FIN", "HR", "IT", "Demo"};
		String[] classType45Names = {"Group"};
		realm.executeTransaction(realm -> {
			// classes that take 40 minutes
			for (String classTypeName : classType40Names) {
				ClassType classType = realm.createObject(ClassType.class);
				classType.setName(classTypeName);
				classType.setNumMinutes(40);
				classType.setWage(15f);        // todo: Load default wage value from options
			}
			// classes that take 45 minutes
			for (String classTypeName : classType45Names) {
				ClassType classType = realm.createObject(ClassType.class);
				classType.setName(classTypeName);
				classType.setNumMinutes(40);
				classType.setWage(15f);        // todo: Load default wage value from options
			}
		});
		adapter.notifyDataSetChanged();
	}
}
