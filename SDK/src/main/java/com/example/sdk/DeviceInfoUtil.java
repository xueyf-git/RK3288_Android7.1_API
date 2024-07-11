package com.example.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;

public class DeviceInfoUtil {

    private final Activity mActivity;
    MotionEvent event;
    public static int DEVICEINFOUTIL_PERMISSION_REQUIRECODE = 100;
    private static String specialInfo = "无";
    private static int touchPointCount = 0;
    private  int firmwareVersionCode = 0;



    public DeviceInfoUtil(Activity activity ) {
        this.mActivity = activity;
    }

    // 获取设备的生产商信息
    public String getFactoryInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.MANUFACTURER;
    }

    // 获取设备的产品型号
    public String getProductInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.MODEL;
    }

    public void setSpecialInfo(String specialInfo){
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }
        this.specialInfo = specialInfo;

    }

    // 获取定制化信息（此方法需要根据实际情况实现）
    public String getSpecialInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        // 这里假设返回一个定制化信息
        return specialInfo;
    }

    // 获取 CPU 型号信息
    public String getCpuTypeInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.PRODUCT;
    }

    // 获取 CPU 序列号
    @SuppressLint("HardwareIds")
    public String getCpuSerial() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    // 获取 Android 版本信息
    public String getAndroidVersionInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.VERSION.RELEASE;
    }

    // 获取平台版本信息（系统版本）
    public String getPlatformVersionInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.DISPLAY;
    }

    // 获取系统版本信息
    public String getSystemVersionInfo() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return Build.VERSION.INCREMENTAL;
    }

    // 获取 Boot 版本信息（仅支持V2设备）
    public String getBootVersion() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        String bootVersion = getSystemProperty("ro.bootloader");
        return bootVersion;
    }

    // 获取 OEM 版本信息（仅支持V2设备）
    public String getOemVersion() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        String oemVersion = getSystemProperty("ro.oem.version");
        return oemVersion;
    }

    // 设置触摸点数量（?） permission可能需要添加write
    public int setTouchPointerCount(int count) {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        this.touchPointCount = count;
        return 0;
    }

    // 获取触摸点数量
    public int getTouchPointerCount() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        return touchPointCount;
    }

    public void initTouchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            touchPointCount = event.getPointerCount();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            touchPointCount = event.getPointerCount() - 1;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            touchPointCount = 0;
                            break;
                    }
                }
                return true; // 返回 true 以表示事件已处理
            }
        });
    }

    // 获取固件版本
    public String getFirmwareVersion() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }
        String FV = System.getProperty("os.version");
        return FV;
    }

    public void setFirmwareVersionCode(int firmwareVersionCode){
        this.firmwareVersionCode = firmwareVersionCode;
    }

    // 获取固件版本号
    public int getFirmwareVersionCode() {
        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasPermission("android.permission.READ_PHONE_STATE")) {
            requestPermission(mActivity,"android.permission.READ_PHONE_STATE",DEVICEINFOUTIL_PERMISSION_REQUIRECODE);
        }

        // 这里返回一个固件版本号
        return firmwareVersionCode;
    }

    //测试输出各种信息
    public String getFirmwareInfo() {
        StringBuilder info = new StringBuilder();

        // Android 版本
        info.append("Android Version: ").append(Build.VERSION.RELEASE).append("\n");

        // 内核版本
        info.append("Kernel Version: ").append(System.getProperty("os.version")).append("\n");

        // 基带版本
        info.append("Baseband Version: ").append(getSystemProperty("gsm.version.baseband")).append("\n");

        // 构建号
        info.append("Build Number: ").append(Build.DISPLAY).append("\n");

        // 引导加载程序版本
        info.append("Bootloader Version: ").append(Build.BOOTLOADER).append("\n");

        // 系统版本
        info.append("System Version: ").append(Build.ID).append("\n");

        // OEM 版本
        info.append("OEM Version: ").append(getSystemProperty("ro.oem.version")).append("\n");

        return info.toString();
    }

    // 获取SystemProperties中的属性
    public static String getSystemProperty(String key) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class);
            return (String) method.invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询是否具有权限
    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //动态请求权限
    public void requestPermission(Activity activity, String permission, int requestCode) {
        if (!hasPermission(permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }
}

