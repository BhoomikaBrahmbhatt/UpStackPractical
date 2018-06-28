package com.giphy.upstackpractical.presenter;

import android.app.Application;
import android.content.res.Configuration;

import com.giphy.upstackpractical.model.MyObjectBox;

import io.objectbox.BoxStore;

public class BaseApplication extends Application {

    private BoxStore boxStore;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        boxStore = MyObjectBox.builder().androidContext(BaseApplication.this).build();

    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    public BoxStore getBoxStore() {
        return boxStore;
    }
}
