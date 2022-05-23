package com.smartapps.sigev2.models.teacher;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.teacher.pojo.TeacherData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherListData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherSync;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TeacherDao extends BaseDao<Teacher> {
    @Query("SELECT p.FirstName, p.LastName,p.DocumentNumber,p.Birthdate,p.Id from Staff as t " +
            "inner join People as p on p.Id = t.Id inner join EmployeeJobTitles as ejt on ejt.EmployeeId = t.Id where ejt.ShiftId =:shiftId")
    LiveData<List<TeacherListData>> getTeacherList(int shiftId);


    @Query("SELECT p.*, t.CUIL, t.HireDate, ejt.TypeEmployeeId from Staff as t " +
            "inner join people as p on p.Id = t.Id " +
            "inner join EmployeeJobTitles as ejt on ejt.EmployeeId = t.Id and ejt.ShiftId =:shiftId " +
            "where t.Id = :id limit 1")
    Maybe<TeacherData> getTeacher(String id, int shiftId);

    @Query("SELECT * from Staff where Id = :id")
    Teacher getTeacherById(String id);

//    @Query("select p.*, t.HireDate,t.CUIL, ejt.TypeEmployeeId, ejt.ShiftId, ejt.EmployeeId, ejt.JobTitleId from Staff as t " +
//            "inner join people as p on p.id = t.id " +
//            "inner join EmployeeJobTitles as ejt on ejt.EmployeeId = t.Id " +
//            "where t.Sync = 0")
//    List<TeacherDataSync> getDataToSync();

    @Query("select * from Staff as s inner join people as p on p.Id = s.Id")
    List<TeacherSync> getDataToSync();
}
