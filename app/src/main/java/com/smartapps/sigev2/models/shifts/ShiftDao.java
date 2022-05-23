package com.smartapps.sigev2.models.shifts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class ShiftDao implements BaseDao<Shift> {
    @Query("select * from Shifts")
    public abstract LiveData<List<Shift>> getAll();

    @Query("delete from Shifts")
    public abstract void deleteAll();
}