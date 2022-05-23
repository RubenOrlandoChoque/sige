package com.smartapps.sigev2.models.typeemployee;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TypeEmployees")
public class TypeEmployee {
    @PrimaryKey
    @NonNull
    private int Id;
    private String Description;

    @NonNull
    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
