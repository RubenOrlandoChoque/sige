package com.smartapps.sigev2.models.teacher.pojo;

import com.smartapps.sigev2.models.people.Person;

import java.util.Date;

public class TeacherDataSync extends Person {
    private Date HireDate;
    private String CUIL;

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

