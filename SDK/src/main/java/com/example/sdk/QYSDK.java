package com.example.sdk;

import android.app.Activity;
import android.view.View;

import java.util.List;


public class QYSDK {
    private final SystemUI mSystemUI;
    private final HomeDesktop mHomeDesktop;
    private final SystemFunctions mSystemFunctions;
    private final TimeManagement mTimeManagement;
    private final LogDumper mlogDumper;
    private final KeepAlive mKeepAlive;
    private final PowerManagement powerManagement;
    private final WlanManagement mWlanManagement;
    private final DeviceInfoUtil deviceInfoUtil;
    private final EthernetManagement ethernetManagement;
    private final BootAnimation bootAnimation;
    private final ScreenRotation screenRotation;
    private final HardwareState hardwareState;

    public QYSDK(Activity activity) {
        this.mSystemUI = new SystemUI(activity);
        this.mHomeDesktop = new HomeDesktop(activity);
        this.mSystemFunctions = new SystemFunctions(activity);
        this.mTimeManagement = new TimeManagement(activity);
        this.mlogDumper = new LogDumper(activity);
        this.mKeepAlive = new KeepAlive(activity);
        this.mWlanManagement = new WlanManagement(activity);
        this.powerManagement =new PowerManagement(activity);
        this.deviceInfoUtil = new DeviceInfoUtil(activity);
        this.ethernetManagement = new EthernetManagement(activity);
        this.bootAnimation = new BootAnimation(activity);
        this.screenRotation = new ScreenRotation(activity);
        this.hardwareState = new HardwareState(activity);
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
    public void setTime(int hour,int minute,int second){mTimeManagement.setTime(hour,minute,second);}
    public void setDate(int year,int month,int day){mTimeManagement.setDate(year,month,day);}


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
    public void disableScreenOn(){powerManagement.restoreScreenTimeout();}
    public void setScreenOffTimeout(int time){powerManagement.setScreenOffTimeout(time);}
    public int setTouchWake(boolean enable){return powerManagement.setTouchWake(enable);}
    public int getTouchWakeState(){return powerManagement.getTouchWakeState();}
    public int setTimedTouchWake(boolean enable){return powerManagement.setTimedTouchWake(enable);}
    public int addScheduleToTouchWake(String start,String end,boolean enable){return powerManagement.addScheduleToTouchWake(start,end,enable);}
    public int deleteScheduleToTouchWake(String start,String end,boolean enable){return powerManagement.deleteScheduleToTouchWake(start,end,enable);}
    public int cancelTimedToTouchWake(){return powerManagement.cancleTimedTouchWake();}
    public int getTimedTouchWakeState(){return powerManagement.getTimedTouchWakeState();}

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
    public void initTouchListener(View view){deviceInfoUtil.initTouchListener(view);}

    //以太网设置
    public int switchEthernet(boolean enable,String ifname){return ethernetManagement.switchEthernet(enable,ifname);}
    public boolean isEthernetEnable(String ifname){return ethernetManagement.isEthernetEnable(ifname);}
    public boolean setDynamicIp(Activity activity){return ethernetManagement.setDynamicIp(activity);}
    public boolean setEthernetStaticIp(Activity activity,String address, String mask, String gate, String dns, String dns2){return ethernetManagement.setEthernetStaticIp(activity, address, mask, gate, dns, dns2);}
    public EthernetConfigure getEthernetConfig(String ifname){return  ethernetManagement.getEthernetConfig(ifname);}
    public String getEthernetMacAddress(String ifname){return ethernetManagement.getEthernetMacAddress(ifname);}
    public String getEthernetConnectState(String ifname){return ethernetManagement.getEthernetConnectState(ifname);}
    public String[] getEthernetDevices() {return ethernetManagement.getEthernetDevices();}

    //开机动画
    public int setBootAnimation(String bootAnimationPath){return bootAnimation.setBootAnimation(bootAnimationPath);}
    public int resetBootAnimation(){return bootAnimation.resetBootAnimation();}

    //屏幕旋转
    public int setScreenRotation(int rotation){return screenRotation.setSystemRotation(rotation);}
    public int getSystemRotation(){return screenRotation.getSystemRotation();}

    //硬件状态
    public String getCPUUsage(){return hardwareState.getCpuUsage();}
    public String getCPUTemperature(){return hardwareState.getCpuTemperature();}
    public long getUpTime(){return hardwareState.getUptime();}

}
