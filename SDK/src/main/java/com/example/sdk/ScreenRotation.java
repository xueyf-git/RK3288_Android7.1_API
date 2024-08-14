package com.example.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

public class ScreenRotation {
    private final Activity activity;
    private static final String TAG = "ScreenRotation";
    private static final String HW_ROTATION_PROPERTY = "persist.sf.hwrotation";

    public ScreenRotation(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        this.activity = activity;
    }

    /**
     * 设置主屏幕系统方向
     *
     * @param rotation 要设置的旋转角度（0, 90, 180, 270）
     * @return 结果代码
     */
    public int setSystemRotation(int rotation) {
        String rotationValue;
        switch (rotation) {
            case 0:
                rotationValue = "0";
                break;
            case 90:
                rotationValue = "90";
                break;
            case 180:
                rotationValue = "180";
                break;
            case 270:
                rotationValue = "270";
                break;
            default:
                throw new IllegalArgumentException("Invalid rotation angle. Use 0, 90, 180, or 270.");
        }

        // 设置系统属性以更新屏幕方向
        SystemProperties.set(HW_ROTATION_PROPERTY, rotationValue);
        Log.i(TAG, "Screen rotation set to " + rotation + " degrees");

        // 重启设备以应用更改
        rebootDevice();

        return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
    }

    /**
     * 获取当前屏幕方向
     *
     * @return 当前屏幕方向
     */
    public int getSystemRotation() {
        String rotationValue = SystemProperties.get(HW_ROTATION_PROPERTY, "0");
        int rotation;
        switch (rotationValue) {
            case "0":
                rotation = 0;
                break;
            case "90":
                rotation = 90;
                break;
            case "180":
                rotation = 180;
                break;
            case "270":
                rotation = 270;
                break;
            default:
                Log.e(TAG, "Error getting system rotation!");
                return -1;
        }
        return rotation;
    }

    /**
     * 重启设备以应用更改
     */
    private void rebootDevice() {
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("startTime", 1);
        activity.sendBroadcast(intent);
    }

}