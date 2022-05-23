package com.smartapps.sigev2.models.persongenders;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;


@Dao
public abstract class PersonGendersDao implements BaseDao<PersonGenders> {

    @Query("select * from PersonGenders")
    public abstract LiveData<List<PersonGenders>> getAll();

    @Query("delete from PersonGenders")
    public abstract void deleteAll();
}
