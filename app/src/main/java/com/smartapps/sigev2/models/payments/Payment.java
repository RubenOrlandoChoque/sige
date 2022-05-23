package com.smartapps.sigev2.models.payments;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.smartapps.sigev2.models.students.Student;

@Entity(tableName = "Payments",
        foreignKeys = {
                @ForeignKey(entity = Student.class,
                        parentColumns = "Id",
                        childColumns = "StudentId"
                )
        })
public class Payment {
    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    @NonNull
    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(@NonNull String studentId) {
        StudentId = studentId;
    }

    public int getPaymentConceptId() {
        return PaymentConceptId;
    }

    public void setPaymentConceptId(int paymentConceptId) {
        PaymentConceptId = paymentConceptId;
    }

    public int getSchoolYearId() {
        return SchoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        SchoolYearId = schoolYearId;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        Amount = amount;
    }

    @PrimaryKey
    @NonNull
    private String Id;
    @NonNull
    private String StudentId;
    private int PaymentConceptId;
    private int SchoolYearId;
    private java.util.Date Date;
    private Float Amount;
}
