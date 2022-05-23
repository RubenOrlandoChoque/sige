package com.smartapps.sigev2.models.employeejobtitles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class EmployeeJobTitleDao implements BaseDao<EmployeeJobTitle> {
    @Query("select * from EmployeeJobTitles where Id=:id")
    abstract EmployeeJobTitle getById(int id);

    @Query("select * from EmployeeJobTitles where EmployeeId=:employeeId and ShiftId=:shiftId")
    abstract EmployeeJobTitle get(String employeeId, int shiftId);

    @Insert
    abstract void insertAll(List<EmployeeJobTitle> persons);

    @Query("select * from EmployeeJobTitles")
    abstract LiveData<List<EmployeeJobTitle>> getAll();
}