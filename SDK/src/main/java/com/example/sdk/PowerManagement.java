package com.example.sdk;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private static final String TOUCH_WAKE_PROP = "persist.sys.touch_wake";
    private PowerManager powerManager;
    private int timeToSleep = 60000;
    private AlarmManager alarmManager;
    private List<PendingIntent> pendingIntents;
    private static final String TAG = "AlarmReceiver";




    public PowerManagement(Activity mActivity) {
        this.mActivity = mActivity;
        this.lowBatteryReceiver = new LowBatteryReceiver();
        devicePolicyManager = (DevicePolicyManager) mActivity.getSystemService(mActivity.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(mActivity,DeviceAdminReceiver.class);
        powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
        this.pendingIntents = new ArrayList<>();

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

    // 使用root权限设置系统屏幕超时时间
    public void setScreenAlwaysOn() {
        executeRootCommand("settings put system screen_off_timeout 2147483647"); // 2147483647 是 Integer.MAX_VALUE
        executeRootCommand("setprop poweroff.disabled 1");
    }

    // 使用root权限恢复系统屏幕超时时间
    public void restoreScreenTimeout() {
        executeRootCommand("settings put system screen_off_timeout "+timeToSleep); // 恢复为 1 分钟
        executeRootCommand("setprop poweroff.disabled 0");
    }

    // Set the screen off timeout
    // 设置屏幕自动熄灭的最大时间
    public void setScreenOffTimeout(int timeInMs) {
        this.timeToSleep = timeInMs;
        Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeToSleep);
    }

    //使设备休眠
    int goToSleep() {
        try {
            // 使用反射调用 goToSleep 方法
            Method goToSleepMethod = powerManager.getClass().getMethod("goToSleep", long.class);
            goToSleepMethod.invoke(powerManager, SystemClock.uptimeMillis());
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            e.printStackTrace();
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    //使设备唤醒
    public int wakeup() {
        try {
            // 使用反射调用 wakeUp 方法
            Method wakeUpMethod = powerManager.getClass().getMethod("wakeUp", long.class);
            wakeUpMethod.invoke(powerManager, SystemClock.uptimeMillis());
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            e.printStackTrace();
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }



    //设置触摸唤醒
    /**
     * 设置触摸唤醒功能
     * @param enable true 打开触摸唤醒, false 关闭触摸唤醒
     * @return 操作结果 0 表示成功, -1 表示失败
     */
    public int setTouchWake(boolean enable) {
        try {
            SystemProperties.set(TOUCH_WAKE_PROP, enable ? "1" : "0");

            // 处理屏幕状态
            if (!enable) {
                // 如果禁用触摸唤醒，确保设备锁屏
                if (wakeLock == null) {
                    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::TouchWakeLock");
                }
                wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/); // 确保设备在后台运行
                goToSleep();
                wakeLock.release();
            } else {
                // 如果启用触摸唤醒，确保设备唤醒
                if (wakeLock == null) {
                    wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyApp::TouchWakeLock");
                }
                wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/); // 确保设备唤醒
                wakeLock.release();
            }

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取当前触摸唤醒状态
     * @return 1 表示开启, 0 表示关闭, -1 表示读取失败
     */
    public int getTouchWakeState() {
        try {
            String value = SystemProperties.get(TOUCH_WAKE_PROP, "0");
            return "1".equals(value) ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int setTimedTouchWake(boolean enable) {
        // Here you should add the logic to enable/disable timed touch wake
        // This is a placeholder implementation
        this.timedTouchWakeState = enable;
        return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
    }

    //设置定时唤醒计划
    public int addScheduleToTouchWake(String start, String end, boolean enable) {
        if(timedTouchWakeState){
            try {
                Calendar startCal = getCalendarFromTime(start);
                Calendar endCal = getCalendarFromTime(end);

                int startRequestCode = (int)(startCal.getTimeInMillis()/ 1000);
                int endRequestCode = (int)(endCal.getTimeInMillis()/ 1000);
                setAlarm(startCal, "START", enable,startRequestCode);
                setAlarm(endCal, "END", enable,endRequestCode);
                return McErrorCode.ENJOY_COMMON_SUCCESSFUL; // Success
            } catch (Exception e) {
                e.printStackTrace();
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN; // Error
            }
        }else{
            System.out.println("当前服务未启动！");
            return McErrorCode.ENJOY_COMMON_ERROR_SDK_NOT_SUPPORT;
        }

    }

    //取消定时唤醒计划
    public int deleteScheduleToTouchWake(String start, String end, boolean enable) {
        try {
            Calendar startCal = getCalendarFromTime(start);
            Calendar endCal = getCalendarFromTime(end);
            cancelAlarm(startCal, "START", enable);
            cancelAlarm(endCal, "END", enable);
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN; // Error
        }
    }

    //取消所有定时唤醒计划
    public int cancleTimedTouchWake() {
        try {
            for (PendingIntent pendingIntent : pendingIntents) {
                alarmManager.cancel(pendingIntent);
            }
            pendingIntents.clear();
            return 0; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    //获取定时触摸唤醒计划开启状态
    int getTimedTouchWakeState(){
        return timedTouchWakeState?McErrorCode.ENJOY_COMMON_SUCCESSFUL:McErrorCode.ENJOY_COMMON_ERROR_SDK_NOT_SUPPORT;
    }

    //将字符串类型转换为calendar类型
    private Calendar getCalendarFromTime(String time) throws Exception {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    //在alarmManager中设置alarm
    private void setAlarm(Calendar calendar, String type, boolean enable,int requestCode) {
        Log.d(TAG, "Setting alarm for type: " + type + " at time: " + calendar.getTime());

        Intent intent = new Intent(mActivity, AlarmReceiver.class);
        intent.setAction("com.example.apiDemo.ALARM_RECEIVER");
        intent.putExtra("type", type);
        intent.putExtra("enable", enable);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d(TAG, "Alarm set with setExactAndAllowWhileIdle for type: " + type);

            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d(TAG, "Alarm set with setExact for type: " + type);

            }
            pendingIntents.add(pendingIntent); // Track the pending intent
            Log.d(TAG, "Alarm set successfully.");
        } else {
            Log.e(TAG, "Failed to set alarm: AlarmManager is null.");
        }
    }

    //取消alarm
    private void cancelAlarm(Calendar calendar, String type, boolean enable) {
        Intent intent = new Intent(mActivity, AlarmReceiver.class);
        intent.setAction("com.example.apiDemo.ALARM_RECEIVER");
        intent.putExtra("type", type);
        intent.putExtra("enable", enable);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, type.equals("START") ? 0 : 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntents.remove(pendingIntent); // Remove the pending intent from tracking
            Log.d(TAG, "Alarm cancelled successfully.");
        } else {
            Log.e(TAG, "Failed to cancel alarm: AlarmManager is null.");
        }
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

    //定时触摸唤醒方法内部类
    public static class AlarmReceiver extends BroadcastReceiver {

        private static PowerManager.WakeLock screenWakeLock;
        public AlarmReceiver() {
            // Default constructor
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "AlarmReceiver triggered");

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            String type = intent.getStringExtra("type");
            boolean enable = intent.getBooleanExtra("enable", false);

            Log.d(TAG, "Type: " + type + ", Enable: " + enable);

            if ("START".equals(type)) {
                // 获取 PARTIAL_WAKE_LOCK 和 FULL_WAKE_LOCK
                if (screenWakeLock == null) {
                    screenWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "apiDemo:SCREEN_WAKE_LOCK");
                    screenWakeLock.acquire();
                    Log.d(TAG, "ScreenWakeLock acquired at START");
                }
            } else if ("END".equals(type)) {
                // 释放 FULL_WAKE_LOCK
                if (screenWakeLock != null && screenWakeLock.isHeld()) {
                    screenWakeLock.release();
                    screenWakeLock = null;
                    Log.d(TAG, "ScreenWakeLock released at END");
                }
            }

            // 释放 PARTIAL_WAKE_LOCK
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "apiDemo:WAKE_LOCK");
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
            wakeLock.release();
            Log.d(TAG, "WakeLock released");
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
