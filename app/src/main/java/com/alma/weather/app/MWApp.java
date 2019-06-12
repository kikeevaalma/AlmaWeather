package com.alma.weather.app;

import android.app.Application;

import com.alma.weather.di.AppComponent;
import com.alma.weather.di.DaggerAppComponent;
import com.alma.weather.di.module.ContextModule;

public class MWApp extends Application {
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .application(this)
                .build();
        mAppComponent.inject(this);
    }

    public static void setAppComponent(AppComponent mAppComponent) {
        MWApp.mAppComponent = mAppComponent;
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
