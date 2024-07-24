package com.example.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeepAliveManager;


import java.util.ArrayList;
import java.util.List;


public class KeepAlive {
    private final Activity mActivity;
    public KeepAlive(Activity activity) { this.mActivity = activity; }

    List<String> KeepAliveApp = new ArrayList<>();
    boolean KeepAliveOpenFlag = false;

    public int enableKeepAlive(boolean enable) {
        try {
            @SuppressLint("WrongConstant")
            KeepAliveManager kp = (KeepAliveManager) mActivity.getSystemService("keepalive_service");
            if (enable) {
                kp.buildKeepAliveWhiteList(KeepAliveApp);
                KeepAliveOpenFlag = true;
            } else {
                kp.clearKeepAliveWhiteList();
                KeepAliveOpenFlag = false;
            }
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    public boolean isKeepAliveOpen() {
        if (KeepAliveOpenFlag) {
            return true;
        } else {
            return false;
        }

    }

    public int addKeepAliveAPP(List<String> stringList) {
        try {
            KeepAliveApp = stringList;
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    public int removeKeepAliveAPP() {
        try {
            @SuppressLint("WrongConstant")
            KeepAliveManager kp = (KeepAliveManager) mActivity.getSystemService("keepalive_service");
            kp.clearKeepAliveWhiteList();
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    public List<String> getKeepAliveAPP() {
        if (KeepAliveApp.isEmpty()) {
            return new ArrayList<>();
        }
        return KeepAliveApp;
    }

    public boolean isKeepAliveApp(String appname) {
        return KeepAliveApp.contains(appname);
    }

}
