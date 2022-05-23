package com.smartapps.sigev2.models.relationship;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Relationships")
public class Relationship {
    @PrimaryKey
    @NonNull
    private int Id;
    private String Description;
    private boolean IsActive;

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

    public boolean isIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean active) {
        IsActive = active;
    }
}
