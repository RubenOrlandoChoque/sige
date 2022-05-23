package com.smartapps.sigev2;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.microblink.MicroblinkSDK;
import com.microblink.licence.exception.InvalidLicenceKeyException;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.ui.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AndroidNetworking.initialize(getApplicationContext());
        //Stetho.initializeWithDefaults(this);

        try{
            if (!SharedConfig.getScanLicence().isEmpty()) {
                MicroblinkSDK.setLicenseKey(SharedConfig.getScanLicence(),this);
                MicroblinkSDK.setShowTrialLicenseWarning(false);
                SharedConfig.setKeyValid(true);
            }
        } catch (InvalidLicenceKeyException e) {
            Toast.makeText(getApplicationContext(), "Licencia caducada", Toast.LENGTH_SHORT).show();
            //setScanLicence();
            SharedConfig.setScanLicence("");
            SharedConfig.setKeyValid(false);
        }
        //String key = "sRwAAAAUY29tLnNtYXJ0YXBwcy5zaWdldjIenBEcBte+WrPd9NYzqPyPlG7Xte6vWz5EwgAVjoqRzbW15z/SDQMfWYiwZLqjMZK0lxiW3GpQp4wMcle9ZYPEKoErnfeeuSCuiO/sq/oOrLSV9lWoC5eOqyYUsUr2IEJhPGOjfWht+XEe6bjR6hTaFwgyueqLkhiwZOQprxr4o4r0bM9UEgVDCazDqg9ITxO5Y6NWKzzdFjOX+jxMcVuU05SgvuTWhVEd7wIgb61R";
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
                        } catch (JSONException e) {
                            String msg = e.getMessage();
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

    public static Context getAppContext() {
        return context;
    }
}
