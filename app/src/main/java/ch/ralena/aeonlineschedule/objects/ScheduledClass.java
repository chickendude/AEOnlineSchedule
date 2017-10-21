package ch.ralena.aeonlineschedule.objects;

import java.util.Date;

/**
 * ScheduledClass object.
 */
public class ScheduledClass {
	private Date date;
	private String notes;
	private String summary;
	private Student student;

	public ScheduledClass(Date date, String notes, Student student) {
		this.date = date;
		this.notes = notes;
		this.student = student;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
