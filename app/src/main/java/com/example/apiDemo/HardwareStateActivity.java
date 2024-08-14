package com.example.apiDemo;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.QYSDK;

public class HardwareStateActivity extends AppCompatActivity {

    QYSDK qysdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hardware_state);
        qysdk = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //设置cpu使用率
        String cpuUsage = qysdk.getCPUUsage();
        TextView cpuUsage_tv = findViewById(R.id.cpuUsage_tv);
        cpuUsage_tv.setText(cpuUsage);

        //设置cpu温度
        String cpuTemperature = qysdk.getCPUTemperature();
        TextView cpuTemperature_tv = findViewById(R.id.cpuTemperature_tv);
        cpuTemperature_tv.setText(cpuTemperature);

        //设置cpu启动时间
        long cpuUptime = qysdk.getUpTime();
        TextView cpuUptime_tv = findViewById(R.id.cpuUptime_tv);
        cpuUptime_tv.setText(""+cpuUptime+"s");


        //返回主界面
        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HardwareStateActivity.this, MainActivity.class));
            }
        });

    }
}