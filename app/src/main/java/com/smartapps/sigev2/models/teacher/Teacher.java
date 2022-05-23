package com.smartapps.sigev2.models.teacher;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.smartapps.sigev2.models.people.Person;

import java.util.Date;

@Entity(tableName = "Staff", foreignKeys = {
        @ForeignKey(entity = Person.class,
                parentColumns = "Id",
                childColumns = "Id"
        )
})
public class Teacher {
    @PrimaryKey
    @NonNull
    private String Id;
    private String CUIL;
    private Date HireDate;
    private boolean Sync = false;

    public String getCUIL() {
        return CUIL;
    }

    public void setCUIL(String CUIL) {
        this.CUIL = CUIL;
    }

    public Date getHireDate() {
        return HireDate;
    }

    public void setHireDate(Date hireDate) {
        HireDate = hireDate;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }
}
