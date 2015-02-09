package com.msn.weiboclient;

import android.app.Application;
import android.content.Context;

/**
 * Created by Msn on 2015/2/6.
 */
public class WeiBoApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
