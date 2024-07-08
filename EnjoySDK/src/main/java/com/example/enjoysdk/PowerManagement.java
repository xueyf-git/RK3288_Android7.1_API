package com.example.enjoysdk;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

public class PowerManagement {
    private final Activity mActivity;
    private boolean lowBatteryWarning = true;
    private final LowBatteryReceiver lowBatteryReceiver;
    private static final int RQ_WRITE_SETTINGS = 1;

    public PowerManagement(Activity mActivity) {
        this.mActivity = mActivity;
        this.lowBatteryReceiver = new LowBatteryReceiver();
    }

    // 重启设备
    public int reboot() {
        if (mActivity == null) {                                                                    //相关服务未启动

            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return -1;                                                                              // 返回一个特定的值，表示权限未授予并已请求
        }

        try {
            // 执行重启命令
            Process process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "reboot"});
            process.waitFor();
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (IOException e) {
            Log.e("EnjoySDK", "Reboot failed due to IOException", e);
            return McErrorCode.ENJOY_COMMON_ERROR_WRITE_SETTINGS_ERROR;
        } catch (InterruptedException e) {
            Log.e("EnjoySDK", "Reboot failed due to InterruptedException", e);
            return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
        } catch (Exception e) {
            Log.e("EnjoySDK", "Reboot failed due to unknown error", e);
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    // 获取设备电量
    public int getBatteryLevel() {
        if (mActivity == null) {                                                                    //相关服务未启动

            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return -1;                                                                              // 返回一个特定的值，表示权限未授予并已请求
        }

        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = mActivity.registerReceiver(null, ifilter);

            if (batteryStatus != null) {
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                if (level != -1 && scale != -1) {
                    return (int)((level / (float) scale) * 100);
                }
            }
        } catch (Exception e) {
            Log.e("EnjoySDK", "Failed to get battery level", e);
        }

        return -1; // 返回-1表示获取电量失败
    }

    // 获取设备充电状态
    public String getChargingStatus() {
        if (mActivity == null) {                                                                    //相关服务未启动

            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();                                                                              // 返回一个特定的值，表示权限未授予并已请求
        }

        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = mActivity.registerReceiver(null, ifilter);

            if (batteryStatus != null) {
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

                String chargingType;
                switch (chargePlug) {
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        chargingType = "USB Charging";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        chargingType = "AC Charging";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        chargingType = "Wireless Charging";
                        break;
                    default:
                        chargingType = "Not Charging";
                        break;
                }

                if (isCharging) {
                    return "Charging (" + chargingType + ")";
                } else {
                    return "Not Charging";
                }
            }
        } catch (Exception e) {
            Log.e("EnjoySDK", "Failed to get charging status", e);
        }

        return "Unknown";
    }

    // 获取是否进行低电量警告
    public boolean getIsLowBatteryWarning(){
        return lowBatteryWarning;
    }

    // 设置设备是否进行低电量警告
    public void isLowBatteryWarning(boolean isLBW){
        this.lowBatteryWarning = isLBW;
    }

    // 注册低电量广播接收器
    public void registerLowBatteryReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        mActivity.registerReceiver(lowBatteryReceiver, filter);
    }

    // 注销低电量广播接收器
    public void unregisterLowBatteryReceiver() {
        try {
            mActivity.unregisterReceiver(lowBatteryReceiver);
        } catch (IllegalArgumentException e) {
            Log.e("EnjoySDK", "LowBatteryReceiver was not registered or already unregistered");
        }
    }

    /**
     * 控制屏幕亮度的方法
     * @param
     * @return 是否具有权限
     */
    // 控制屏幕亮度
    public void setScreenBrightness(int brightness) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 如果当前平台版本大于23平台
            if (!Settings.System.canWrite(mActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                mActivity.startActivity(intent);
            } else {
                changeSystemBrightness(brightness);
            }
        } else {
            // Android 6.0以下的系统则直接修改亮度
            changeSystemBrightness(brightness);
        }
    }

    // 修改系统亮度方法
    private void changeSystemBrightness(int brightness) {
        try {
            if (brightness < 0) brightness = 0;
            if (brightness > 255) brightness = 255;
            Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        } catch (Exception e) {
            Log.e("EnjoySDK", "Failed to change screen brightness", e);
        }
    }

    // 在Activity中处理权限请求结果的方法
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(mActivity)) {
                // 调用实际的亮度修改方法
                changeSystemBrightness(128);
            } else {
                // 用户未授予权限，可以在这里处理
                Log.e("EnjoySDK", "Permission to write settings was not granted");
            }
        }
    }

    //设备关机，未完成
    public int shutdown(boolean confirm) {
        if (confirm) {
            showShutdownConfirmationDialog();
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } else {
            return executeShutdown();
        }
    }

    //设备关机确认弹窗
    public void showShutdownConfirmationDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("确认关机")
                .setMessage("您确定要关机吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int result = executeShutdown();
                        if (result != McErrorCode.ENJOY_COMMON_SUCCESSFUL) {
                            Log.e("EnjoySDK", "Shutdown failed with error code: " + result);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    //设备关机操作，未完成
    public int executeShutdown() {
        try {
            // 使用 "poweroff" 命令执行关机操作
            Process process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "poweroff"});
            process.waitFor();
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (IOException e) {
            Log.e("EnjoySDK", "Shutdown failed due to IOException with poweroff", e);
            return McErrorCode.ENJOY_COMMON_ERROR_WRITE_SETTINGS_ERROR;
        } catch (InterruptedException e) {
            Log.e("EnjoySDK", "Shutdown failed due to InterruptedException with poweroff", e);
            return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
        } catch (Exception e) {
            Log.e("EnjoySDK", "Shutdown failed due to unknown error with poweroff", e);
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }


    // 内部类：低电量广播接收器
    private class LowBatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction()) && lowBatteryWarning) {
                // 弹出警告对话框
                new AlertDialog.Builder(context)
                        .setTitle("低电量警告")
                        .setMessage("您的设备电量过低，请及时充电！")
                        .setPositiveButton("确定", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    /* ------------------------------------------------------- */
    /*             以下为系统权限相关的方法                  */
    /* ------------------------------------------------------- */

    /**
     * 判断是否具有写设置权限的方法
     * @param context 上下文
     * @return 是否具有权限
     */
    private boolean hasWriteSettingsPermission(Context context) {
        return Settings.System.canWrite(context);
    }

    /**
     * 请求写设置权限的方法
     */
    private void requestWriteSettingsPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }


}
