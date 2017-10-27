package ch.ralena.aeonlineschedule.objects;

import io.realm.RealmObject;

/**
 * Contains information about what type of class it is, eg. the name, wage, length, etc.
 */

public class ClassType extends RealmObject {

	private String name;
	private float wage;
	private int numMinutes;
	private boolean isEnabled;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWage() {
		return wage;
	}

	public void setWage(float wage) {
		this.wage = wage;
	}

	public int getNumMinutes() {
		return numMinutes;
	}

	public void setNumMinutes(int numMinutes) {
		this.numMinutes = numMinutes;
	}


}
