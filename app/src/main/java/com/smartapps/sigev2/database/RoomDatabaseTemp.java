package com.smartapps.sigev2.database;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smartapps.sigev2.models.division.Division;
import com.smartapps.sigev2.models.payments.Payment;
import com.smartapps.sigev2.models.payments.PaymentDao;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonDao;
import com.smartapps.sigev2.models.studentdivision.StudentDivisions;
import com.smartapps.sigev2.models.studentdivision.StudentDivisionsDao;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentation;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentationDao;
import com.smartapps.sigev2.models.students.Student;
import com.smartapps.sigev2.models.students.StudentDao;
import com.smartapps.sigev2.models.tutor.Tutor;
import com.smartapps.sigev2.models.tutor.TutorDao;
import com.smartapps.sigev2.models.tutorStudents.TutorStudents;
import com.smartapps.sigev2.models.tutorStudents.TutorStudentsDao;

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */

@Database(entities = {
        Division.class,
        Person.class,
        Student.class,
        StudentDivisions.class,
        Tutor.class,
        TutorStudents.class,
        StudentDocumentation.class,
        Payment.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDatabaseTemp extends androidx.room.RoomDatabase {

    public abstract StudentDao studentDao();

    public abstract PersonDao personDao();

    public abstract StudentDivisionsDao studentDivisionsDao();

    public abstract TutorStudentsDao tutorStudentsDao();

    public abstract TutorDao tutorDao();

    public abstract StudentDocumentationDao studentDocumentationDao();

    public abstract PaymentDao paymentDao();

    private static RoomDatabaseTemp INSTANCE;

    public static RoomDatabaseTemp getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabaseTemp.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabaseTemp.class, "SiGETmp")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static Callback sRoomDatabaseCallback = new Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
