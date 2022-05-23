package com.smartapps.sigev2.models.teacher.pojo;

import com.smartapps.sigev2.models.people.Person;

import java.util.Date;

public class TeacherData extends Person {
    private Date HireDate;
    private String CUIL;
    private String Ocupation;
    private int TypeEmployeeId;

    public String getOcupation() {
        return Ocupation;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }

    public int getTypeEmployeeId() {
        return TypeEmployeeId;
    }

    public void setTypeEmployeeId(int typeEmployeeId) {
        TypeEmployeeId = typeEmployeeId;
    }

    public Date getHireDate() {
        return HireDate;
    }

    public void setHireDate(Date hireDate) {
        HireDate = hireDate;
    }

    public String getCUIL() {
        return CUIL;
    }

    public void setCUIL(String CUIL) {
        this.CUIL = CUIL;
    }
}
