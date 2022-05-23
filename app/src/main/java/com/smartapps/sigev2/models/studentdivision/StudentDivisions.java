package com.smartapps.sigev2.models.studentdivision;


import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "StudentDivisions", primaryKeys =
        {"StudentId", "DivisionId"})
public class StudentDivisions {
    @NonNull
    private String StudentId;
    @NonNull
    private int DivisionId;
    private boolean currentDivision;

    @NonNull
    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(@NonNull String studentId) {
        StudentId = studentId;
    }

    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int divisionId) {
        DivisionId = divisionId;
    }

    public boolean isCurrentDivision() {
        return currentDivision;
    }

    public void setCurrentDivision(boolean currentDivision) {
        this.currentDivision = currentDivision;
    }
}
