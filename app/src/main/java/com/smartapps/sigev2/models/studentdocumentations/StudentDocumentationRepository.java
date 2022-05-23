package com.smartapps.sigev2.models.studentdocumentations;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;

import java.util.List;
import io.reactivex.Maybe;

public class StudentDocumentationRepository {
    private StudentDocumentationDao studentDocumentationDao;
    private LiveData<List<StudentDocumentation>> all;

    public StudentDocumentationRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        studentDocumentationDao = db.studentDocumentationDao();
        all = studentDocumentationDao.getAll();

    }

    public void insert(StudentDocumentation object) {
        studentDocumentationDao.insert(object);
    }

    public StudentDocumentation get(String studentId) {
        return studentDocumentationDao.get(studentId);
    }

    public void delete(StudentDocumentation studentDocumentation) {
        studentDocumentationDao.delete(studentDocumentation);
    }

    public LiveData<List<StudentDocumentation>> getAll() {
        return studentDocumentationDao.getAll();
    }

    public Maybe<List<Integer>> getDocumentations(String id) {
        return studentDocumentationDao.getDocumentations(id);
    }

    public List<StudentDocumentation> getDocumentationsList(String id) {
        return studentDocumentationDao.getDocumentationsList(id);
    }

    public void deleteAll(String studentId) {
        studentDocumentationDao.deleteAll(studentId);
    }

    public List<Integer> getStudentDocumentationsToSync(String studentId) {
        return studentDocumentationDao.getStudentDocumentationsToSync(studentId);
    }
}
