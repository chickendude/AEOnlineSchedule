package ch.ralena.aeonlineschedule;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ch.ralena.aeonlineschedule.fragments.MyClassesFragment;
import ch.ralena.aeonlineschedule.fragments.OptionsFragment;
import ch.ralena.aeonlineschedule.fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navigationView = findViewById(R.id.navigationView);

		// load proper fragment when menu item is pressed
		navigationView.setOnNavigationItemSelectedListener(item -> {
			Fragment fragment = null;
			// check which menu item was pressed and load fragment object
			switch (item.getItemId()) {
				case R.id.menu_schedule:
					fragment = new ScheduleFragment();
					break;
				case R.id.menu_my_classes:
					fragment = new MyClassesFragment();
					break;
				case R.id.menu_invoice:
					fragment = new ScheduleFragment();
					break;
				case R.id.menu_options:
					fragment = new OptionsFragment();
					break;
			}
			// if a proper press was registered, load that fragment
			if (fragment != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragmentContainer, fragment)
						.commit();
			}
			return true;
		}); // end of item selected listener
		navigationView.setSelectedItemId(R.id.menu_schedule);
	}
}
