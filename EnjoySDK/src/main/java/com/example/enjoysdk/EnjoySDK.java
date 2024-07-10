package com.example.enjoysdk;

import android.app.Activity;
import java.util.List;


public class EnjoySDK {
    private final SystemUI mSystemUI;
    private final HomeDesktop mHomeDesktop;
    private final SystemFunctions mSystemFunctions;
    private final TimeManagement mTimeManagement;
    private final LogDumper mlogDumper;
    private final KeepAlive mKeepAlive;
    private final WlanManagement mWlanManagement;

    public EnjoySDK(Activity activity) {
        this.mSystemUI = new SystemUI(activity);
        this.mHomeDesktop = new HomeDesktop(activity);
        this.mSystemFunctions = new SystemFunctions(activity);
        this.mTimeManagement = new TimeManagement(activity);
        this.mlogDumper = new LogDumper(activity);
        this.mKeepAlive = new KeepAlive(activity);
        this.mWlanManagement = new WlanManagement(activity);
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

    //日志记录
    public int enableLogRecorder(boolean enable){return mlogDumper.enableLogRecorder(enable);}
    public int setRecorderTime(int hour){return  mlogDumper.setRecorderTime(hour);}
    public int getRecorderTime(){return mlogDumper.getRecorderTime();}
    public boolean isLogRecorderEnabled(){return mlogDumper.isLogRecorderEnabled();}
    public String logExport(){return mlogDumper.exportLog();}

    // 应用保活
    public int enableKeepAlive(boolean enable) { return mKeepAlive.enableKeepAlive(enable);}
    public int addKeepAliveAPP(List<String> stringList) { return mKeepAlive.addKeepAliveAPP(stringList);}
    public int removeKeepAliveAPP() { return mKeepAlive.removeKeepAliveAPP();}
    public boolean isKeepAliveOpen() { return mKeepAlive.isKeepAliveOpen();}
    public List<String> getKeepAliveAPP() { return mKeepAlive.getKeepAliveAPP();}
    public boolean isKeepAliveApp(String appname) { return mKeepAlive.isKeepAliveApp(appname);}

    // wlan管理
    public int enableWiFi(boolean enable) { return mWlanManagement.enableWiFi(enable); }
    public List<String> getWiFiList() { return mWlanManagement.getWiFiList(); }
    public int connectToWiFi(String ssid, String password) { return mWlanManagement.connectToWiFi(ssid, password); }
    public List<String> refreshWiFiList() { return mWlanManagement.refreshWiFiList(); }
    public List<WlanManagement.WiFiInfo> showConnectedWiFiInfo() { return mWlanManagement.showConnectedWiFiInfo(); }




}
