package com.smartapps.sigev2.models.tutor;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonRepository;
import com.smartapps.sigev2.models.people.PersonRepositoryTemp;
import com.smartapps.sigev2.models.students.Student;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync;
import com.smartapps.sigev2.models.tutorStudents.TutorStudents;
import com.smartapps.sigev2.models.tutorStudents.TutorStudentsRepositoryTemp;
import com.smartapps.sigev2.models.tutorStudents.pojo.TutorStudentRelationshipData;

import java.util.List;
import java.util.UUID;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TutorViewModel extends AndroidViewModel {

    private PersonRepository personRepository;
    private PersonRepositoryTemp personRepositoryTemp;
    private TutorRepository tutorRepository;
    private TutorRepositoryTemp tutorRepositoryTemp;

    private TutorStudentsRepositoryTemp tutorStudentsRepositoryTemp;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Student>> all;

    public TutorViewModel(Application application) {
        super(application);
        tutorRepository = new TutorRepository(application);
        tutorRepositoryTemp = new TutorRepositoryTemp(application);
        personRepository = new PersonRepository(application);
        personRepositoryTemp = new PersonRepositoryTemp(application);
        tutorStudentsRepositoryTemp = new TutorStudentsRepositoryTemp(application);
    }

    public Maybe<TutorData> getTutorData(String tutorId, String studentId) {
        return tutorRepository.getTutorData(tutorId, studentId);
    }

    public Maybe<TutorStudentRelationshipData> getTutorStudentRelationship(String tutorId, String studentId) {
        return tutorRepository.getTutorStudentRelationship(tutorId, studentId);
    }

    public Maybe<TutorStudentRelationshipData> getTutorStudentRelationshipTmp(String tutorId, String studentId) {
        return tutorRepositoryTemp.getTutorStudentRelationship(tutorId, studentId);
    }

    public Maybe<TutorData> getTutorDataTemp(String tutorId, String studentId) {
        return tutorRepositoryTemp.getTutorData(tutorId, studentId);
    }

    public TutorDataToSync getTutorDataToSync(String tutorId, String studentId) {
        return tutorRepository.getTutorDataToSync(tutorId, studentId);
    }

    public void insert(Tutor word) {
        new Thread(() -> {
            tutorRepository.insert(word);
        }).start();
    }

    public Maybe<Tutor> getTutor(String id) {
        return tutorRepository.getTutor(id);
    }

    // from temp
    public LiveData<List<TutorData>> getStudentTutors(String studentId) {
        return tutorRepositoryTemp.getStudentTutors(studentId);
    }

    public Single<String> saveTempTutor(TutorData tutorData, String studentId) {
        return Single.create(emitter -> {
            try {
                //save Tutor
                Person person = personRepositoryTemp.getByDni(tutorData.getDocumentNumber());
                if (person == null) {
                    person = new Person();
                    String id = UUID.randomUUID().toString();
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
                personRepositoryTemp.insert(person);

                Tutor tutor = tutorRepositoryTemp.getTutorById(person.getId());
                if (tutor == null) {
                    tutor = new Tutor();
                    tutor.setId(person.getId());
                }
                tutor.setSync(false);
                tutor.setOcupation(tutorData.getOcupation());
                tutorRepositoryTemp.insert(tutor);

                // save tutor-student
                TutorStudents tutorStudents = new TutorStudents();
                tutorStudents.setStudentId(studentId);
                tutorStudents.setTutorId(tutor.getId());
                tutorStudents.setCurrentTutor(true);
                tutorStudents.setRelationshipId(tutorData.getRelationshipId());
                tutorStudentsRepositoryTemp.insert(tutorStudents); // insertar nuevo.

                emitter.onSuccess(tutor.getId());
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
