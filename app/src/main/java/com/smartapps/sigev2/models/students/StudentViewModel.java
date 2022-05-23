package com.smartapps.sigev2.models.students;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.payments.Payment;
import com.smartapps.sigev2.models.payments.PaymentRepository;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonRepository;
import com.smartapps.sigev2.models.people.PersonRepositoryTemp;
import com.smartapps.sigev2.models.studentdivision.StudentDivisions;
import com.smartapps.sigev2.models.studentdivision.StudentDivisionsRepository;
import com.smartapps.sigev2.models.studentdivision.StudentDivisionsRepositoryTemp;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentation;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentationRepository;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentationRepositoryTemp;
import com.smartapps.sigev2.models.students.pojo.ImageToSync;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.models.students.pojo.StudentDataSync;
import com.smartapps.sigev2.models.students.pojo.StudentListData;
import com.smartapps.sigev2.models.tutor.Tutor;
import com.smartapps.sigev2.models.tutor.TutorRepository;
import com.smartapps.sigev2.models.tutor.TutorRepositoryTemp;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync;
import com.smartapps.sigev2.models.tutorStudents.TutorStudents;
import com.smartapps.sigev2.models.tutorStudents.TutorStudentsRepository;
import com.smartapps.sigev2.models.tutorStudents.TutorStudentsRepositoryTemp;
import com.smartapps.sigev2.util.Validation;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import static br.com.zbra.androidlinq.Linq.stream;

public class StudentViewModel extends AndroidViewModel {

    private PersonRepository personRepository;
    private PersonRepositoryTemp personRepositoryTemp;
    private StudentRepository studentRepository;
    private StudentRepositoryTemp studentRepositoryTemp;
    private StudentDivisionsRepository studentDivisionsRepository;
    private StudentDivisionsRepositoryTemp studentDivisionsRepositoryTemp;
    private PaymentRepository paymentRepository;
    private TutorRepository tutorRepository;
    private TutorRepositoryTemp tutorRepositoryTemp;
    private TutorStudentsRepository tutorStudentsRepository;
    private TutorStudentsRepositoryTemp tutorStudentsRepositoryTemp;
    private StudentDocumentationRepository studentDocumentationRepository;
    private StudentDocumentationRepositoryTemp studentDocumentationRepositoryTemp;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Student>> all;

    public StudentViewModel(Application application) {
        super(application);
        studentRepository = new StudentRepository(application);
        personRepository = new PersonRepository(application);
        studentDivisionsRepository = new StudentDivisionsRepository(application);
        paymentRepository = new PaymentRepository(application);
        tutorRepository = new TutorRepository(application);
        tutorStudentsRepository = new TutorStudentsRepository(application);
        studentDocumentationRepository = new StudentDocumentationRepository(application);
        // temp repository
        studentRepositoryTemp = new StudentRepositoryTemp(application);
        personRepositoryTemp = new PersonRepositoryTemp(application);
        tutorStudentsRepositoryTemp = new TutorStudentsRepositoryTemp(application);
        tutorRepositoryTemp = new TutorRepositoryTemp(application);
        studentDocumentationRepositoryTemp = new StudentDocumentationRepositoryTemp(application);
        studentDivisionsRepositoryTemp = new StudentDivisionsRepositoryTemp(application);
    }

    public void insert(Student word) {
        new Thread(() -> {
            studentRepository.insert(word);
        }).start();
    }

    public Maybe<StudentData> getStudent(String id) {
        return studentRepository.getStudent(id);
    }

    public LiveData<List<StudentListData>> getStudentList(int courseYearId, int shiftId) {
        return studentRepository.getStudentList(courseYearId, shiftId);
    }

    public Single<Boolean> saveStudent(StudentData studentData) {
        return Single.create(emitter -> {
            try {
                // Save Student
                Person person = personRepository.getByDni(studentData.getDocumentNumber());
                if (person == null) {
                    person = new Person();
                    String id = studentData.getId();
                    person.setId(id);
                }
                person.setDocumentTypeId(studentData.getDocumentTypeId());
                person.setFirstName(studentData.getFirstName());
                person.setLastName(studentData.getLastName());
                person.setDocumentNumber(studentData.getDocumentNumber());
                person.setPersonGenderId(studentData.getPersonGenderId());
                person.setPhoneNumber(studentData.getPhoneNumber());
                person.setMobilePhoneNumber(studentData.getMobilePhoneNumber());
                person.setAnotherContactPhone(studentData.getAnotherContactPhone());
                person.setAddress(studentData.getAddress());
                person.setBirthdate(studentData.getBirthdate());
                person.setNationality(studentData.getNationality());
                person.setLocation(studentData.getLocation());

                person.setBelongsEthnicGroup(studentData.isBelongsEthnicGroup());
                person.setEthnicName(studentData.getEthnicName());
                person.setHasHealthProblems(studentData.isHasHealthProblems());
                person.setDescriptionHealthProblems(studentData.getDescriptionHealthProblems());
                person.setHasLegalProblems(studentData.isHasLegalProblems());
                person.setDescriptionLegalProblems(studentData.getDescriptionLegalProblems());
                person.setCountryBirth(studentData.getCountryBirth());
                person.setProvinceBirth(studentData.getProvinceBirth());
                person.setDepartmentBirth(studentData.getDepartmentBirth());
                person.setLocationBirth(studentData.getLocationBirth());
                person.setPhoneType1(studentData.getPhoneType1());
                person.setPhoneType2(studentData.getPhoneType2());
                person.setPhoneType3(studentData.getPhoneType3());
                person.setMail(studentData.getMail());


                if (!studentData.getScannerResult().trim().isEmpty()) {
                    person.setScannerResult(studentData.getScannerResult());
                }
                personRepository.insert(person);

                Student student = studentRepository.getStudentById(person.getId());
                if (student == null) {
                    student = new Student();
                    student.setId(person.getId());
                }
                student.setSync(false);
                if (student.getIdCardBack() == null || !student.getIdCardBack().equals(studentData.IdCardBack)) {
                    student.setSyncCardBack(false);
                }
                if (student.getIdCardFront() == null || !student.getIdCardFront().equals(studentData.IdCardFront)) {
                    student.setSyncCardFront(false);
                }
                if (student.getIdImgProfile() == null || !student.getIdImgProfile().equals(studentData.IdImgProfile)) {
                    student.setSyncImgProfile(false);
                }

                student.setHasBrothersOfSchoolAge(studentData.HasBrothersOfSchoolAge);
                student.setIdCardBack(studentData.IdCardBack);
                student.setIdCardFront(studentData.IdCardFront);
                student.setIdImgProfile(studentData.IdImgProfile);
                student.setSchoolOrigin(studentData.SchoolOrigin);
                student.setObservation(studentData.Observation);
                student.setPhoneBelongs1(studentData.PhoneBelongs1);
                student.setPhoneBelongs2(studentData.PhoneBelongs2);
                student.setOtherPhoneBelongs(studentData.OtherPhoneBelongs);
                student.setConditionId(studentData.ConditionId);
                student.setHighestEducLevelFatherId(studentData.HighestEducLevelFatherId);
                student.setHighestEducLevelMotherId(studentData.HighestEducLevelMotherId);

                studentRepository.insert(student);

                // studen division
                StudentDivisions studentDivisions = new StudentDivisions();
                studentDivisions.setStudentId(student.getId());
                studentDivisions.setDivisionId(studentData.DivisionId);
                studentDivisions.setCurrentDivision(true);
                studentDivisionsRepository.disableAll(student.getId()); // deshabilitar anteriores
                studentDivisionsRepository.insert(studentDivisions); // insertar nuevo.

                // student documentations
                studentDocumentationRepository.deleteAll(student.getId());
                for (int documentation : studentData.documentations) {
                    StudentDocumentation studentDocumentation = new StudentDocumentation();
                    studentDocumentation.setDocumentationId(documentation);
                    studentDocumentation.setStudentId(student.getId());
                    studentDocumentationRepository.insert(studentDocumentation);
                }

                Payment payment = new Payment();
                payment.setPaymentConceptId(0);
                payment.setStudentId(student.getId());
                payment.setAmount(studentData.Amount);


                //save Tutors
                //traer todos los tutores de la base de datos temporal y guardarlos en la base de datos principal

                tutorStudentsRepository.disableAll(student.getId());
                List<TutorData> studentTutorsList = tutorRepositoryTemp.getStudentTutorsList(student.getId());
                for (TutorData tutorData : studentTutorsList) {
                    person = personRepository.getByDni(tutorData.getDocumentNumber());
                    if (person == null) {
                        person = new Person();
                        String id = tutorData.getId();
                        person.setId(id);
                    }
                    person.setDocumentTypeId(tutorData.getDocumentTypeId());
                    person.setFirstName(tutorData.getFirstName());
                    person.setLastName(tutorData.getLastName());
                    person.setDocumentNumber(tutorData.getDocumentNumber());
                    person.setPersonGenderId(tutorData.getPersonGenderId());
                    person.setPhoneNumber(tutorData.getPhoneNumber());
                    person.setMobilePhoneNumber(tutorData.getMobilePhoneNumber());
                    person.setAnotherContactPhone(tutorData.getAnotherContactPhone());
                    person.setAddress(tutorData.getAddress());
                    person.setBirthdate(tutorData.getBirthdate());
                    person.setNationality(tutorData.getNationality());
                    person.setLocation(tutorData.getLocation());
                    if (!tutorData.getScannerResult().trim().isEmpty()) {
                        person.setScannerResult(tutorData.getScannerResult());
                    }
                    personRepository.insert(person);

                    Tutor tutor = tutorRepository.getById(person.getId());
                    if (tutor == null) {
                        tutor = new Tutor();
                        tutor.setId(person.getId());
                    }
                    tutor.setSync(false);
                    tutor.setOcupation(tutorData.getOcupation());
                    tutorRepository.insert(tutor);

                    // save tutor-student
                    TutorStudents tutorStudents = new TutorStudents();
                    tutorStudents.setStudentId(student.getId());
                    tutorStudents.setTutorId(tutor.getId());
                    tutorStudents.setCurrentTutor(true);
                    tutorStudents.setRelationshipId(tutorData.getRelationshipId());
                    tutorStudentsRepository.insert(tutorStudents); // insertar nuevo.
                }

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

//    public Completable saveStudent(StudentData studentData, TutorData tutorData) {
//
//        return Completable.fromAction(() -> {
//            // if there's no use, create a new user.
//            // if we already have a user, then, since the user object is immutable,
//            // create a new user, with the id of the previous user and the updated user name.
//        });
//    }

    public Single<List<StudentDataSync>> getDataToSync() {

        return Single.create(new SingleOnSubscribe<List<StudentDataSync>>() {
            @Override
            public void subscribe(SingleEmitter<List<StudentDataSync>> emitter) throws Exception {
                List<StudentDataSync> data = studentRepository.getStudentToSync();
                for (StudentDataSync studentDataSync : data) {
                    List<Integer> documentations = studentDocumentationRepository.getStudentDocumentationsToSync(studentDataSync.getId());
                    studentDataSync.setDocumentations(documentations);

                    if (studentDataSync.getIdCardBack() != null && !studentDataSync.getIdCardBack().isEmpty()) {
                        try {
                            String[] s = studentDataSync.getIdCardBack().split(File.separator);
                            studentDataSync.setIdCardBack(s[s.length - 1]);
                        } catch (Exception ignored) {
                        }
                    }
                    if (studentDataSync.getIdCardFront() != null && !studentDataSync.getIdCardFront().isEmpty()) {
                        try {
                            String[] s = studentDataSync.getIdCardFront().split(File.separator);
                            studentDataSync.setIdCardFront(s[s.length - 1]);
                        } catch (Exception ignored) {
                        }
                    }
                    if (studentDataSync.getIdImgProfile() != null && !studentDataSync.getIdImgProfile().isEmpty()) {
                        try {
                            String[] s = studentDataSync.getIdImgProfile().split(File.separator);
                            studentDataSync.setIdImgProfile(s[s.length - 1]);
                        } catch (Exception ignored) {
                        }
                    }
                    TutorDataToSync tutorDataToSync = tutorRepository.getTutorDataToSync(studentDataSync.getTutorId(), studentDataSync.getId());
                    studentDataSync.setTutorDataToSync(tutorDataToSync);
                }

                emitter.onSuccess(data);
            }
        });
    }

    public Single<List<ImageToSync>> getImageToSync() {

        return Single.create(new SingleOnSubscribe<List<ImageToSync>>() {
            @Override
            public void subscribe(SingleEmitter<List<ImageToSync>> emitter) throws Exception {
                List<ImageToSync> front = studentRepository.getCardFrontImageToSync();
                List<ImageToSync> back = studentRepository.getCardBackImageToSync();
                List<ImageToSync> imgProfile = studentRepository.getImgProfileImageToSync();
                List<ImageToSync> list = new ArrayList<>();
                list.addAll(front);
                list.addAll(back);
                list.addAll(imgProfile);
                emitter.onSuccess(list);
            }
        });
    }


    public Single<String> syncStudentTutor(String studentId, String tutorId) {
        return Single.create(emitter -> {
            try {
                Student student = studentRepository.getStudentById(studentId);
                if (student != null) {
                    student.setSync(true);
                    studentRepository.insert(student);
                }

                Tutor tutor = tutorRepository.getById(tutorId);
                if (tutor != null) {
                    tutor.setSync(true);
                    tutorRepository.insert(tutor);
                }
                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<String> syncImage(ImageToSync item) {
        return Single.create(emitter -> {
            try {
                Student student = studentRepository.getStudentById(item.getId());
                if (student != null) {
                    switch (item.getType()) {
                        case ImageToSync.CARD_BACK:
                            student.setSyncCardBack(true);
                            break;
                        case ImageToSync.CARD_FRONT:
                            student.setSyncCardFront(true);
                            break;
                        case ImageToSync.IMG_PROFILE:
                            student.setSyncImgProfile(true);
                            break;
                    }

                    studentRepository.insert(student);
                }
                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Observable<JSONObject> rxDownload() {
        return studentRepository.rxDownload();
    }

    public void save(JSONObject students) {
        studentRepository.save(students);
    }

    public Single<TempData> createTempData(String studentId, boolean keepTutors) {
        return Single.create(emitter -> {
            try {
                boolean newStudent;
                // studiante
                String newStudentId = UUID.randomUUID().toString();
                String newPaymentId = UUID.randomUUID().toString();
                if (studentId == null || studentId.isEmpty()) { // crear uno nuevo
                    newStudent = true;
                    if(keepTutors) {
                        deleteAllTempDataKeepTutor(newStudentId);
                    }else{
                        deleteAllTempData();

                        Person p = new Person();
                        p.setId(newStudentId);
                        personRepositoryTemp.insert(p);

                        Student s = new Student();
                        s.setId(newStudentId);
                        studentRepositoryTemp.insert(s);
                    }
                } else { // cargar el existente en el temporal
                    newStudentId = studentId;
                    deleteAllTempData();
                    newStudent = false;
                    Student student = studentRepository.getStudentById(studentId);
                    Person personStudent = personRepository.getById(studentId);
                    if (personStudent != null) {
                        personRepositoryTemp.insert(personStudent);
                    }

                    if (student != null) {
                        studentRepositoryTemp.insert(student);
                    }

                    // cargar documentacion
                    List<StudentDocumentation> studentDocumentations = studentDocumentationRepository.getDocumentationsList(studentId);
                    for(StudentDocumentation studentDocumentation: studentDocumentations) {
                        studentDocumentationRepositoryTemp.insert(studentDocumentation);
                    }
                    // cargar student division
                    StudentDivisions studentDivisions = studentDivisionsRepository.getByStudentId(studentId);
                    studentDivisionsRepositoryTemp.insert(studentDivisions);

                    // cargar tutores
                    List<TutorData> studentTutorsList = tutorRepository.getStudentTutorsList(studentId);
                    for(TutorData tutorData: studentTutorsList) {
                        Tutor tutor = tutorRepository.getById(tutorData.getId());
                        Person personTutor = personRepository.getById(tutorData.getId());
                        if (personTutor != null) {
                            personRepositoryTemp.insert(personTutor);
                        }

                        if (tutor != null) {
                            tutorRepositoryTemp.insert(tutor);
                        }

                        // tutor student
                        List<TutorStudents> byTutorAndStudent = tutorStudentsRepository.getByTutorAndStudent(studentId, tutorData.getId());
                        for(TutorStudents tutorStudents : byTutorAndStudent){
                            tutorStudentsRepositoryTemp.insert(tutorStudents);
                        }
                    }
                }
                emitter.onSuccess(new TempData(newStudentId, newStudent));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public void deleteAllTempData(){
        tutorStudentsRepositoryTemp.deleteAll();
        tutorRepositoryTemp.deleteAll();
        studentDocumentationRepositoryTemp.deleteAll();
        studentDivisionsRepositoryTemp.deleteAll();
        studentRepositoryTemp.deleteAll();
        personRepositoryTemp.deleteAll();
    }

    public Single<Boolean> deleteAllTempDataSingle() {
        return Single.create(emitter -> {
            try {
                deleteAllTempData();
                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private void deleteAllTempDataKeepTutor(String newStudentId){
        studentDocumentationRepositoryTemp.deleteAll();
        studentDivisionsRepositoryTemp.deleteAll();
        List<TutorStudents> allTutorStudents  = tutorStudentsRepositoryTemp.getAllList();
        tutorStudentsRepositoryTemp.deleteAll();
        studentRepositoryTemp.deleteAll();

        List<Tutor> allTutors = tutorRepositoryTemp.getAll();

        List<String> keepIdTutors = stream(allTutors).select(Tutor::getId).toList();

        personRepositoryTemp.deleteAllExcept(keepIdTutors);

        Person p = new Person();
        p.setId(newStudentId);
        personRepositoryTemp.insert(p);

        Student s = new Student();
        s.setId(newStudentId);
        studentRepositoryTemp.insert(s);

        for(TutorStudents tutorStudents: allTutorStudents) {
            tutorStudents.setStudentId(newStudentId);
            tutorStudents.setCurrentTutor(true);
            tutorStudentsRepositoryTemp.insert(tutorStudents);
        }
    }

    public Maybe<StudentData> getTempStudent(String id) {
        return studentRepositoryTemp.getStudent(id);
    }

    public Single<String> validar(StudentData studentData) {
        return Single.create(emitter -> {
            String error = "";
            if (studentData.getFirstName().isEmpty()) {
                error = "Debe ingresar nombre del alumno";
            }
            if (studentData.getLastName().isEmpty()) {
                error = "Debe ingresar apellido del alumno";
            }
            if (studentData.getDocumentNumber().isEmpty()) {
                error = "Debe ingresar número de documento del alumno";
            }

            if (studentData.getPersonGenderId() == -1) {
                error = "Debe ingresar sexo del alumno";
            }

            if (studentData.getBirthdate() == null) {
                error = "Debe ingresar fecha de nacimiento del alumno";
            }

            if (studentData.getNationality().isEmpty()) {
                error = "Debe ingresar la Nacionalidad del alumno";
            }

            if (studentData.getAddress().isEmpty()) {
                error = "Debe ingresar el domicilio del alumno";
            }

            if (!studentData.getPhoneNumber().isEmpty()) {
                if (studentData.getPhoneType1().isEmpty()) {
                    error = "Debe indicar el Tipo del Teléfono 1";
                } else {
                    if (studentData.PhoneBelongs1.isEmpty()) {
                        error = "Debe indicar a quién pertenece el Teléfono 1";
                    }
                }
            }

            if (!studentData.getMobilePhoneNumber().isEmpty()) {
                if (studentData.getPhoneType2().isEmpty()) {
                    error = "Debe indicar el Tipo del Teléfono 2";
                } else {
                    if (studentData.PhoneBelongs2.isEmpty()) {
                        error = "Debe indicar a quién pertenece el Teléfono 2";
                    }
                }
            }

            if (!studentData.getAnotherContactPhone().isEmpty()) {
                if (studentData.getPhoneType3().isEmpty()) {
                    error = "Debe indicar el Tipo del Teléfono 3";
                } else {
                    if (studentData.PhoneBelongs2.isEmpty()) {
                        error = "Debe indicar a quién pertenece el Teléfono 3";
                    }
                }
            }

            if (!studentData.HasBrothersOfSchoolAgeAnswered) {
                error = "Debe ingresar si el alumno tiene hermanos en edad escolar";
            }

            if (studentData.DivisionId == -1) {
                error = "Debe ingresar año y division del alumno";
            }

            // checkear tutores
            List<TutorData> studentTutorsList = tutorRepositoryTemp.getStudentTutorsList(studentData.getId());
            if (studentTutorsList.isEmpty()) {
                error = "Debe ingresar al menos un tutor";
            }

            if (!Validation.isEmailAddress(studentData.getMail())) {
                error = "Debe ingresar un <strong> Mail válido</strong>";
            }

            TutorData tutorData = stream(studentTutorsList).where(st -> st.getRelationshipId() == 0).firstOrNull();
            if (tutorData != null) {
                error = "Falta definir parentesco para el tutor: " + tutorData.getLastName() + ",  " + tutorData.getFirstName();
            }
            if(error.isEmpty()) {
                emitter.onSuccess("");
            }else {
                emitter.onError(new Throwable(error));
            }
        });
    }

    public Single<String> updateStudenTempData(String oldStudentId, String newStudentId) {
        return Single.create(emitter -> {
            Student oldStudent = studentRepositoryTemp.getStudentById(oldStudentId);
            Person oldPerson = personRepositoryTemp.getById(oldStudentId);
            List<StudentDocumentation> oldDocumentations = studentDocumentationRepositoryTemp.getAllList();
            List<StudentDivisions> oldDivisions = studentDivisionsRepositoryTemp.getAllList();
            List<TutorStudents> oldTutorStudents = tutorStudentsRepositoryTemp.getAllList();

            tutorStudentsRepositoryTemp.deleteAll();
            studentDocumentationRepositoryTemp.deleteAll();
            studentDivisionsRepositoryTemp.deleteAll();
            studentRepositoryTemp.deleteAll();
            personRepositoryTemp.delete(oldStudentId);

            oldPerson.setId(newStudentId);
            oldStudent.setId(newStudentId);
            personRepositoryTemp.insert(oldPerson);
            studentRepositoryTemp.insert(oldStudent);
            for(StudentDocumentation studentDocumentation: oldDocumentations) {
                studentDocumentation.setStudentId(newStudentId);
                studentDocumentationRepositoryTemp.insert(studentDocumentation);
            }

            for(StudentDivisions studentDivisions: oldDivisions) {
                studentDivisions.setStudentId(newStudentId);
                studentDivisionsRepositoryTemp.insert(studentDivisions);
            }

            for(TutorStudents tutorStudents: oldTutorStudents) {
                tutorStudents.setStudentId(newStudentId);
                tutorStudentsRepositoryTemp.insert(tutorStudents);
            }
            emitter.onSuccess("");
        });
    }

    public class TempData {
        public String studentId;
        public boolean newStudent;

        TempData(String studentId, boolean newStudent) {
            this.studentId = studentId;
            this.newStudent = newStudent;
        }
    }
}
