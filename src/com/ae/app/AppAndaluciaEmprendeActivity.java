package com.ae.app;

import org.apache.cordova.DroidGap;
import org.apache.cordova.api.LOG;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class AppAndaluciaEmprendeActivity extends DroidGap {
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
				this.getString(R.string.localLogPlace), this.getString(R.string.urlLog)));
		
		super.loadUrl("file:///android_asset/www/index.html", 3000);
		super.setIntegerProperty("splashscreen", R.drawable.splash);
	}
	
    @Override
    public void onStart() {
        super.onStart();
        UAirship.shared().getAnalytics().activityStarted(this);    
    }

    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        UAirship.shared().getAnalytics().activityStopped(this);
    }
    

 
    
}