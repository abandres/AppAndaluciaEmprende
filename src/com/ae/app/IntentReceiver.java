package com.ae.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

    private static final String TAG = IntentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received intent: " + intent.toString());
        String action = intent.getAction();

        // NO HACEMOS NADA AL RECIBIR LA NOTIFICACIÓN, SE PINTA Y PUNTO
        /*if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
            int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
 
            Log.i(TAG, "Received push notification. Alert: " + intent.getStringExtra(PushManager.EXTRA_ALERT)
                + ". Payload: " + intent.getStringExtra(PushManager.EXTRA_STRING_EXTRA) + ". NotificationID="+id);

            String alert = intent.getStringExtra(PushManager.EXTRA_ALERT);
            String extra = intent.getStringExtra(PushManager.EXTRA_STRING_EXTRA);

            PushNotificationPlugin plugin = PushNotificationPlugin.getInstance();
            plugin.sendResultBack(alert, extra);
        } else*/
        // SE HA HECHO CLIC EN LA NOTIFICACIÓN
        if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
            Log.i(TAG, "User clicked notification. Message: " + intent.getStringExtra(PushManager.EXTRA_ALERT)
                    + ". Payload: " + intent.getStringExtra(PushManager.EXTRA_STRING_EXTRA));
            
            String alert = intent.getStringExtra(PushManager.EXTRA_ALERT);
            String extra = intent.getStringExtra(PushManager.EXTRA_STRING_EXTRA);
            
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(UAirship.shared().getApplicationContext(), AppAndaluciaEmprendeActivity.class);
            i.setComponent(cn);
            UAirship.shared().getApplicationContext().startActivity(i);
            //Intent i = new Intent(context, AppAndaluciaEmprendeActivity.class);
            //UAirship.shared().getApplicationContext().startActivity(i);
            
            // abrimos la aplicación
//            Intent launch = new Intent(Intent.ACTION_MAIN);
//            launch.setClass(UAirship.shared().getApplicationContext(), AppAndaluciaEmprendeActivity.class);
//            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            UAirship.shared().getApplicationContext().startActivity(launch);
                        
            // llamamos al plugin y le decimos que ahí va el retorno de la notificación
            PushNotificationPlugin plugin = PushNotificationPlugin.getInstance();
            plugin.sendResultBack(alert, extra);
            
        } else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
            Log.i(TAG, "Registration complete. APID:" + intent.getStringExtra(PushManager.EXTRA_APID)
                    + ". Valid: " + intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false));
            //PushPreferences prefs = PushManager.shared().getPreferences();
//            PushClient.envia(PushClient.URL_REGISTER_APID+prefs.getPushId()+"/", "PUT", "{\"tags\": [\"tag1\"],"+" \"extra\": {\"com.urbanairship.push.C2DM_REGISTRATION_ID\": \""+prefs.getC2DMId()+"\"}}"+"}");
        }

    }
}