package com.smartapps.sigev2.models.studentdivision;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class StudentDivisionsDao implements BaseDao<StudentDivisions> {

    @Query("select * from StudentDivisions")
    abstract LiveData<List<StudentDivisions>> getAll();

    @Query("delete from StudentDivisions")
    abstract void deleteAll();

    @Query("select * from StudentDivisions where StudentId = :studentId and DivisionId=:divisionId")
    abstract StudentDivisions get(String studentId, int divisionId);

    @Query("update StudentDivisions set currentDivision=0 where StudentId = :studentId")
    public abstract void disableAll(String studentId);

    @Query("select sd.* from StudentDivisions sd " +
            "inner join Divisions d on sd.DivisionId = d.Id " +
            "where d.SchoolYearId = :schoolYearId and sd.StudentId = :studentId")
    abstract List<StudentDivisions> getAllByCourseYearId(String studentId, int schoolYearId);

    @Query("select * from StudentDivisions where StudentId = :studentId and currentDivision=1 limit 1")
    abstract StudentDivisions getByStudentId(String studentId);

    @Query("select * from StudentDivisions")
    abstract List<StudentDivisions> getAllList();
}
