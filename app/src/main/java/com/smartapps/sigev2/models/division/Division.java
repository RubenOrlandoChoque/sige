package com.smartapps.sigev2.models.division;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Divisions")
public class Division {
    @PrimaryKey
    @NonNull
    private int Id;
    private String DivisionName;
    private int CourseYearId;
    private int SchoolYearId;
    private int ShiftId;

    public Division() {

    }

    public int getShiftId() {
        return ShiftId;
    }

    public void setShiftId(int shiftId) {
        ShiftId = shiftId;
    }

    @NonNull
    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String divisionName) {
        DivisionName = divisionName;
    }

    public int getCourseYearId() {
        return CourseYearId;
    }

    public void setCourseYearId(int courseYaerId) {
        CourseYearId = courseYaerId;
    }

    public int getSchoolYearId() {
        return SchoolYearId;
    }

    public void setSchoolYearId(int schoolYaerId) {
        SchoolYearId = schoolYaerId;
    }
}
