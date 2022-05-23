package com.smartapps.sigev2.models.tutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync;
import com.smartapps.sigev2.models.tutorStudents.pojo.TutorStudentRelationshipData;

import java.util.List;

import io.reactivex.Maybe;

public class TutorRepository {
    private TutorDao tutorDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TutorRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        tutorDao = db.tutorDao();
    }

    public Maybe<TutorData> getTutorData(String tutorId, String studentId) {
        return tutorDao.getTutorData(tutorId, studentId);
    }

    public Maybe<TutorStudentRelationshipData> getTutorStudentRelationship(String tutorId, String studentId) {
        return tutorDao.getTutorStudentRelationship(tutorId, studentId);
    }

    public LiveData<List<TutorData>> getStudentTutors(String studentId) {
        return tutorDao.getStudentTutors(studentId);
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

    public Tutor getById(String id) {
        return tutorDao.getTutorById(id);
    }

    public TutorDataToSync getTutorDataToSync(String tutorId, String studentId) {
        return tutorDao.getTutorDataToSync(tutorId, studentId);
    }

    public Maybe<Tutor> getTutor(String id) {
        return tutorDao.getTutor(id);
    }
}
