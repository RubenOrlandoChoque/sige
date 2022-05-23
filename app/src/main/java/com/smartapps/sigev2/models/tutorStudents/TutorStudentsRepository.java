package com.smartapps.sigev2.models.tutorStudents;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;

import java.util.List;

public class TutorStudentsRepository {
    private TutorStudentsDao tutorStudentsDao;
    private LiveData<List<TutorStudents>> all;

    public TutorStudentsRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        tutorStudentsDao = db.tutorStudentsDao();
        all = tutorStudentsDao.getAll();

    }

    public void insert(TutorStudents person) {
        tutorStudentsDao.insert(person);
    }

    public LiveData<List<TutorStudents>> getAll() {
        return tutorStudentsDao.getAll();
    }

    public void disableAll(String studentId) {
        tutorStudentsDao.disableAll(studentId);
    }

    public List<TutorStudents> getByTutorAndStudent(String studentId, String tutorId) {
        return tutorStudentsDao.getByTutorAndStudent(studentId, tutorId);
    }
}
