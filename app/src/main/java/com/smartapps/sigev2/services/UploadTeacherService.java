package com.smartapps.sigev2.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.employeejobtitles.EmployeeJobTitle;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;
import com.smartapps.sigev2.models.teacher.pojo.TeacherSync;

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

public class UploadTeacherService {
    private final String TAG = "DownloadService";
    private Stack<TeacherSync> teacherSyncs = new Stack<>();
    private UploadTeachersListener uploadTeachersListener;
    TeacherViewModel teacherViewModel;
    private boolean success = true;

    public UploadTeacherService(UploadTeachersListener uploadTeachersListener) {
        this.uploadTeachersListener = uploadTeachersListener;
    }

    private void uploadUpdate(JSONObject item) {
        final String urlServer = SharedConfig.getServerUrl() + "api/employees/add";
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
                                String teacherId = item.getString("Id");
                                UploadTeacherService.this.teacherViewModel.syncStudentTutor(teacherId)
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
                                                next();
                                            }
                                        });
                            } else if (response.getInt("ResultCode") == 0) {
                                success = false;
                                next();
                            }
                        } catch (Exception e) {
                            success = false;
                            next();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        success = false;
                        next();
                    }
                });
    }

    public void uploadUpdateTeachers(TeacherViewModel teacherViewModel) {
        this.teacherViewModel = teacherViewModel;
        this.teacherViewModel.getDataToSync().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<TeacherSync>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<TeacherSync> studentDataSyncs) {
                Stack<TeacherSync> stack = new Stack<>();
                stack.addAll(studentDataSyncs);
                UploadTeacherService.this.teacherSyncs = stack;
                next();
            }

            @Override
            public void onError(Throwable e) {
                uploadTeachersListener.onFinish(false);
            }
        });
    }

    private void next() {
        if (teacherSyncs.size() > 0) {
            TeacherSync teacherSync = teacherSyncs.pop();
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                JSONObject teacher = new JSONObject();

                teacher.put("Id", teacherSync.getId());
                teacher.put("FirstName", teacherSync.getFirstName());
                teacher.put("LastName", teacherSync.getLastName());
                teacher.put("DocumentNumber", teacherSync.getDocumentNumber());
                teacher.put("MobilePhoneNumber", teacherSync.getMobilePhoneNumber());
                teacher.put("PhoneNumber", teacherSync.getPhoneNumber());
                teacher.put("Birthdate", simpleDateFormat.format(teacherSync.getBirthdate()));
                teacher.put("PersonGenderId", teacherSync.getPersonGenderId());
                teacher.put("Address", teacherSync.getAddress());
                teacher.put("CUIL", teacherSync.getCUIL());
                teacher.put("HireDate", simpleDateFormat.format(teacherSync.getHireDate()));
                teacher.put("ScannerResult", teacherSync.getScannerResult());



                JSONArray jobTitles = new JSONArray();

                for (EmployeeJobTitle employeeJobTitle : teacherSync.getJobTitles()) {
                    JSONObject job = new JSONObject();
                    job.put("JobTitleId", employeeJobTitle.getJobTitleId());
                    job.put("ShiftId", employeeJobTitle.getShiftId());
                    job.put("TypeEmployeeId", employeeJobTitle.getTypeEmployeeId());
                    jobTitles.put(job);
                }
                teacher.put("JobTitles", jobTitles);

                uploadUpdate(teacher);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.uploadTeachersListener.onFinish(success);
        }
    }

    public interface UploadTeachersListener<T> {
        void onFinish(boolean success);
    }
}
