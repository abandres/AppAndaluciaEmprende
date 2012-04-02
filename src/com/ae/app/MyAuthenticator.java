package com.ae.app;

import java.net.PasswordAuthentication;

public class MyAuthenticator extends java.net.Authenticator{

	private String user;
    private String passwd;

    public MyAuthenticator(String user, String passwd)
    {
        this.user = user;
        this.passwd = passwd;
    }

    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(user, passwd.toCharArray());
    }
    
}
