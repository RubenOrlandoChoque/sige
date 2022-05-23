package com.smartapps.sigev2.models.employeejobtitles;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.smartapps.sigev2.models.teacher.Teacher;

@Entity(tableName = "EmployeeJobTitles", foreignKeys = {
        @ForeignKey(entity = Teacher.class,
                parentColumns = "Id",
                childColumns = "EmployeeId"
        )
}, indices = {@Index("EmployeeId")})
public class EmployeeJobTitle {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int Id;
    private String EmployeeId;
    private int JobTitleId;
    private int ShiftId;
    private int TypeEmployeeId;
    @Ignore
    private int Workload = 0;

    @NonNull
    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public int getJobTitleId() {
        return JobTitleId;
    }

    public void setJobTitleId(int jobTitleId) {
        JobTitleId = jobTitleId;
    }

    public int getShiftId() {
        return ShiftId;
    }

    public void setShiftId(int shiftId) {
        ShiftId = shiftId;
    }

    public int getTypeEmployeeId() {
        return TypeEmployeeId;
    }

    public void setTypeEmployeeId(int typeEmployeeId) {
        TypeEmployeeId = typeEmployeeId;
    }
}
