package com.smartapps.sigev2.models.tutorStudents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;


@Dao
public abstract class TutorStudentsDao implements BaseDao<TutorStudents> {

    @Query("select * from TutorStudents")
    public abstract LiveData<List<TutorStudents>> getAll();

    @Query("delete from TutorStudents")
    public abstract void deleteAll();

    @Query("update TutorStudents set CurrentTutor = 0 where StudentId = :studentId")
    public abstract void disableAll(String studentId);

    @Query("select * from TutorStudents where StudentId = :studentId and TutorId = :tutorId")
    public abstract List<TutorStudents> getByTutorAndStudent(String studentId, String tutorId);

    @Query("select * from TutorStudents")
    public abstract List<TutorStudents> getAllList();
}
