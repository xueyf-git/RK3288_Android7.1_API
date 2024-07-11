package com.example.enjoysdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

public class TimeUtil {
    private final Activity mActivity;

    public TimeUtil(Activity activity){
        this.mActivity = activity;
    }

    @SuppressLint("MissingPermission")
    public void setTime(int hour, int minute, int seconds){
        new Thread(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, seconds);
                calendar.set(Calendar.MILLISECOND, 0);

                long timeInMillis = calendar.getTimeInMillis();
                if (timeInMillis / 1000 < Integer.MAX_VALUE) {
                    AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.setTime(timeInMillis);
                        System.out.println("System time has been changed.");
                    } else {
                        System.out.println("Failed to get AlarmManager service.");
                    }
                } else {
                    System.out.println("The time value is out of range.");
                }
            } catch (Exception e) {
                System.out.println("Failed to set system time: " + e.getMessage());
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    public void setDate(int year, int month, int day){
        new Thread(() -> {
            try{
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR ,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                long timeInMillis = calendar.getTimeInMillis();

                if (timeInMillis / 1000 < Integer.MAX_VALUE) {
                    AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.setTime(timeInMillis);
                        System.out.println("System time has been changed.");
                    } else {
                        System.out.println("Failed to get AlarmManager service.");
                    }
                } else {
                    System.out.println("The time value is out of range.");
                }
            } catch (Exception e) {
                System.out.println("Failed to set system time: " + e.getMessage());
            }

        }).start();
    }

}
