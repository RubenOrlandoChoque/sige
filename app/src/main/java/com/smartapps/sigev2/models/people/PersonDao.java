package com.smartapps.sigev2.models.people;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class PersonDao implements BaseDao<Person> {
    @Query("select * from People where DocumentNumber=:documentNumber")
    abstract Person getByDni(String documentNumber);

    @Query("select * from People where Id=:id")
    abstract Person getById(String id);

    @Insert
    abstract void insertAll(List<Person> persons);

    @Query("select * from People")
    abstract LiveData<List<Person>> getAll();

    @Query("delete from People")
    abstract void deleteAll();

    @Query("delete from People where Id not in (:keepData)")
    abstract void deleteAllExcept(List<String> keepData);

    @Query("delete from People where Id =:id")
    public abstract void delete(String id);
}