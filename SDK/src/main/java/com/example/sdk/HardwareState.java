package com.example.sdk;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HardwareState {
    private final Activity activity;

    public HardwareState(Activity activity){
        this.activity = activity;
    }

    // 获取CPU状态
    public String getCpuUsage() {
        try (RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r")) {
            String load = reader.readLine();
            String[] toks = load.split("\\s+");

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) +
                    Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);
//            Log.d("HardwareState", "idle1=" + idle1 + "\ncpu1=" + cpu1);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            reader.seek(0);
            load = reader.readLine();

            toks = load.split("\\s+");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) +
                    Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);
//            Log.d("HardwareState", "idle2=" + idle2 + "\ncpu2=" + cpu2);

            double cpuUsage = (double) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)) * 100.0;
//            Log.d("HardwareState", "Cpu 使用率：" + cpuUsage);
            return String.format("%.2f", cpuUsage)+"%";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取CPU温度
    public String getCpuTemperature() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone0/temp"))) {
            String line = reader.readLine();
            if (line != null) {
                double temperature = Double.parseDouble(line) / 1000.0;
//                Log.d("HardwareState", "Cpu 温度：" + temperature);
                return String.format("%.2f", temperature) +"℃";
            }
            return "无法获取 Cpu 温度";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取设备运行时间
    public long getUptime(){
        long uptimeMilllis = SystemClock.uptimeMillis();
//        long elapsedRealtimeMillis = SystemClock.elapsedRealtime();

        long uptimeSeconds = uptimeMilllis/ 1000;
//        long elapsedRealtimeSeconds = elapsedRealtimeMillis/ 1000;

        Log.d("HardwareState","设备运行时间为："+uptimeSeconds);
        return uptimeSeconds;
    }

}
