package com.smartapps.sigev2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.microblink.MicroblinkSDK;
import com.microblink.licence.exception.InvalidLicenceKeyException;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.smartapps.sigev2.BuildConfig;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.base.BaseViewModel;
import com.smartapps.sigev2.models.courseyear.CourseYearViewModel;
import com.smartapps.sigev2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private BaseViewModel baseViewModel;
    private CourseYearViewModel courseYearViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (SharedConfig.getServerUrl().isEmpty()) {
            SharedConfig.setServerUrl(BuildConfig.SERVER_URL);
        }
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        courseYearViewModel = ViewModelProviders.of(this).get(CourseYearViewModel.class);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if ((dialog == null || !dialog.isShowing())) {
            dialog = ProgressDialog.show(this, "",
                    "Cargando", true);
        }

        if (!SharedConfig.isKeyValid()) {
            setScanLicence();
        }

        courseYearViewModel.getAllSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseYears -> {
                            if (courseYears.size() == 0) {
                                SharedConfig.setToken("");
                                SharedConfig.setUserCode("");
                                SharedConfig.setUserName("");
                                SharedConfig.setUserPhoto("");
                                SharedConfig.setUserSurname("");
                            }
                            valid();
                        },
                        error -> {
                            SharedConfig.setToken("");
                            SharedConfig.setUserCode("");
                            SharedConfig.setUserName("");
                            SharedConfig.setUserPhoto("");
                            SharedConfig.setUserSurname("");
                            valid();
                        });
    }

    private void valid() {
        if ((dialog != null && dialog.isShowing())) {
            dialog.dismiss();
        }
        if (SharedConfig.getToken() == null || SharedConfig.getToken().isEmpty()) {
            if (!Util.isNetworkAvailable()) {
                runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        new MaterialDialog.Builder(SplashActivity.this)
                                .title("Aviso")
                                .content(R.string.notinternet)
                                .positiveText("Aceptar").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                SplashActivity.this.finish();
                            }
                        }).show();
                    }
                }));
                return;
            }
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, SchoolYearActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clear all the subscriptions
        mDisposable.clear();
    }

    public void setScanLicence() {
        final String urlServer = SharedConfig.getServerUrl() + "api/account/scanlicence";
        Rx2AndroidNetworking.get(urlServer)
                .addHeaders("Authorization", "Bearer " + SharedConfig.getToken())
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
                            SharedConfig.setScanLicence(jsonObject.getString("Result"));
                            try{
                                MicroblinkSDK.setLicenseKey(SharedConfig.getScanLicence(),getApplicationContext());
                                MicroblinkSDK.setShowTrialLicenseWarning(false);
                                SharedConfig.setKeyValid(true);
                            } catch (InvalidLicenceKeyException e) {
                                Toast.makeText(getApplicationContext(), "Licencia caducada", Toast.LENGTH_SHORT).show();
                                SharedConfig.setScanLicence("");
                                SharedConfig.setKeyValid(false);
                            }
                        } catch (JSONException ex) {
                            String msg = ex.getMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            JSONObject jsonObject = new JSONObject(((ANError) e).getErrorBody());
                            String msg = jsonObject.getString("error_description");
                        } catch (Exception exception) {
                            String msg = exception.getMessage();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}