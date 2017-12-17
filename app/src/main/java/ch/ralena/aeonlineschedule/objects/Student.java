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
	private String notes;

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
