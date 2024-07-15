package com.example.sdk;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.DataOutputStream;
import java.io.IOException;

public class PowerManagement {
    private final Activity mActivity;
    private boolean lowBatteryWarning = true;                                                       //是否进行低电量警告标志
    private final LowBatteryReceiver lowBatteryReceiver;
    private static final int RQ_WRITE_SETTINGS = 1;
    private PowerManager.WakeLock wakeLock;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private boolean touchWakeState = false;
    private boolean timedTouchWakeState = false;

    public PowerManagement(Activity mActivity) {
        this.mActivity = mActivity;
        this.lowBatteryReceiver = new LowBatteryReceiver();
        devicePolicyManager = (DevicePolicyManager) mActivity.getSystemService(mActivity.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(mActivity,DeviceAdminReceiver.class);
    }

    // 重启设备
    public int reboot() {
        if (mActivity == null) {                                                                    //相关服务未启动

            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return McErrorCode.ENJOY_COMMON_ERROR_WRITE_SETTINGS_ERROR;                                                                              // 返回一个特定的值，表示权限未授予并已请求
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
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);                 //过滤器，过滤系统电量变化的广播ACTION_BATTERY_CHANGED;
            Intent batteryStatus = mActivity.registerReceiver(null, ifilter);               //注册Receiver

            if (batteryStatus != null) {
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);   //获取当前电量
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);   //获取电池容量

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
            requestWriteSettingsPermission();                                                       // 返回一个特定的值，表示权限未授予并已请求
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

    //设备关机
    //confirm为true时将显示确认弹窗，为false时将直接关机
    //部分设备（比如我的测试设备）在通电的情况下关机也会立刻开机，导致看起来像reboot一样,所以我在虚拟机的terminal中测试了关机命令，确认了这是关shutdown而不是reboot,虽然命令确实是reboot来着;
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
        new AlertDialog.Builder(mActivity)                                                          //android内置的对话框
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

    //设备关机操作
    public int executeShutdown() {
        try {
            // 使用 "reboot -p" 命令执行关机操作
            Process process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "reboot -p"});     //Runtime.getRuntime()方法获取当前应用的运行时环境，exec()方法运行指定的系统命令;
                                                                                                                //"/system/bin/sh"指定要使用的shell解释器，"-c"指定要执行的命令，"reboot "为重启设备，"-p"为关闭电源;
            process.waitFor();                                                                      //使当前线程等待，直到由 exec 方法启动的进程终止;
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (IOException e) {                                                                   //处理IO异常
            Log.e("EnjoySDK", "Shutdown failed due to IOException with reboot -p", e);
            return McErrorCode.ENJOY_COMMON_ERROR_WRITE_SETTINGS_ERROR;
        } catch (InterruptedException e) {                                                          //处理线程中断异常
            Log.e("EnjoySDK", "Shutdown failed due to InterruptedException with reboot -p", e);
            return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
        } catch (Exception e) {
            Log.e("EnjoySDK", "Shutdown failed due to unknown error with reboot -p", e);
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    public void setMaximumTimeToLock(long timeInMs) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, timeInMs);
        }
    }

    // 使用root权限设置系统屏幕超时时间
    public void setScreenAlwaysOn() {
        executeRootCommand("settings put system screen_off_timeout 2147483647"); // 2147483647 是 Integer.MAX_VALUE
        executeRootCommand("setprop poweroff.disabled 1");
    }

    // 使用root权限恢复系统屏幕超时时间
    public void restoreScreenTimeout() {
        executeRootCommand("settings put system screen_off_timeout 60000"); // 恢复为 1 分钟
        executeRootCommand("setprop poweroff.disabled 0");
    }

    // Set the screen off timeout
    // 设置屏幕自动熄灭的最大时间
    public void setScreenOffTimeout(int timeInMs) {
        Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeInMs);
    }

    //设置触摸唤醒
    public int setTouchWake(boolean enable) {
        //当定时唤醒开启时，无法启动触摸唤醒
        this.touchWakeState = enable;
        if(timedTouchWakeState){
            touchWakeState = false;
            return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
        }
        if(!touchWakeState){
            return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
        }
        else {
            enableTouchWake();
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        }

    }

    public void enableTouchWake(){
        PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::TouchWakeLock");
        wakeLock.acquire();
        // 释放唤醒锁，因为这里我们只需要一次性唤醒操作
        wakeLock.release();
    }

    //获取触摸唤醒状态
    public int getTouchWakeState(){
        if(touchWakeState)return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        else return McErrorCode.ENJOY_COMMON_ERROR_SERVICE_NOT_START;
    }




    /* ------------------------------------------------------- */
    /*             以下为内部类                  */
    /* ------------------------------------------------------- */

    // 内部类：低电量广播接收器
    private class LowBatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction()) && lowBatteryWarning) {        //当接收到系统ACTION_BATTERY_LOW的广播并且低电量警告标志lowBatteryWarning为true;
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

    //内部类：设备管理员接收器，处理设备管理员状态变化事件。
    public static class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {

        @Override
        public void onEnabled(Context context, Intent intent) {
            Toast.makeText(context, "Device Admin Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisabled(Context context, Intent intent) {
            Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show();
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


    // 执行root命令的通用方法
    private void executeRootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            Log.e("PowerManagement", "Failed to execute root command", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                Log.e("PowerManagement", "Failed to close resources", e);
            }
        }
    }

}
