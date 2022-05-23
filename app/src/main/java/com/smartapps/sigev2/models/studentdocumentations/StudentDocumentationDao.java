package com.smartapps.sigev2.models.studentdocumentations;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;
import io.reactivex.Maybe;

@Dao
public abstract class StudentDocumentationDao implements BaseDao<StudentDocumentation> {

    @Query("select * from StudentDocumentations")
    abstract LiveData<List<StudentDocumentation>> getAll();

    @Query("delete from StudentDocumentations")
    abstract void deleteAll();

    @Query("select * from StudentDocumentations where StudentId = :studentId")
    abstract StudentDocumentation get(String studentId);

    @Query("SELECT DocumentationId from StudentDocumentations where StudentId=:studentId")
    abstract Maybe<List<Integer>> getDocumentations(String studentId);

    @Query("SELECT * from StudentDocumentations where StudentId=:studentId")
    abstract List<StudentDocumentation> getDocumentationsList(String studentId);

    @Query("delete from StudentDocumentations where StudentId=:studentId")
    abstract void deleteAll(String studentId);

    @Query("SELECT DocumentationId from StudentDocumentations where StudentId=:studentId")
    abstract List<Integer> getStudentDocumentationsToSync(String studentId);

    @Query("select * from StudentDocumentations")
    abstract List<StudentDocumentation> getAllList();
}
