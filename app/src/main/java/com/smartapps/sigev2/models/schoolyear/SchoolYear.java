package com.smartapps.sigev2.models.schoolyear;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "SchoolYears")
public class SchoolYear {
    @PrimaryKey
    @NonNull
    private int Id;
    private String Description;
    private Date StartDate;
    private Date FinishDate;
    private int SchoolYearStateId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getFinishDate() {
        return FinishDate;
    }

    public void setFinishDate(Date finishDate) {
        FinishDate = finishDate;
    }

    public int getSchoolYearStateId() {
        return SchoolYearStateId;
    }

    public void setSchoolYearStateId(int schoolYearStateId) {
        SchoolYearStateId = schoolYearStateId;
    }
}
