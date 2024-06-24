package com.example.enjoysdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EnjoySDK {
    private final SystemUI mSystemUI;
    private final HomeDesktop mHomeDesktop;
    private final SystemFunctions mSystemFunctions;
    private final TimeManagement mTimeManagement;
    public EnjoySDK(Activity activity) {
        this.mSystemUI = new SystemUI(activity);
        this.mHomeDesktop = new HomeDesktop(activity);
        this.mSystemFunctions = new SystemFunctions(activity);
        this.mTimeManagement = new TimeManagement(activity);
    }
    // 系统UI
    public int setStatusBarShowStatus(boolean status) { return mSystemUI.setStatusBarShowStatus(status); }
    public int setNavigationBarShowStatus(boolean status) { return mSystemUI.setNavigationBarShowStatus(status); }
    public int switchStatusBarAndNavigationOverwrite(boolean isShow) { return mSystemUI.switchStatusBarAndNavigationOverwrite(isShow); }

    // 开机桌面
    public int setHomePackage(String packageName) { return mHomeDesktop.setHomePackage(packageName); }
    public String getHomePackage() { return mHomeDesktop.getHomePackage(); }
    public int setHomeScreenApp() { return mHomeDesktop.setHomeScreenApp(); }

    // 系统功能
    public int takeScreenshot() { return mSystemFunctions.takeScreenshot(); }

    //时间管理
    public int switchAutoDateAndTime(boolean enable) { return mTimeManagement.switchAutoDateAndTime(enable); }
    public void openAutoTime() { mTimeManagement.openAutoTime(); }
    public void closeAutoTime() { mTimeManagement.closeAutoTime(); }
    public void toggleAutoTime() { mTimeManagement.toggleAutoTime(); }


}
