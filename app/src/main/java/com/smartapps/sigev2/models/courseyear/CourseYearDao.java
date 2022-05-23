package com.smartapps.sigev2.models.courseyear;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearAndAllDivisions;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearStudentsCount;
import com.smartapps.sigev2.models.students.pojo.StudentCountData;

import java.util.List;

import io.reactivex.Single;


@Dao
public abstract class CourseYearDao implements BaseDao<CourseYear> {
    @Query("select * from CourseYears")
    public abstract LiveData<List<CourseYear>> getAll();

    @Query("delete from CourseYears")
    public abstract void deleteAll();

    @Query("SELECT cy.CourseYearName, d.DivisionName, count(sd.DivisionId) FROM StudentDivisions AS sd " +
            "INNER JOIN divisions AS d ON d.id = sd.DivisionId " +
            "INNER JOIN CourseYears AS cy ON cy.Id = d.CourseYearId " +
            "WHERE d.ShiftId = :shiftId AND d.SchoolYearId = :schoolYearId AND d.CourseYearId = :courseYearId " +
            "GROUP BY cy.CourseYearName, d.DivisionName " +
            "ORDER BY cy.CourseYearName, d.DivisionName")
    public abstract LiveData<List<StudentCountData>> GetAllCountStudents(int courseYearId, int shiftId, int schoolYearId);

    @Query("select count(sd.StudentId) as Cant,cy.Id as CourseYearId, CourseYearName " +
            "from CourseYears as cy " +
            "left join Divisions as d on d.CourseYearId = cy.ID " +
            "left join StudentDivisions as sd on sd.DivisionId = d.Id and sd.currentDivision = 1 where d.ShiftId = :shiftId " +
            "and d.SchoolYearId = :schoolYearId " +
            "group by cy.Id,cy.CourseYearName")
    public abstract LiveData<List<CourseYearStudentsCount>> getAllWithStudentsCount(int shiftId, int schoolYearId);

    @Transaction
    @Query("SELECT d.Id as DivisionId, d.DivisionName, d.CourseYearId, cy.CourseYearName from Divisions as d " +
            "inner join CourseYears as cy on cy.Id = d.CourseYearId and d.ShiftId = :shiftId and d.SchoolYearId = :schoolYearId")
    public abstract LiveData<List<CourseYearAndAllDivisions>> getCourseYearsDivisionsByShiftId(int shiftId, int schoolYearId);

    @Query("select * from CourseYears")
    public abstract Single<List<CourseYear>> getAllSingle();
}
