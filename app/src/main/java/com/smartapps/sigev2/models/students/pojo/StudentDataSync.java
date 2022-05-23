package com.smartapps.sigev2.models.students.pojo;

import androidx.room.Ignore;

import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync;

import java.util.List;

public class StudentDataSync extends Person {
    private boolean HasBrothersOfSchoolAge;
    private int DivisionId;
    private String TutorId;
    private String IdImgProfile;
    private String IdCardFront;
    private String IdCardBack;
    private String Observation;
    private String SchoolOrigin;
    private String OtherPhoneBelongs;

    private String PhoneBelongs1;
    private String PhoneBelongs2;
    private int HighestEducLevelFatherId;
    private int HighestEducLevelMotherId;

    public String getPhoneBelongs1() {
        return PhoneBelongs1;
    }

    public void setPhoneBelongs1(String phoneBelongs1) {
        PhoneBelongs1 = phoneBelongs1;
    }

    public String getPhoneBelongs2() {
        return PhoneBelongs2;
    }

    public void setPhoneBelongs2(String phoneBelongs2) {
        PhoneBelongs2 = phoneBelongs2;
    }

    public int getHighestEducLevelFatherId() {
        return HighestEducLevelFatherId;
    }

    public void setHighestEducLevelFatherId(int highestEducLevelFatherId) {
        HighestEducLevelFatherId = highestEducLevelFatherId;
    }

    public int getHighestEducLevelMotherId() {
        return HighestEducLevelMotherId;
    }

    public void setHighestEducLevelMotherId(int highestEducLevelMotherId) {
        HighestEducLevelMotherId = highestEducLevelMotherId;
    }

    @Ignore
    private com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync TutorDataToSync;
    @Ignore
    private List<Integer> Documentations;

    public String getOtherPhoneBelongs() {
        return OtherPhoneBelongs;
    }

    public void setOtherPhoneBelongs(String otherPhoneBelongs) {
        OtherPhoneBelongs = otherPhoneBelongs;
    }

    public List<Integer> getDocumentations() {
        return Documentations;
    }

    public void setDocumentations(List<Integer> documentations) {
        Documentations = documentations;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public String getSchoolOrigin() {
        return SchoolOrigin;
    }

    public void setSchoolOrigin(String schoolOrigin) {
        SchoolOrigin = schoolOrigin;
    }

    public String getIdImgProfile() {
        return IdImgProfile;
    }

    public void setIdImgProfile(String idImgProfile) {
        IdImgProfile = idImgProfile;
    }

    public String getIdCardFront() {
        return IdCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        IdCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return IdCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        IdCardBack = idCardBack;
    }

    public String getTutorId() {
        return TutorId;
    }

    public void setTutorId(String tutorId) {
        TutorId = tutorId;
    }

    public TutorDataToSync getTutorDataToSync() {
        return TutorDataToSync;
    }

    public void setTutorDataToSync(TutorDataToSync TutorDataToSync) {
        this.TutorDataToSync = TutorDataToSync;
    }

    public boolean isHasBrothersOfSchoolAge() {
        return HasBrothersOfSchoolAge;
    }

    public void setHasBrothersOfSchoolAge(boolean hasBrothersOfSchoolAge) {
        HasBrothersOfSchoolAge = hasBrothersOfSchoolAge;
    }

    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int divisionId) {
        DivisionId = divisionId;
    }
}
