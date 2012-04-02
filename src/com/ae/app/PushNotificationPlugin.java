package com.ae.app;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

import org.apache.cordova.api.LOG;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;


public class PushNotificationPlugin extends Plugin {
    final static String TAG = PushNotificationPlugin.class.getSimpleName();

    static PushNotificationPlugin instance = null;

    public static final String ACTION = "registerCallback";
    public static final String ACTION_APP_READY = "appReady";
    public static final String ACTION_PUSH_CONF = "pushConf";
    public static final String ACTION_ERROR = "error";    
    
    
    private static String sData ="";
    
    public static PushNotificationPlugin getInstance() {

        return instance;
    }

    public void sendResultBack(String msg, String payload) {
    	sData = URLEncoder.encode("{\"msg\":\""+msg+"\",\"payload\":"+URLDecoder.decode(payload)+"}");
        String js = String.format("navigator.pushNotification.notificationCallback('%s');", sData);
        this.sendJavascript(js);
    }

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {

        instance = this;

        PluginResult result = null;
        if (ACTION.equals(action)) {
            result = new PluginResult(Status.NO_RESULT);
            result.setKeepCallback(false);
            if (sData.length()>0){
        		String js = String.format("navigator.pushNotification.notificationCallback('%s');", sData);
        		this.sendJavascript(js);
        		sData = "";
        	}            
        } else if (ACTION_PUSH_CONF.equals(action)){        	
        	try {
        		pushConf(data.getJSONObject(0));
			} catch (JSONException e) {
				new CustomExceptionHandler(
						UAirship.shared().getApplicationContext().getString(R.string.localLogPlace), 
						UAirship.shared().getApplicationContext().getString(R.string.urlLog)).uncaughtException(Thread.currentThread(), e);
				}
        } else if(ACTION_APP_READY.equals(action)) {
        	if (sData.length()>0){
        		String js = String.format("navigator.pushNotification.notificationCallback('%s');", sData);
        		this.sendJavascript(js);
        		sData = "";
        	}
        }else if(ACTION_ERROR.equals(action)) {
        	this.error(data);
        } else {
            Log.d(TAG, "Invalid action: " + action + " passed");
            result = new PluginResult(Status.INVALID_ACTION);
        }
        return result;
    }
  
    private void pushConf(JSONObject json){
    	try {
			if ("off".equals(json.getString("push"))){
				PushManager.disablePush();
			} else {
				PushManager.enablePush();
				// metemos los tags
				JSONArray nuevasTags = new JSONArray();
				boolean sonidos = false;
				boolean vibracion = false;

				try{
					nuevasTags = json.getJSONArray("tags");
					sonidos = nuevasTags.toString().contains("sonidos");
					vibracion = nuevasTags.toString().contains("vibracion");
				}catch (Exception e) {
					//no hacemos nada
				}
				
				PushPreferences prefs = PushManager.shared().getPreferences();        
		        prefs.setSoundEnabled(sonidos); // enable/disable sound when a push is received
		        prefs.setVibrateEnabled(vibracion); // enable/disable vibrate on receive
		        Set<String> tags = PushManager.shared().getTags();
		        tags.clear(); 
		        for (int i=0;i<nuevasTags.length();i++){
		        	tags.add(nuevasTags.getString(i));
		        }
		        PushManager.shared().setTags(tags);
			}
		} catch (JSONException e) {
			new CustomExceptionHandler(
					UAirship.shared().getApplicationContext().getString(R.string.localLogPlace), 
					UAirship.shared().getApplicationContext().getString(R.string.urlLog)).uncaughtException(Thread.currentThread(), e);
		}
    }
    
    public void error(JSONArray data){
    	String s = "";
    	try {
    		s = ((JSONObject)data.get(1)).getString("error");
		} catch (JSONException e) {
			
		}
    	new CustomExceptionHandler(
				UAirship.shared().getApplicationContext().getString(R.string.localLogPlace), 
				UAirship.shared().getApplicationContext().getString(R.string.urlLog))
		.uncaughtExceptionJS(Thread.currentThread(), new Exception(s));
    }

}