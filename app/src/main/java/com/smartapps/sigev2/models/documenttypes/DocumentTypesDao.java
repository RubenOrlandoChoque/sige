package com.smartapps.sigev2.models.documenttypes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class DocumentTypesDao implements BaseDao<DocumentTypes> {

    @Query("select * from DocumentTypes")
    public abstract LiveData<List<DocumentTypes>> getAll();

    @Query("delete from DocumentTypes")
    public abstract void deleteAll();
}
