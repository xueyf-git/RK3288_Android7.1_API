package com.example.sdk;

import android.app.Activity;
import java.util.List;


public class EnjoySDK {
    private final SystemUI mSystemUI;
    private final HomeDesktop mHomeDesktop;
    private final SystemFunctions mSystemFunctions;
    private final TimeManagement mTimeManagement;
    private final LogDumper mlogDumper;
    private final KeepAlive mKeepAlive;
    private final PowerManagement powerManagement;
    private final WlanManagement mWlanManagement;
    private final TimeUtil timeUtil;
    private final DeviceInfoUtil deviceInfoUtil;
    private final EthernetManagement ethernetManagement;

    public EnjoySDK(Activity activity) {
        this.mSystemUI = new SystemUI(activity);
        this.mHomeDesktop = new HomeDesktop(activity);
        this.mSystemFunctions = new SystemFunctions(activity);
        this.mTimeManagement = new TimeManagement(activity);
        this.mlogDumper = new LogDumper(activity);
        this.mKeepAlive = new KeepAlive(activity);
        this.mWlanManagement = new WlanManagement(activity);
        this.powerManagement =new PowerManagement(activity);
        this.timeUtil = new TimeUtil(activity);
        this.deviceInfoUtil = new DeviceInfoUtil(activity);
        this.ethernetManagement = new EthernetManagement(activity);
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

    //电源管理
    public int reboot(){return powerManagement.reboot();}
    public int shutdown(boolean confirm){return powerManagement.shutdown(confirm);}
    public int batteryLevel(){return powerManagement.getBatteryLevel();}
    public String chargeStatus(){return powerManagement.getChargingStatus();}
    public void registerLowBatteryReceiver(){powerManagement.registerLowBatteryReceiver();}
    public void unregisterLowBatteryReceiver(){powerManagement.unregisterLowBatteryReceiver();}
    public boolean getIsLowBatteryWarning(){return powerManagement.getIsLowBatteryWarning();}
    public void setIsLowBatteryWarning(boolean isLBW){powerManagement.isLowBatteryWarning(isLBW);}
    public void setScreenBrightness(int inputBrightness){powerManagement.setScreenBrightness(inputBrightness);}
    public void enableScreenOn(){powerManagement.setScreenAlwaysOn();}
    public void acquireWakeLock(){powerManagement.restoreScreenTimeout();}
    public void setScreenOffTimeout(int time){powerManagement.setScreenOffTimeout(time);}
    public int setTouchWake(boolean enable){return powerManagement.setTouchWake(enable);}
    public int getTouchWakeState(){return powerManagement.getTouchWakeState();}
    public int setTimedTouchWake(boolean enable){return powerManagement.setTimedTouchWake(enable);}
    public int addScheduleToTouchWake(String start,String end,boolean enable){return powerManagement.addScheduleToTouchWake(start,end,enable);}
    public int deleteScheduleToTouchWake(String start,String end,boolean enable){return powerManagement.deleteScheduleToTouchWake(start,end,enable);}
    public int cancelTimedToTouchWake(){return powerManagement.cancleTimedTouchWake();}
    public int getTimedTouchWakeState(){return powerManagement.getTimedTouchWakeState();}
    //时间设置
    public void setTime(int hour,int minute,int second){timeUtil.setTime(hour,minute,second);}
    public void setDate(int year,int month,int day){timeUtil.setDate(year,month,day);}

    //固件信息
    public String getFactoryInfo(){return deviceInfoUtil.getFactoryInfo();}
    public String getProductInfo(){return deviceInfoUtil.getProductInfo();}
    public String getSpecialInfo(){return deviceInfoUtil.getSpecialInfo();}
    public String getCpuTypeInfo(){return deviceInfoUtil.getCpuTypeInfo();}
    public String getCpuSerial(){return deviceInfoUtil.getCpuSerial();}
    public String getAndroidVersionInfo(){return deviceInfoUtil.getAndroidVersionInfo();}
    public String getPlatformVersionInfo(){return deviceInfoUtil.getPlatformVersionInfo();}
    public String getSystemVersion(){return deviceInfoUtil.getSystemVersionInfo();}
    public String getBootVersion(){return deviceInfoUtil.getBootVersion();}
    public String getOemVersion(){return deviceInfoUtil.getOemVersion();}
    public int setTouchPointerCount(int count){return deviceInfoUtil.setTouchPointerCount(count);}
    public int getTouchPointerCount(){return deviceInfoUtil.getTouchPointerCount();}
    public String getFirmwareVersion(){return deviceInfoUtil.getFirmwareVersion();}
    public int getFirmwareVersionCode(){return deviceInfoUtil.getFirmwareVersionCode();}

    //以太网设置
    public int switchEthernet(boolean enable,String ifname){return ethernetManagement.switchEthernet(enable,ifname);}
    public boolean isEthernetEnable(String ifname){return ethernetManagement.isEthernetEnable(ifname);}
    public int setEthernetConfig(EthernetConfigure ethernetConfigure,String ifname){return ethernetManagement.setEthernetConfig(ethernetConfigure,ifname);}
    public EthernetConfigure getEthernetConfig(String ifname){return  ethernetManagement.getEthernetConfig(ifname);}
}
