package com.smartapps.sigev2.models.tutor;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.smartapps.sigev2.models.people.Person;

@Entity(tableName = "Tutors", foreignKeys = @ForeignKey(entity = Person.class,
        parentColumns = "Id",
        childColumns = "Id"))
public class Tutor {
    @PrimaryKey
    @NonNull
    private String Id;
    private boolean Sync = false;
    private String Ocupation;

    public String getOcupation() {
        return Ocupation;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }
}
