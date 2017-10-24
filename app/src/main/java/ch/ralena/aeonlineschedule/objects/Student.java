package ch.ralena.aeonlineschedule.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Student class.
 */
public class Student extends RealmObject {
	@PrimaryKey
	private String id;

	private String name;
	private String notes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
