package com.example.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LogDumper implements Runnable {
    private Thread logThread = null;
    private boolean isRunning = false;                                                              //判断是否运行的标志
    String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());    //获取当前日期以生成log的文件名
    private final String log_FileName = "log_"+timeStamp+".txt";                                    // 日志文件名称
    private final String dmesg_FileName = "dmesg_"+timeStamp+".txt";
    private final Activity mActivity;
    Timer timer;
    long std_period = 3600000;                                                                      //基准停止时间，就是1小时
    int hour = 48;                                                                                  //默认自动关闭时间，48小时
    long period;                                                                                    //实际关闭时间

    public LogDumper(Activity activity) {     //默认生成方法
        this.mActivity = activity;
    }

    /**
     * 启用/禁止日志记录
     */
    public int enableLogRecorder(boolean enable){
        if (mActivity == null) {                                                                    //相关服务未启动

            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
        requestWriteSettingsPermission();
            return -1;                                                                              // 返回一个特定的值，表示权限未授予并已请求
        }

        try {
            if (enable) {
                start();
            } else {
                stop();
            }
            return 0;                                                                               // 成功

        } catch (Exception e) {
            Log.e("EnableLogRecorder", "Error while toggling log recorder", e);
            return -1701;                                                                           // 相关服务未启动
        }
    }

    /**
     * 获取日志记录工作时间
     */
    public int getRecorderTime(){
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return -2;
        }

        try{
            return hour;

        }catch (Exception e){
            Log.e("isLogRecorderEnabled", "Error checking recorder time", e);
            throw new IllegalStateException("Error while checking recorder time.", e);        //暂时没想好怎么抛出异常
        }
    }

    /**
     * 获取日志记录工作状态
     */
    public boolean isLogRecorderEnabled(){
        if(mActivity == null){
            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return false;
        }

        try{
            return isRunning;

        }catch (Exception e){
            Log.e("isLogRecorderEnabled", "Error checking log recorder status", e);
            throw new IllegalStateException("Error while checking log recorder status.", e);        //暂时没想好怎么抛出异常
        }
    }                                                                                               //返回日志记录运行状态

    /**
     * 设置日志记录工作时间
     */
    public int setRecorderTime(int hour){
        if (mActivity == null) {
            throw new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
            return -2;
        }

        try{
            if(hour<=0){
                throw new IOException("不允许设置非正数!");
            }
            this.hour =hour;
            return 0;

        }catch (Exception e){
            return -1;
        }
    }                                                                                               //返回设置的运行时间

    /**
     * 导出日志记录
     */
    public String exportLog(){
        String filepath_ep = Environment.getExternalStorageDirectory()+"/";                         //获取外部存储的文件路径，一般为/sdcard
        String tsp = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault()).format(new Date());//获取时间戳以生成日志文件的文件名
        String logcatPath;
        String dmesgPath;

        if(mActivity == null){
            throw  new IllegalStateException("No activity provided.");
        }

        if (!hasWriteSettingsPermission(mActivity)) {
            requestWriteSettingsPermission();
        }
        try{
            //创建文件夹
            File dir = createDir(filepath_ep, "McLog");
            File file_log = createFile(dir, tsp + "-log.txt");
            File file_dmesg = createFile(dir, tsp + "-dmesg.txt");

            copyFile(mActivity.getExternalFilesDir(null) + "/" + log_FileName, file_log.getPath());            //以下代码主要实现将应用内的日志文件拷贝至目标路径；
            copyFile(mActivity.getExternalFilesDir(null) + "/" + dmesg_FileName, file_dmesg.getPath());        //当日志记录未启用时，不能输出最近的日志记录，只能输出日志记录关闭前的所有日志

            logcatPath = filepath_ep+tsp+"-log.txt";
            dmesgPath = filepath_ep+tsp+"-dmesg.txt";

            return "Logcat导出路径："+logcatPath+"\nDmesg导出路径："+dmesgPath;

        }catch (Exception e){
            Log.e("LogDumper", "Error in LogDumper: " + e.getMessage());
            throw new IllegalStateException("Error while export logrecorder.", e);
        }
    }

    /**
     * 设置日志记录工作状态为启用
     */
    public void start() {                                                                           //日志记录功能开启
        if (isRunning) return;
        isRunning = true;
        logThread = new Thread(this);
        logThread.start();
    }

    /**
     * 设置日志记录工作状态为禁用
     */
    public void stop() {                                                                            //日志记录功能结束
        if (!isRunning) return;
        isRunning = false;
        if (logThread != null) {
            logThread.interrupt();
            logThread = null;
        }
    }

    /**
     * 日志记录主体
     * 复制生成程序内部的记录文件
     */
    @Override
    public void run() {
        try {
            if(isRunning)start_Timer();
            File log_File = createFile(mActivity.getExternalFilesDir(null), log_FileName);     //创建日志文件
            File dmesg_File = createFile(mActivity.getExternalFilesDir(null), dmesg_FileName);

            try(FileWriter log_writer = new FileWriter(log_File, true);
                FileWriter dmesg_writer = new FileWriter(dmesg_File,true);
                InputStreamReader log_Reader = new InputStreamReader(Runtime.getRuntime().exec("logcat -v time").getInputStream());     //从外部程序读取日志，并通过streamReader读取字符流
                InputStreamReader dmesg_Reader = new InputStreamReader(Runtime.getRuntime().exec("dmesg").getInputStream())){

                char[] log_Buffer = new char[1024];
                char[] dmesg_Buffer = new char[1024];
                int log_BytesRead;
                int dmesg_BytesRead;

                while (isRunning) {                                                                 //将读出的日志字符流写入指定文件
                    log_BytesRead = log_Reader.read(log_Buffer);
                    dmesg_BytesRead = dmesg_Reader.read(dmesg_Buffer);

                    if (log_BytesRead > 0) {
                        log_writer.write(log_Buffer, 0, log_BytesRead);
                        log_writer.flush();
                    }

                    if (dmesg_BytesRead > 0) {
                        dmesg_writer.write(dmesg_Buffer, 0, dmesg_BytesRead);
                        dmesg_writer.flush();
                    }

                }
            }

        } catch (Exception e) {
            Log.e("LogDumper", "Error in LogDumper: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------- */
    /*             以下为文件操作模块                 */
    /* ------------------------------------------------------- */

    /**
     * 创建文件夹
     */
    private File createDir(String parent, String child) {                                           //创建文件夹
        File dir = new File(parent, child);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e("LogDumper", "Directory creation failed: " + dir.getPath());
            }
        }
        return dir;
    }

    /**
     * 创建文件
     */
    private File createFile(File dir, String fileName) throws IOException {                         //创建文件
        File file = new File(dir, fileName);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                Log.e("LogDumper", "File creation failed: " + file.getPath());
            }
        }
        return file;
    }

    /**
     * 复制文件至指定路径
     */
    private void copyFile(String srcPath, String destPath) {                                        //复制文件
        try (InputStream inStream = new FileInputStream(srcPath);
             OutputStream outStream = new FileOutputStream(destPath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e("LogDumper", "Error copying file: " + e.getMessage());
        }
    }

/* ------------------------------------------------------- */
/*             以下为定时模块                 */
/* ------------------------------------------------------- */

    /**
     * 创建定时器
     */
    void start_Timer(){
        period = std_period*hour;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                                                                                                    // 在这里执行定时任务
                stop_time();

            }

        }, period);
    }

    /**
     * 定时器任务
     * 此处为禁用日志记录并关闭定时器
     */
    void stop_time( ){
        System.out.println("Log recorder is shutting down!");
        stop();
        timer.cancel();
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