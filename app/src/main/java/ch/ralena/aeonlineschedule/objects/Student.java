package ch.ralena.aeonlineschedule.objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Student class.
 */
public class Student extends RealmObject {
	@PrimaryKey
	private String id;

	private String name;
	private String chineseName;
	private String notes;
	private String homeTown;
	private String currentTown;
	private ClassType defaultClassType;

	public String getId() {
		return id;
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

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	public String getCurrentTown() {
		return currentTown;
	}

	public void setCurrentTown(String currentlyTown) {
		this.currentTown = currentlyTown;
	}

	public ClassType getDefaultClassType() {
		return defaultClassType;
	}

	public void setDefaultClassType(ClassType defaultClassType) {
		this.defaultClassType = defaultClassType;
	}

	public boolean deleteStudent(Realm realm) {
		if (realm.where(ScheduledClass.class).equalTo("student.id", id).count() > 0)
			return false;
		else {
			realm.executeTransaction(r -> deleteFromRealm());
			return true;
		}
	}

	public void deleteStudentAndClasses(Realm realm) {
		realm.executeTransaction(r -> {
			realm.where(ScheduledClass.class).equalTo("student.id", id).findAll().deleteAllFromRealm();
			deleteFromRealm();
		});
	}
}
