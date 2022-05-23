package com.smartapps.sigev2.models.courseyear;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CourseYears")
public class CourseYear {
    @PrimaryKey
    @NonNull
    private int Id;
    private String CourseYearName;

    public String getCourseYearName() {
        return CourseYearName;
    }

    public void setCourseYearName(String courseYearName) {
        CourseYearName = courseYearName;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(@NonNull int Id) {
        this.Id = Id;
    }
}
