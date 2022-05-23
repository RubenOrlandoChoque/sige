package com.smartapps.sigev2.models.tutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabaseTemp;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutorStudents.pojo.TutorStudentRelationshipData;

import java.util.List;

import io.reactivex.Maybe;

public class TutorRepositoryTemp {
    private TutorDao tutorDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TutorRepositoryTemp(Application application) {
        RoomDatabaseTemp db = RoomDatabaseTemp.getDatabase(application);
        tutorDao = db.tutorDao();
    }

    public LiveData<List<TutorData>> getStudentTutors(String studentId) {
        return tutorDao.getStudentTutors(studentId);
    }

    public Maybe<TutorData> getTutorData(String tutorId, String studentId) {
        return tutorDao.getTutorData(tutorId, studentId);
    }

    public Maybe<TutorStudentRelationshipData> getTutorStudentRelationship(String tutorId, String studentId) {
        return tutorDao.getTutorStudentRelationship(tutorId, studentId);
    }

    public List<TutorData> getStudentTutorsList(String studentId) {
        return tutorDao.getStudentTutorsList(studentId);
    }

    public void insert(Tutor tutor) {
        tutorDao.insert(tutor);
    }

    public void update(Tutor tutor) {
        tutorDao.update(tutor);
    }

    public Tutor getTutorById(String id) {
        return tutorDao.getTutorById(id);
    }

    public void deleteAll() {
        tutorDao.deleteAll();
    }

    public List<Tutor> getAll() {
        return tutorDao.getAll();
    }
}
