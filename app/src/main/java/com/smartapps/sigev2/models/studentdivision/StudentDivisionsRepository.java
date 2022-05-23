package com.smartapps.sigev2.models.studentdivision;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.database.RoomDatabase;

import java.util.List;

public class StudentDivisionsRepository {
    private StudentDivisionsDao studentsDivisionDao;
    private LiveData<List<StudentDivisions>> all;

    public StudentDivisionsRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        studentsDivisionDao = db.studentDivisionsDao();
        all = studentsDivisionDao.getAll();

    }

    public void insert(StudentDivisions object) {
        studentsDivisionDao.insert(object);
    }

    public StudentDivisions get(String studentId, int divisionId) {
        return studentsDivisionDao.get(studentId, divisionId);
    }

    public void delete(StudentDivisions studentDivisions) {
        studentsDivisionDao.delete(studentDivisions);
    }

    public LiveData<List<StudentDivisions>> getAll() {
        return studentsDivisionDao.getAll();
    }

    public void disableAll(String studentId) {
        List<StudentDivisions> allByCourseYearId = studentsDivisionDao.getAllByCourseYearId(studentId, SharedConfig.getCurrentSchoolYearId());
        for(StudentDivisions studentDivisions: allByCourseYearId){
            studentDivisions.setCurrentDivision(false);
            insert(studentDivisions);
        }
    }

    public StudentDivisions getByStudentId(String studentId) {
        return studentsDivisionDao.getByStudentId(studentId);
    }
}
