package com.smartapps.sigev2.models.studentconditions;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class StudentConditionsDao implements BaseDao<StudentConditions> {

    @Query("select * from StudentConditions")
    public abstract LiveData<List<StudentConditions>> getAll();

    @Query("delete from StudentConditions")
    public abstract void deleteAll();
}
