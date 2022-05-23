package com.smartapps.sigev2.services;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.ImageToSync;
import com.smartapps.sigev2.util.Util;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class UploadImageService {
    private final String TAG = "DownloadService";
    private Stack<ImageToSync> studentDataSyncs = new Stack<>();
    private UploadStudentListener uploadStudentListener;
    private StudentViewModel studentViewModel;
    private boolean success = true;
    private String error = "";

    public UploadImageService(UploadStudentListener uploadStudentListener) {
        this.uploadStudentListener = uploadStudentListener;
    }

    private void uploadUpdate(ImageToSync item) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(24, TimeUnit.HOURS)
                .readTimeout(24, TimeUnit.HOURS)
                .writeTimeout(24, TimeUnit.HOURS)
                .build();

        error += "Sicronizado image: " + item.getId() + " >>>>>>>>> \n";

        AndroidNetworking.upload(SharedConfig.getServerUrl()
                + "/api/Upload/PostStudentImage")
                .addMultipartFile("file", new File(item.getPath()))
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        Log.e("e", response.toString());
                        if (okHttpResponse.code() == 201) {
                            UploadImageService.this.studentViewModel.syncImage(item)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<String>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(String s) {
                                            Util.borrarArchivo(item.getPath());
                                            next();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            success = false;
                                            error += "Error al actualizar la imagen como sincronizado en el dispositivo: " + item.getPath() + (e.getMessage() != null ? e.getMessage() : "") + "\n";
                                            next();
                                        }
                                    });
                        } else {
                            error += "Error devuelto por el servidor: " + response.toString() + ". Code: " + okHttpResponse.code() + "\n";
                            success = false;
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

    public void uploadImageStudents(StudentViewModel studentViewModel) {
        this.studentViewModel = studentViewModel;
        this.studentViewModel.getImageToSync().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ImageToSync>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<ImageToSync> studentDataSyncs) {
                Stack<ImageToSync> stack = new Stack<>();
                stack.addAll(studentDataSyncs);
                UploadImageService.this.studentDataSyncs = stack;
                next();
            }

            @Override
            public void onError(Throwable e) {
                SyncResult syncResult = new SyncResult();
                syncResult.result = false;
                syncResult.errors = "Ocurrió un error al recuperar las imagenes para sincronizar. " + (e.getMessage() != null ? e.getMessage() : "") + "\n";
                uploadStudentListener.onFinish(syncResult);
            }
        });
    }

    private void next() {
        if (studentDataSyncs.size() > 0) {
            ImageToSync imageToSync = UploadImageService.this.studentDataSyncs.pop();
            File file = new File(imageToSync.getPath());
            if(file.exists()){
                uploadUpdate(imageToSync);
            } else{
                next();
            }
        } else {
            SyncResult syncResult = new SyncResult();
            syncResult.result = success;
            syncResult.errors = error;
            this.uploadStudentListener.onFinish(syncResult);
        }
    }

    public interface UploadStudentListener<T> {
        void onFinish(SyncResult success);
    }
}
