package com.example.sdk;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BootAnimation {
    private final Activity mActivity;
    private String targetPath = "/system/media/bootanimation.zip";
    private String systemMountPoint = "/dev/block/platform/ff0f0000.dwmmc/by-name/system";

    public BootAnimation(Activity activity) {
        this.mActivity = activity;
    }

    // 设置开机动画
    public int setBootAnimation(String bootAnimationPath) {
        File sourceFile = new File(bootAnimationPath);
        Log.d("BootAnimation", "sourceFilePath:" + bootAnimationPath);

        if (!sourceFile.exists() || !sourceFile.isFile()) {
            Log.e("BootAnimation", "Source file does not exist or is not a file.");
            return McErrorCode.ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_NOT_EXIST;
        }

        try {
            // 强制重新挂载 /system 为读写
            if (!remountSystemRW()) {
                Log.e("BootAnimation", "/system remount as read-write failed.");
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
            }

            // 确保目标路径存在并设置权限
            executeRootCommand("mkdir -p /system/media");
            executeRootCommand("chmod 755 /system/media");

            // 复制目标文件到指定路径
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                executeRootCommand("cp " + sourceFile.getAbsolutePath() + " " + targetPath);
            } else {
                copyFileUsingStream(sourceFile, new File(targetPath));
            }

            // 设置正确的权限与所有者
            executeRootCommand("chmod 644 " + targetPath);
            executeRootCommand("chown root:root " + targetPath);

            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;

        } catch (IOException e) {
            Log.e("BootAnimation", "Error setting boot animation", e);
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    // 重置开机动画
    public int resetBootAnimation() {
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            Log.e("BootAnimation", "BootAnimation is already default version.");
            return McErrorCode.ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_NOT_EXIST;
        }

        try {
            // 强制重新挂载 /system 为读写
            if (!remountSystemRW()) {
                Log.e("BootAnimation", "/system remount as read-write failed.");
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
            }

            // 删除开机动画文件
            boolean success = executeRootCommand("rm " + targetPath);

            if (success && !targetFile.exists()) {
                Log.i("BootAnimation", "BootAnimation reset succeed!");
                return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
            } else {
                Log.e("BootAnimation", "Failed to reset bootAnimation! File still exists.");
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
            }
        } catch (Exception e) {
            Log.e("BootAnimation", "Error resetting boot animation", e);
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    // 强制重新挂载 /system 为读写
    private boolean remountSystemRW() {
        boolean isMounted = executeRootCommand("mount | grep '/system' | grep 'rw,'");
        if (isMounted) {
            Log.i("BootAnimation", "/system is already mounted as read-write.");
            return true;
        } else {
            return executeRootCommand("busybox mount -o rw,remount " + systemMountPoint + " /system");
//            return executeRootCommand("mount -o rw,remount /system");
        }
    }

    /**
     * 使用 FileInputStream 和 FileOutputStream 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     * @throws IOException 如果发生 I/O 错误
     */
    private void copyFileUsingStream(File source, File dest) throws IOException {
        executeRootCommand("cp " + source.getAbsolutePath() + " " + dest.getAbsolutePath());
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
                Log.i("BootAnimation", "Command executed successfully: " + command);
                return true;
            } else {
                Log.e("BootAnimation", "Command execution failed with exit code " + exitValue + ": " + command);
                printErrorStream(process);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Log.e("BootAnimation", command + " failed!", e);
            return false;
        } finally {
            closeResources(os, process);
        }
    }

    private void printErrorStream(Process process) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("BootAnimation", "Error: " + line);
            }
        } catch (IOException e) {
            Log.e("BootAnimation", "Failed to read error stream", e);
        }
    }

    private void closeResources(DataOutputStream os, Process process) {
        try {
            if (os != null) {
                os.close();
            }
            if (process != null) {
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
