package com.smartapps.sigev2.models.payments;


import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;

@Dao
public abstract class PaymentDao implements BaseDao<Payment> {
    @Query("select * from Payments")
    abstract Payment getPayments();

    @Query("select Id, StudentId, PaymentConceptId, SchoolYearId from Payments")
    abstract Payment getAttrs();
}