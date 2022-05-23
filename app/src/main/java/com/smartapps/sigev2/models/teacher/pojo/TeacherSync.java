package com.smartapps.sigev2.models.teacher.pojo;

import androidx.room.Relation;

import com.smartapps.sigev2.models.employeejobtitles.EmployeeJobTitle;

import java.util.Date;
import java.util.List;

public class TeacherSync {
    @Relation(parentColumn = "Id",
            entityColumn = "EmployeeId")
    private List<EmployeeJobTitle> JobTitles;
    private String Id;
    private String FirstName;
    private String LastName;
    private String DocumentNumber;
    private String MobilePhoneNumber;
    private String PhoneNumber;
    private String AnotherContactPhone;
    private Date Birthdate;
    private String Photo;
    private int PersonGenderId;
    private String Address;
    private Date HireDate;
    private String CUIL;
    private String ScannerResult;
    private String Ocupation;

    public String getOcupation() {
        return Ocupation;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }

    public String getScannerResult() {
        return ScannerResult;
    }

    public void setScannerResult(String scannerResult) {
        ScannerResult = scannerResult;
    }

    public List<EmployeeJobTitle> getJobTitles() {
        return JobTitles;
    }

    public void setJobTitles(List<EmployeeJobTitle> jobTitles) {
        JobTitles = jobTitles;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getMobilePhoneNumber() {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        MobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAnotherContactPhone() {
        return AnotherContactPhone;
    }

    public void setAnotherContactPhone(String anotherContactPhone) {
        AnotherContactPhone = anotherContactPhone;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(Date birthdate) {
        Birthdate = birthdate;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public int getPersonGenderId() {
        return PersonGenderId;
    }

    public void setPersonGenderId(int personGenderId) {
        PersonGenderId = personGenderId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
