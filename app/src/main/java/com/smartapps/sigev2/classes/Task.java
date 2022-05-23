package com.smartapps.sigev2.classes;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by USUARIO 004 on 6/4/2018.
 */

public abstract class Task<T> {
    public AsyncTask<Void, Void, T> run() {
        AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                T result = null;
                try {
                    result = task();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return result;
            }
        };
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public abstract T task();
}
