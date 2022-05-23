package com.smartapps.sigev2.models.people;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "People")
public class Person implements Serializable {
    @PrimaryKey
    @NonNull
    private String Id;
    private String FirstName;
    private String LastName;
    private int DocumentTypeId;
    private String DocumentNumber;
    private String MobilePhoneNumber;
    private String PhoneNumber;
    private String AnotherContactPhone;
    private Date Birthdate;
    private String Photo;
    private int PersonGenderId;
    private String Address;
    private String Nationality;
    private boolean BelongsEthnicGroup;
    private String EthnicName;
    private boolean HasHealthProblems;
    private String DescriptionHealthProblems;
    private boolean HasLegalProblems;
    private String DescriptionLegalProblems;
    private String Location;
    private String ScannerResult = "";

    private String CountryBirth;
    private String ProvinceBirth;
    private String DepartmentBirth;
    private String LocationBirth;
    private String PhoneType1;
    private String PhoneType2;
    private String PhoneType3;
    private String Mail;

    public String getCountryBirth() {
        return CountryBirth;
    }

    public void setCountryBirth(String countryBirth) {
        CountryBirth = countryBirth;
    }

    public String getProvinceBirth() {
        return ProvinceBirth;
    }

    public void setProvinceBirth(String provinceBirth) {
        ProvinceBirth = provinceBirth;
    }

    public String getDepartmentBirth() {
        return DepartmentBirth;
    }

    public void setDepartmentBirth(String departmentBirth) {
        DepartmentBirth = departmentBirth;
    }

    public String getLocationBirth() {
        return LocationBirth;
    }

    public void setLocationBirth(String locationBirth) {
        LocationBirth = locationBirth;
    }

    public String getPhoneType1() {
        return PhoneType1;
    }

    public void setPhoneType1(String phoneType1) {
        PhoneType1 = phoneType1;
    }

    public String getPhoneType2() {
        return PhoneType2;
    }

    public void setPhoneType2(String phoneType2) {
        PhoneType2 = phoneType2;
    }

    public String getPhoneType3() {
        return PhoneType3;
    }

    public void setPhoneType3(String phoneType3) {
        PhoneType3 = phoneType3;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public boolean isBelongsEthnicGroup() {
        return BelongsEthnicGroup;
    }

    public void setBelongsEthnicGroup(boolean belongsEthnicGroup) {
        BelongsEthnicGroup = belongsEthnicGroup;
    }

    public String getEthnicName() {
        return EthnicName;
    }

    public void setEthnicName(String ethnicName) {
        EthnicName = ethnicName;
    }

    public boolean isHasHealthProblems() {
        return HasHealthProblems;
    }

    public void setHasHealthProblems(boolean hasHealthProblems) {
        HasHealthProblems = hasHealthProblems;
    }

    public String getDescriptionHealthProblems() {
        return DescriptionHealthProblems;
    }

    public void setDescriptionHealthProblems(String descriptionHealthProblems) {
        DescriptionHealthProblems = descriptionHealthProblems;
    }

    public boolean isHasLegalProblems() {
        return HasLegalProblems;
    }

    public void setHasLegalProblems(boolean hasLegalProblems) {
        HasLegalProblems = hasLegalProblems;
    }

    public String getDescriptionLegalProblems() {
        return DescriptionLegalProblems;
    }

    public void setDescriptionLegalProblems(String descriptionLegalProblems) {
        DescriptionLegalProblems = descriptionLegalProblems;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getScannerResult() {
        return ScannerResult;
    }

    public void setScannerResult(String scannerResult) {
        ScannerResult = scannerResult;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        this.Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lasttName) {
        this.LastName = lasttName;
    }

    public int getDocumentTypeId() {
        return DocumentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.DocumentTypeId = documentTypeId;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.DocumentNumber = documentNumber;
    }

    public String getMobilePhoneNumber() {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.MobilePhoneNumber = mobilePhoneNumber;
    }

    public String getAnotherContactPhone() {
        return AnotherContactPhone;
    }

    public void setAnotherContactPhone(String anotherContactPhone) {
        this.AnotherContactPhone = anotherContactPhone;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.Birthdate = birthdate;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }

    public int getPersonGenderId() {
        return PersonGenderId;
    }

    public void setPersonGenderId(int personGenderId) {
        this.PersonGenderId = personGenderId;
    }
}
