package ch.ralena.aeonlineschedule.objects;

/**
 * Student class.
 */
public class Student {
	private String name;
	private String notes;

	public Student(String name) {
		this.name = name;
	}

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
