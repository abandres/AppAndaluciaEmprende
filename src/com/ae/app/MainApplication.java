package com.ae.app;

import android.app.Application;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class MainApplication extends Application {

    final static String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate(); 

        UAirship.takeOff(this, AirshipConfigOptions.loadDefaultOptions(this));
        PushManager.enablePush();
        PushManager.shared().setIntentReceiver(IntentReceiver.class);
    }

    public void onStop() {
        UAirship.land();
    }
}