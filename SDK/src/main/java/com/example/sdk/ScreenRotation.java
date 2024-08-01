package com.example.sdk;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScreenRotation {
    private final Activity activity;
    private static final String TAG = "ScreenRotation";

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
        String command;
        executeRootCommand("settings put system accelerometer_rotation 0");
        switch (rotation) {
            case 0:
                command = "settings put system user_rotation 0";
                break;
            case 90:
                command = "settings put system user_rotation 1";
                break;
            case 180:
                command = "settings put system user_rotation 2";
                break;
            case 270:
                command = "settings put system user_rotation 3";
                break;
            default:
                Log.e(TAG, "Incorrect rotation input!");
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }

        return executeRootCommand(command) ? McErrorCode.ENJOY_COMMON_SUCCESSFUL : McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
    }

    /**
     * 获取当前屏幕方向
     *
     * @return 当前屏幕方向
     */
    public int getSystemRotation() {
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("settings get system user_rotation");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            int rotation_code = Integer.parseInt(line);
            int rotation;
            switch (rotation_code){
                case 0:
                    rotation = 0;
                    break;
                case 1:
                    rotation = 90;
                    break;
                case 2:
                    rotation = 180;
                    break;
                case 3:
                    rotation = 270;
                    break;
                default:
                    Log.e(TAG,"Error get system rotation!");
                    return -1;
            }
            return rotation;
        } catch (IOException | NumberFormatException e) {
            Log.e(TAG, "Failed to get current rotation", e);
            return -1;
        } finally {
            closeResources(reader);
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 设置副屏幕方向
     *
     * @return 当前屏幕方向
     */
    public int setViceRotation(int rotation){

        return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
    }

    /**
     * 执行 root 命令
     *
     * @param command 要执行的命令
     * @return true 如果成功，否则 false
     */
    private boolean executeRootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            int exitValue = process.exitValue();
            if (exitValue == 0) {
                Log.i(TAG, "Command executed successfully: " + command);
                return true;
            } else {
                Log.e(TAG, "Command execution failed with exit code " + exitValue + ": " + command);
                printErrorStream(process);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, command + " failed!", e);
            return false;
        } finally {
            closeResources(os);
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void printErrorStream(Process process) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e(TAG, "Error: " + line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read error stream", e);
        } finally {
            closeResources(reader);
        }
    }

    private void closeResources(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                Log.e(TAG, "Failed to close resource", e);
            }
        }
    }
}
