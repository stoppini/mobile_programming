package com.example.myapplication_test_db.model;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        com.example.myapplication_test_db.model.MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return com.example.myapplication_test_db.model.MyApplication.context;
    }
}
