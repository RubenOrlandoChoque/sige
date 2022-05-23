package com.smartapps.sigev2.models.students;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.smartapps.sigev2.models.people.Person;

@Entity(tableName = "Students", foreignKeys = {
        @ForeignKey(entity = Person.class,
                parentColumns = "Id",
                childColumns = "Id"
        )
})
public class Student {
    @PrimaryKey
    @NonNull
    private String Id;
    private boolean HasBrothersOfSchoolAge;
    private String IdCardFront;
    private String IdCardBack;
    private String IdImgProfile;
    private boolean Sync = false;
    private boolean SyncCardFront = false;
    private boolean SyncCardBack = false;
    private boolean SyncImgProfile = false;
    private String Observation;
    private String SchoolOrigin;
    private String OtherPhoneBelongs;

    private String PhoneBelongs1;
    private String PhoneBelongs2;
    private int HighestEducLevelFatherId;
    private int HighestEducLevelMotherId;

    private int ConditionId;

    public int getConditionId() {
        return ConditionId;
    }

    public void setConditionId(int conditionId) {
        ConditionId = conditionId;
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

    public String getPhoneBelongs1() {
        return PhoneBelongs1;
    }

    public void setPhoneBelongs1(String phoneBelongs1) {
        PhoneBelongs1 = phoneBelongs1;
    }

    public String getOtherPhoneBelongs() {
        return OtherPhoneBelongs;
    }

    public void setOtherPhoneBelongs(String otherPhoneBelongs) {
        OtherPhoneBelongs = otherPhoneBelongs;
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

    public boolean isSyncImgProfile() {
        return SyncImgProfile;
    }

    public void setSyncImgProfile(boolean syncImgProfile) {
        SyncImgProfile = syncImgProfile;
    }

    public boolean isSyncCardFront() {
        return SyncCardFront;
    }

    public String getIdImgProfile() {
        return IdImgProfile;
    }

    public void setIdImgProfile(String idImgProfile) {
        IdImgProfile = idImgProfile;
    }

    public void setSyncCardFront(boolean syncCardFront) {
        SyncCardFront = syncCardFront;
    }

    public boolean isSyncCardBack() {
        return SyncCardBack;
    }

    public void setSyncCardBack(boolean syncCardBack) {
        SyncCardBack = syncCardBack;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
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

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        this.Id = id;
    }

    public boolean isHasBrothersOfSchoolAge() {
        return HasBrothersOfSchoolAge;
    }

    public void setHasBrothersOfSchoolAge(boolean hasBrothersOfSchoolAge) {
        this.HasBrothersOfSchoolAge = hasBrothersOfSchoolAge;
    }
}
