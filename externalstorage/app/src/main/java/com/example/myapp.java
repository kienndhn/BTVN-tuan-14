package com.example;

import android.app.Application;
import android.content.Context;

public class myapp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        myapp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return myapp.context;
    }
}
