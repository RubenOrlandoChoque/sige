package com.smartapps.sigev2.models.tutorStudents;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabaseTemp;

import java.util.List;

public class TutorStudentsRepositoryTemp {
    private TutorStudentsDao tutorStudentsDao;
    private LiveData<List<TutorStudents>> all;

    public TutorStudentsRepositoryTemp(Application application) {
        RoomDatabaseTemp db = RoomDatabaseTemp.getDatabase(application);
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

    public void deleteAll() {
        tutorStudentsDao.deleteAll();
    }

    public List<TutorStudents> getAllList() {
        return tutorStudentsDao.getAllList();
    }
}
