package com.smartapps.sigev2.services;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.smartapps.sigev2.classes.SharedConfig;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthService {
    public void login(String user, String pass, OnLoginEvent onLoginEvent) {
        String urlServer = SharedConfig.getServerUrl();
        Rx2AndroidNetworking.post(urlServer + "api/auth/token")
                .addUrlEncodeFormBodyParameter("grant_type", "password")
                .addUrlEncodeFormBodyParameter("username", user)
                .addUrlEncodeFormBodyParameter("password", pass)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF8")
                .setPriority(Priority.MEDIUM)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        try {
                            SharedConfig.setToken(jsonObject.getString("access_token"));
                            SharedConfig.setUserCode(jsonObject.getString("userName"));
                            SharedConfig.setUserName(jsonObject.getString("firstName"));
                            SharedConfig.setUserSurname(jsonObject.getString("lastName"));
                            SharedConfig.setUserPhoto(jsonObject.getString("photo"));
                            JSONArray shifts = new JSONArray(jsonObject.getString("shifts"));
                            HashSet<String> ids = new HashSet<>();
                            for (int i = 0; i < shifts.length(); i++) {
                                JSONObject shift = shifts.getJSONObject(i);
                                ids.add(shift.getString("Id"));
                            }
                            SharedConfig.setUserShiftIds(ids);
                            onLoginEvent.successLogin();
                        } catch (JSONException e) {
                            onLoginEvent.errorLogin(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            JSONObject jsonObject = new JSONObject(((ANError) e).getErrorBody());
                            String msg = jsonObject.getString("error_description");
                            onLoginEvent.errorLogin(msg);
                        } catch (Exception exception) {
                            onLoginEvent.errorLogin(exception.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public interface OnLoginEvent {
        void successLogin();

        void errorLogin(String msg);
    }
}
