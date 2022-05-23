package com.smartapps.sigev2.models.schoolyear;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

import java.util.List;

@Dao
public abstract class SchoolYearDao  implements BaseDao<SchoolYear> {
    @Query("select * from SchoolYears")
    public abstract LiveData<List<SchoolYear>> getAll();
}
