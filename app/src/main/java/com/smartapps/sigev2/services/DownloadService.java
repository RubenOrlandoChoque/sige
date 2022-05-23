package com.smartapps.sigev2.services;

import com.smartapps.sigev2.classes.SharedConfig;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONObject;

import io.reactivex.Observable;

public class DownloadService {
    public DownloadService() {
    }

    public Observable<JSONObject> rxDownload(String api) {
        final String urlServer = SharedConfig.getServerUrl() + api;
        return Rx2AndroidNetworking.get(urlServer)
                .addHeaders("Authorization", "Bearer " + SharedConfig.getToken())
                .build()
                .getJSONObjectObservable();
    }
}
