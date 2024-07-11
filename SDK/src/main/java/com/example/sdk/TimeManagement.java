package com.example.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;


public class TimeManagement {
    private final Activity mActivity;
    public TimeManagement(Activity activity) { this.mActivity = activity; }

    /**
     * 根据布尔参数切换自动日期和时间
     * @param enable 时间配置参数（true 表示打开，false 表示关闭）
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int switchAutoDateAndTime(boolean enable) {
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided.");
        }

        if (enable) {
            if (hasWriteSettingsPermission(mActivity)) {
                openAutoTime();
                return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
            } else {
                requestWriteSettingsPermission();
            }
        } else {
            if (hasWriteSettingsPermission(mActivity)) {
                closeAutoTime();
                return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
            } else {
                requestWriteSettingsPermission();
            }
        }
        return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
    }

    /**
     * 打开自动确定日期和时间
     */
    public void openAutoTime() {
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided.");
        }

        if (hasWriteSettingsPermission(mActivity)) {
            Settings.Global.putInt(mActivity.getContentResolver(), Settings.Global.AUTO_TIME, 1);
            showToast("自动确定日期和时间已打开");
        } else {
            requestWriteSettingsPermission();
        }
    }

    /**
     * 关闭自动确定日期和时间
     */
    public void closeAutoTime() {
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided.");
        }

        if (hasWriteSettingsPermission(mActivity)) {
            Settings.Global.putInt(mActivity.getContentResolver(), Settings.Global.AUTO_TIME, 0);
            showToast("自动确定日期和时间已关闭");
        } else {
            requestWriteSettingsPermission();
        }
    }

    /**
     * 切换自动时间设置的方法
     */
    public void toggleAutoTime() {
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided. Make sure to initialize TimeSettingsManager with an activity.");
        }

        if (hasWriteSettingsPermission(mActivity)) {
            boolean isAutoTimeEnabled = isAutoTimeEnabled(mActivity);
            Settings.Global.putInt(mActivity.getContentResolver(), Settings.Global.AUTO_TIME, isAutoTimeEnabled? 0 : 1);
            showToast(isAutoTimeEnabled? "自动确定日期和时间已禁用" : "自动确定日期和时间已启用");
        } else {
            requestWriteSettingsPermission();
        }
    }

    /**
     * 判断自动时间是否启用的方法
     * @param context 上下文
     * @return 是否启用
     */
    private boolean isAutoTimeEnabled(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME) > 0;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

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

    /**
     * 显示短时间提示消息的方法
     * @param message 要显示的消息
     */
    private void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

}
