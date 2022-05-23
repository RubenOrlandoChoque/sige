package com.smartapps.sigev2.models.division;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;


@Dao
public abstract class DivisionDao implements BaseDao<Division> {

    @Query("select * from Divisions")
    public abstract LiveData<List<Division>> getAll();

    @Query("delete from Divisions")
    public abstract void deleteAll();
}
