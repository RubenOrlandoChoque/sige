package com.smartapps.sigev2.models.relationship;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;

import java.util.List;

@Dao
public abstract class RelationshipDao implements BaseDao<Relationship> {

    @Query("select Id,Description from Relationships")
    abstract LiveData<List<RelationshipData>> getAll();

    @Query("delete from Relationships")
    public abstract void deleteAll();
}
