package com.smartapps.sigev2.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by rchoque on 11/4/2018.
 */

public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0, NETWORK_STAUS_WIFI = 1, NETWORK_STATUS_MOBILE = 2;

    public static boolean getConnectivityStatus(Context context) {
        Log.e("eeeeee>>>>>>>>>>", "change 2222" + Util.isNetworkAvailable());
        return Util.isNetworkAvailable();
    }

    public static boolean getConnectivityStatusString() {
        Log.e("eeeeee>>>>>>>>>>", "change" + Util.isNetworkAvailable());
        return Util.isNetworkAvailable();
    }
}