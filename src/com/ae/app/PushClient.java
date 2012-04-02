package com.ae.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class PushClient {

	// estas variables deberán ser parámetros del sistema
	public static final String username = "zFi0XTI_RAm0zhpC4fJK8w";
	public static final String password = "5UHv8ZedTrmrBQyIaGQZlw";
	public static final String URL_BROADCAST = "https://go.urbanairship.com/api/push/broadcast/";
	public static final String URL_PUSH = "https://go.urbanairship.com/api/push/";
	public static final String URL_REGISTER_APID = "https://go.urbanairship.com/api/apids/";
	
	public static void main(String[] args)
	{
		
	}
	
	public static void envia(String purl, String method,String postdata){
		try
	    {
			URL url;
			url = new URL(purl);
			
	        String responseString = "";
	        String outputString = "";
	        
	        Authenticator.setDefault(new MyAuthenticator(username,password));
	        
	        URLConnection urlConnection = url.openConnection();
	        HttpURLConnection httpConn = (HttpURLConnection)urlConnection;
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        
	        byte[] buffer = new byte[postdata.length()];
	        buffer = postdata.getBytes("UTF8");

	        bout.write(buffer);
	        byte[] b = bout.toByteArray();
	        httpConn.setRequestProperty("Content-Length",String.valueOf(b.length));

	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestMethod(method);

	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);

	        OutputStream out = httpConn.getOutputStream();
	        out.write(b);
	        out.close();

	        System.out.println(httpConn.getResponseCode());
	        
	        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
	        BufferedReader in = new BufferedReader(isr);

	        while ((responseString = in.readLine()) != null) 
	        {
	            outputString = outputString + responseString;
	        }
	        System.out.println(outputString);

	    }
	    catch (MalformedURLException e) 
	    {
	        e.printStackTrace();
	    }
	    catch (IOException e1) 
	    {
	        e1.printStackTrace();
	    }
	}
}
