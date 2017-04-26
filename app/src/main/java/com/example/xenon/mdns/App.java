package com.example.xenon.mdns;

import android.app.Application;

/**
 * Created by xenon on 13.03.2017.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NSDController.INSTANCE.init(getApplicationContext());
        LogController.INSTANCE.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDNSController.INSTANCE.init(getApplicationContext());
            }
        }).start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mDNSController.INSTANCE.unregister();
        NSDController.INSTANCE.stopDiscovery();
        NSDController.INSTANCE.tearDown();
    }
}
