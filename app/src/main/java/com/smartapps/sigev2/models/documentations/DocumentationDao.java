package com.smartapps.sigev2.models.documentations;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;


@Dao
public abstract class DocumentationDao implements BaseDao<Documentation> {

    @Query("select * from Documentations")
    public abstract LiveData<List<Documentation>> getAll();

    @Query("delete from Documentations")
    public abstract void deleteAll();
}
