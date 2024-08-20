package com.example.apiDemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HardwareStateActivity extends AppCompatActivity {

    private QYSDK qysdk;
    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_state);

        qysdk = new QYSDK(this);

        // 初始化定期任务调度器
        scheduler = Executors.newScheduledThreadPool(1);

        // 去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 定期更新CPU使用率
        scheduleCpuUsageUpdate();

        // 定期更新CPU温度
        scheduleCpuTemperatureUpdate();

        // 定期更新设备运行时间
        scheduleCpuUptimeUpdate();

        // 返回主界面
        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(view -> startActivity(new Intent(HardwareStateActivity.this, MainActivity.class)));
    }

    private void scheduleCpuUsageUpdate() {
        scheduler.scheduleWithFixedDelay(() -> {
            String cpuUsage = qysdk.getCPUUsage();
            runOnUiThread(() -> {
                TextView cpuUsage_tv = findViewById(R.id.cpuUsage_tv);
                cpuUsage_tv.setText(cpuUsage);
            });
        }, 0, 5, TimeUnit.SECONDS); // 每10秒更新一次
    }

    private void scheduleCpuTemperatureUpdate() {
        scheduler.scheduleWithFixedDelay(() -> {
            String cpuTemperature = qysdk.getCPUTemperature();
            runOnUiThread(() -> {
                TextView cpuTemperature_tv = findViewById(R.id.cpuTemperature_tv);
                cpuTemperature_tv.setText(cpuTemperature);
            });
        }, 0, 5, TimeUnit.SECONDS); // 每10秒更新一次
    }

    private void scheduleCpuUptimeUpdate() {
        scheduler.scheduleWithFixedDelay(() -> {
            long cpuUptime = qysdk.getUpTime();
            runOnUiThread(() -> {
                TextView cpuUptime_tv = findViewById(R.id.cpuUptime_tv);
                cpuUptime_tv.setText(cpuUptime + "s");
            });
        }, 0, 1, TimeUnit.SECONDS); // 每10秒更新一次
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduler != null) {
            scheduler.shutdown(); // 关闭调度器
        }
    }
}
