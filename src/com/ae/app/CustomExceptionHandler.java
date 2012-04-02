package com.ae.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.SharedPreferences;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;


public class CustomExceptionHandler  implements UncaughtExceptionHandler {
	 private UncaughtExceptionHandler defaultUEH;

	    private String localPath;

	    private String url;

	    /* 
	     * if any of the parameters is null, the respective functionality 
	     * will not be used 
	     */
	    public CustomExceptionHandler(String localPath, String url) {
	        this.localPath = localPath;
	        this.url = url;
	        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	    }

	    public void uncaughtException(Thread t, Throwable e) {
	        String timestamp = new Timestamp(new Date().getTime()).toString();
	        final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	        e.printStackTrace(printWriter);
	        String stacktrace = result.toString();
	        printWriter.close();
	        String filename = timestamp + ".stacktrace";

	        if (localPath != null) {
	            writeToFile(stacktrace, filename);
	        }
	        if (url != null) {
	            sendToServer(stacktrace, filename);
	        }

	        defaultUEH.uncaughtException(t, e);
	    }
	    
	    public void uncaughtExceptionJS(Thread t, Throwable e) {
	        String timestamp = new Timestamp(new Date().getTime()).toString();
	        final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	        e.printStackTrace(printWriter);
	        String stacktrace = result.toString();
	        printWriter.close();
	        String filename = timestamp + ".stacktrace";

	        if (localPath != null) {
	            writeToFile(stacktrace, filename);
	        }
	        if (url != null) {
	            sendToServer(stacktrace, filename);
	        }
	        
	    }	    

	    private void writeToFile(String stacktrace, String filename) {
			 Set pref = PushManager.shared().getTags();
			 if(pref.contains("errores"))
			 {
		    	try {
		            BufferedWriter bos = new BufferedWriter(new FileWriter(
		                    localPath + "/" + filename));
		            bos.write(stacktrace);
		            bos.flush();
		            bos.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			 }
	    }

	    private void sendToServer(String stacktrace, String filename) {
			 Set pref = PushManager.shared().getTags();
			 if(pref.contains("errores"))
			 {
		    	DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpPost httpPost = new HttpPost(url);
		        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		        nvps.add(new BasicNameValuePair("filename", filename));
		        nvps.add(new BasicNameValuePair("stacktrace", "APID:"+PushManager.shared().getAPID()+"---"+stacktrace));
		        try {
		            httpPost.setEntity(
		                    new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		            httpClient.execute(httpPost);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
	    }
}
