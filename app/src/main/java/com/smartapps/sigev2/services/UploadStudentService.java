package com.smartapps.sigev2.services;

import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentDataSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Stack;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UploadStudentService {
    private final String TAG = "DownloadService";
    private Stack<StudentDataSync> studentDataSyncs = new Stack<>();
    private UploadStudentListener uploadStudentListener;
    StudentViewModel studentViewModel;
    private boolean success = true;
    private String error = "";

    public UploadStudentService(UploadStudentListener uploadStudentListener) {
        this.uploadStudentListener = uploadStudentListener;
    }

    private void uploadUpdate(JSONObject item) {
        try {
            error += "Sicronizado alumno: " + item.getString("Id") + " - " + item.getString("FirstName") + ", " + item.getString("LastName") + ">>>>>> \n";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String urlServer = SharedConfig.getServerUrl() + "api/students/add";
        AndroidNetworking.post(urlServer)
                .addHeaders("Authorization", "Bearer " + SharedConfig.getToken())
                .addJSONObjectBody(item)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("ResultCode") == 1) {
                                // actualizar sincronizados
                                String studentId = item.getString("Id");
                                JSONObject tutor = item.getJSONObject("Tutor");
                                String tutorId = tutor.getString("Id");
                                UploadStudentService.this.studentViewModel.syncStudentTutor(studentId, tutorId)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<String>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(String s) {
                                                next();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                success = false;
                                                error += "Error al actualizar el alumno como sincronizado en el dispositivo: " + (e.getMessage() != null ? e.getMessage() : "") + "\n";
                                                next();
                                            }
                                        });
                            } else if (response.getInt("ResultCode") == 0) {
                                success = false;
                                error += "Error devuelto por el servidor" + response.toString() + "\n";
                                next();
                            }
                        } catch (Exception e) {
                            success = false;
                            error += (e.getMessage() != null ? e.getMessage() : "") + "\n";
                            next();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("e", anError.toString());
                        error += (anError.getMessage() != null ? anError.getMessage() : "") + "\n";
                        success = false;
                        next();
                    }
                });
    }

    private void sync(StudentDataSync studentDataSync) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            JSONObject student = new JSONObject();

            student.put("Id", studentDataSync.getId());
            student.put("FirstName", studentDataSync.getFirstName());
            student.put("LastName", studentDataSync.getLastName());
            student.put("DocumentTypeId",studentDataSync.getDocumentTypeId());
            student.put("DocumentNumber", studentDataSync.getDocumentNumber());
            student.put("CountryBirth", studentDataSync.getCountryBirth());
            student.put("ProvinceBirth", studentDataSync.getProvinceBirth());
            student.put("DepartmentBirth", studentDataSync.getDepartmentBirth());
            student.put("LocationBirth", studentDataSync.getLocationBirth());
            student.put("Mail", studentDataSync.getMail());
            student.put("MobilePhoneNumber", studentDataSync.getMobilePhoneNumber());
            student.put("PhoneNumber", studentDataSync.getPhoneNumber());
            student.put("AnotherContactPhone", studentDataSync.getAnotherContactPhone());
            student.put("PhoneType1", studentDataSync.getPhoneType1());
            student.put("PhoneType2", studentDataSync.getPhoneType2());
            student.put("PhoneType3", studentDataSync.getPhoneType3());
            student.put("Birthdate", simpleDateFormat.format(studentDataSync.getBirthdate()));
            student.put("Photo", studentDataSync.getPhoto());
            student.put("PersonGenderId", studentDataSync.getPersonGenderId());
            student.put("DivisionId", studentDataSync.getDivisionId());
            student.put("IdImgProfile", studentDataSync.getIdImgProfile());
            student.put("IdCardBack", studentDataSync.getIdCardBack());
            student.put("IdCardFront", studentDataSync.getIdCardFront());
            student.put("Address", studentDataSync.getAddress());
            student.put("HasBrothersOfSchoolAge", studentDataSync.isHasBrothersOfSchoolAge());
            student.put("ScannerResult", studentDataSync.getScannerResult());
            student.put("Nationality", studentDataSync.getNationality());
            student.put("SchoolOrigin", studentDataSync.getSchoolOrigin());
            student.put("Observation", studentDataSync.getObservation());
            student.put("BelongsEthnicGroup", studentDataSync.isBelongsEthnicGroup());
            student.put("EthnicName", studentDataSync.getEthnicName());
            student.put("HasHealthProblems", studentDataSync.isHasHealthProblems());
            student.put("DescriptionHealthProblems", studentDataSync.getDescriptionHealthProblems());
            student.put("HasLegalProblems", studentDataSync.isHasLegalProblems());
            student.put("DescriptionLegalProblems", studentDataSync.getDescriptionLegalProblems());
            student.put("Location", studentDataSync.getLocation());
            student.put("PhoneBelongs1", studentDataSync.getPhoneBelongs1());
            student.put("PhoneBelongs2", studentDataSync.getPhoneBelongs2());
            student.put("HighestEducLevelFatherId",studentDataSync.getHighestEducLevelFatherId());
            student.put("HighestEducLevelMotherId",studentDataSync.getHighestEducLevelMotherId());
            student.put("OtherPhoneBelongs", studentDataSync.getOtherPhoneBelongs());

            JSONArray jsonArrayDocumentatios = new JSONArray();
            for(int doc : studentDataSync.getDocumentations()){
                jsonArrayDocumentatios.put(doc);
            }
            student.put("Documentations", jsonArrayDocumentatios);

            JSONObject tutor = new JSONObject();
            tutor.put("Id", studentDataSync.getTutorDataToSync().getId());
            tutor.put("FirstName", studentDataSync.getTutorDataToSync().getFirstName());
            tutor.put("LastName", studentDataSync.getTutorDataToSync().getLastName());
            tutor.put("DocumentTypeId",studentDataSync.getDocumentTypeId());
            tutor.put("DocumentNumber", studentDataSync.getTutorDataToSync().getDocumentNumber());
            //tutor.put("MobilePhoneNumber", studentDataSync.getTutorDataToSync().getMobilePhoneNumber());
            //tutor.put("PhoneNumber", studentDataSync.getTutorDataToSync().getPhoneNumber());
            //tutor.put("AnotherContactPhone", studentDataSync.getTutorDataToSync().getAnotherContactPhone());
            tutor.put("Birthdate", simpleDateFormat.format(studentDataSync.getTutorDataToSync().getBirthdate()));
            tutor.put("Photo", studentDataSync.getTutorDataToSync().getPhoto());
            tutor.put("PersonGenderId", studentDataSync.getTutorDataToSync().getPersonGenderId());
            tutor.put("Address", studentDataSync.getTutorDataToSync().getAddress());
            tutor.put("RelationshipId", studentDataSync.getTutorDataToSync().getRelationshipId());
            tutor.put("ScannerResult", studentDataSync.getTutorDataToSync().getScannerResult());
            tutor.put("Nationality", studentDataSync.getTutorDataToSync().getNationality());
            tutor.put("Ocupation", studentDataSync.getTutorDataToSync().getOcupation());
            tutor.put("Location", studentDataSync.getTutorDataToSync().getLocation());

            student.put("Tutor", tutor);

            uploadUpdate(student);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void uploadUpdateStudents(StudentViewModel studentViewModel) {
        this.studentViewModel = studentViewModel;
        this.studentViewModel.getDataToSync().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<StudentDataSync>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<StudentDataSync> studentDataSyncs) {
                Stack<StudentDataSync> stack = new Stack<>();
                stack.addAll(studentDataSyncs);
                UploadStudentService.this.studentDataSyncs = stack;
                next();
            }

            @Override
            public void onError(Throwable e) {
                SyncResult syncResult = new SyncResult();
                syncResult.result = false;
                syncResult.errors = "Ocurrió un error al recuperar el listado de alumnos a sincronizar. " + (e.getMessage() != null ? e.getMessage() : "") + "\n";
                uploadStudentListener.onFinish(syncResult);
            }
        });
    }

    private void next() {
        if (studentDataSyncs.size() > 0) {
            sync(studentDataSyncs.pop());
        } else {
            SyncResult syncResult = new SyncResult();
            syncResult.result = success;
            syncResult.errors = error;
            this.uploadStudentListener.onFinish(syncResult);
        }
    }

    public interface UploadStudentListener<T> {
        void onFinish(SyncResult result);
    }
}
