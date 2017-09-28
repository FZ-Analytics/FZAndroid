package com.fz.fzapp.utils;

/**
 * Created by ignat on 16-Jun-16.
 */

public class FixValue {
    public static final int SPLASH_DISPLAY_LENGHT = 2500;
    public static final String strPreferenceName = "com.smart_tbk.fz.pref";
    public static final String Hostname = "http://192.168.200.23:8084/fz/api/v1/";
//user
    public static final String RestfulLogin = "users/login";
    public static final String RestfulLogout = "users/logout";
    public static final String RestfulChangePassword = "users/changepassword";
    public static final String RestfulRegistration = "users/registration";
    public static final String RestfulReasonlist = "reasons/reasonlist";
    //task
    public static final String RestfulTasklist = "tasks/synchronize";
    public static final String RestfulTasklistTrx = "tasks/tasksync";
    public static final String RestfulUpload = "tasks/upload";
    public static final int TimeoutConnection = 45000;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    public static final int intSuccess = 0;
    public static final int intFail = -1;
    public static final int intNull = -2;
    public static final int intNoNetwork = -3;
    public static final int intNoData = -4;
}
