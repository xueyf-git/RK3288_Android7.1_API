package com.example.sdk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.UserHandle;

public class SystemUI {
    private final Activity mActivity;
    public SystemUI(Activity activity) { this.mActivity = activity; }

    Intent NavigationBarHide = new Intent("android.intent.action.HIDE_NAVIGATION_BAR");
    Intent NavigationBarShow = new Intent("android.intent.action.SHOW_NAVIGATION_BAR");
    Intent StatusBarHide = new Intent("android.intent.action.HIDE_STATUS_BAR");
    Intent StatusBarShow = new Intent("android.intent.action.SHOW_STATUS_BAR");


    /**
     * 根据布尔参数切换系统状态栏显示和隐藏
     * @param status 配置参数（true 表示显示，false 表示隐藏）
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int setStatusBarShowStatus(boolean status) {

        try {
            if (mActivity == null) {
                throw new IllegalStateException("No activity provided.");
            }
            if (status) {
                NavigationBarShow.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
                mActivity.sendBroadcast(StatusBarShow);
            } else {
                mActivity.sendBroadcast(StatusBarHide);
            }
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }

    }

    /**
     * 根据布尔参数切换系统导航栏显示和隐藏
     * @param status 配置参数（true 表示显示，false 表示隐藏）
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int setNavigationBarShowStatus(boolean status) {

        try {
            if (mActivity == null) {
                throw new IllegalStateException("No activity provided.");
            }
            if (status) {
                mActivity.sendBroadcast(NavigationBarShow);
            } else {
                mActivity.sendBroadcast(NavigationBarHide);
            }
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }

    }

    /**
     * 根据布尔参数切换系统导航栏和系统状态栏同时显示和同时隐藏
     * @param isShow 配置参数（true 表示显示，false 表示隐藏）
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int switchStatusBarAndNavigationOverwrite(boolean isShow) {
        Intent NavigationBarHide = new Intent("android.intent.action.HIDE_NAVIGATION_BAR");
        Intent NavigationBarShow = new Intent("android.intent.action.SHOW_NAVIGATION_BAR");
        Intent StatusBarHide = new Intent("android.intent.action.HIDE_STATUS_BAR");
        Intent StatusBarShow = new Intent("android.intent.action.SHOW_STATUS_BAR");
        try {
            if (mActivity == null) {
                throw new IllegalStateException("No activity provided.");
            }
            if (isShow) {
                mActivity.sendBroadcast(NavigationBarShow);
                mActivity.sendBroadcast(StatusBarShow);
            } else {
                mActivity.sendBroadcast(NavigationBarHide);
                mActivity.sendBroadcast(StatusBarHide);
            }
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

}