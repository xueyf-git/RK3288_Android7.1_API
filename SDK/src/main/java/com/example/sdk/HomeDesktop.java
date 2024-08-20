package com.example.sdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;


public class HomeDesktop {
    private final Activity mActivity;
    public HomeDesktop(Activity activity) { this.mActivity = activity; }

    /**
     * 设置默认的主屏幕应用程序包名
     * @param packageName 要设置为默认主屏幕的应用程序包名
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int setHomePackage(String packageName) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtra("packageName", packageName);
            mActivity.startActivity(intent);
            Log.i("testForUpdate","set homepage successfully.");
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    /**
     * 获取默认的主屏幕应用程序包名
     * @param
     * @return 成功返回 获取到的包名
     */
    public String getHomePackage() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = packageManager.resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            return resolveInfo.activityInfo.packageName;
        }
        return null;
    }

    /**
     * 设置默认的主屏幕应用
     * @param
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int setHomeScreenApp() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            mActivity.startActivity(intent);
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

}
