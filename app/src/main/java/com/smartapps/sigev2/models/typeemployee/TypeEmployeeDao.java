package com.smartapps.sigev2.models.typeemployee;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class TypeEmployeeDao implements BaseDao<TypeEmployee> {

    @Query("select * from TypeEmployees")
    public abstract LiveData<List<TypeEmployee>> getAll();

    @Query("delete from TypeEmployees")
    public abstract void deleteAll();
}
