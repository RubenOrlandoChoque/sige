package com.smartapps.sigev2.models.educationlevels;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.educationlevels.pojo.EducationLevelData;

import java.util.List;


@Dao
public abstract class EducationLevelsDao implements BaseDao<EducationLevels> {

    @Query("select Id, Description from EducationLevels")
    public abstract LiveData<List<EducationLevelData>> getAll();

    @Query("delete from EducationLevels")
    public abstract void deleteAll();
}
