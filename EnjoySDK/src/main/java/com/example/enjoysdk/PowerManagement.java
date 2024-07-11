package com.example.enjoysdk;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
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

import java.io.IOException;

public class PowerManagement {
    private final Activity mActivity;
    private boolean lowBatteryWarning = true;                                                       //是否进行低电量警告标志
    private final LowBatteryReceiver lowBatteryReceiver;
    private static final int RQ_WRITE_SETTINGS = 1;
    private PowerManager.WakeLock wakeLock;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

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

    //系统休眠
    //该部分操作在manifest中添加了一个<receiver>,声明了一个设备管理员接收器，并指定了所需的权限和配置文件。这使得你的应用能够注册和接收设备管理员相关的事件;
    //该部分操作在/res/xml中添加了一个device_admin_sample.xml,定义了设备管理员的配置，包括该管理员能够执行的策略和操作。这使得你的应用能够使用这些策略来管理设备的安全和电源状态;

    // 获取一个WakeLock以阻止系统休眠，需求中只提到需要阻止屏幕熄灭，但是调查得知即使屏幕不熄灭，系统也可能进入休眠状态。所以留下这个方法。
    // 方法powerManager.newWakeLock(int levelAndFlags,String tag)中,int levelAndFlags表示锁的类型，此处"PARTIAL_WAKE_LOCK"为使CPU处于运行状态，屏幕和键盘背光可以熄灭;
    // String tag 表示锁的名字;
    public void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "PowerManagement::WakeLockTag"
            );
            wakeLock.acquire();                                                                     //void acquire() 持锁（就是启用该锁）。也有void acquire(long timeout)方法，可以持锁并在timeout毫秒后自动释放;
        }
    }

    // 释放WakeLock
    public void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {                                                //如果锁存在并且锁的状态为true（isHeld()函数返回true表示正被锁持）;
            wakeLock.release();                                                                     //void release() 释放锁;
        }
    }

    // Lock the device immediately
    public void lockNow() {
        if (devicePolicyManager.isAdminActive(componentName)) {                                     //boolean isAdminActive(ComponentName componentName)方法在设备管理员被激活时返回true;
            devicePolicyManager.lockNow();                                                          //lockNow()方法（在设备管理员激活时）会立刻锁屏，相当于按下电源键;
        } else {
            // Prompt user to activate device admin
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);                //如果设备管理员未激活，则启动一个Intent,引导用户前往设备管理员激活界面;
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);                 //ComponentName是应用程序组件的唯一标识，用于系统在运行时定位组件;
            mActivity.startActivity(intent);
        }
    }

    // Set the maximum time to lock the device
    // 设置系统自动进入休眠前的最大时间（需要设备管理员）;
    public void setMaximumTimeToLock(long timeInMs) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, timeInMs);
        }
    }

    // 启用屏幕常亮
    public void enableScreenOn() {
        if (mActivity != null) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            throw new IllegalStateException("No activity provided.");
        }
    }

    // 禁用屏幕常亮
    public void disableScreenOn() {
        if (mActivity != null) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            throw new IllegalStateException("No activity provided.");
        }
    }

    // Set the screen off timeout
    // 设置屏幕自动熄灭的最大时间
    public void setScreenOffTimeout(int timeInMs) {
        Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeInMs);
    }



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


}
