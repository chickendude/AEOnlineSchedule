package ch.ralena.aeonlineschedule.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * ScheduledClass object.
 */
public class ScheduledClass extends RealmObject {
	@PrimaryKey
	private String id;

	private Date date;
	private String notes;
	private String summary;
	private Student student;
	private ClassType classType;
	private boolean isCompleted;

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClassType getClassType() {
		return classType;
	}

	public void setClassType(ClassType classType) {
		this.classType = classType;
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

	private String formatDate(String pattern) {
		return new SimpleDateFormat(pattern, Locale.US).format(date);
	}

	public String getDayOfMonth() {
		return formatDate("d");
	}

	public String getDayOfWeek() {
		return formatDate("E");
	}

	public String getTime() {
		return formatDate("hh:mm a");
	}

	public String getMonth() {
		return formatDate("MMMM");

	}
}
