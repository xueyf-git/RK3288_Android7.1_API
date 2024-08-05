package com.example.apiDemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        QYSDK qySDK = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //开机动画测试
        ImageButton BootAnimation_bt = findViewById(R.id.BootAnimationTest_bt);
        BootAnimation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BootAnimationActivity.class));
            }
        });

        //wifi测试
        ImageButton wifiShow = findViewById(R.id.wifiShow);
        wifiShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NetWorkActivity.class));
            }
        });


        ImageButton keepaliveButton = findViewById(R.id.keepalive);
        keepaliveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,KeepAliveActivity.class));
            }
        });


        // 电源管理按钮
        ImageButton powerManagementButton = findViewById(R.id.powerManagementButton);
        powerManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PowerManagementTest.class));
            }
        });


        // 固件信息按钮
        ImageButton deviceUtil_bt = findViewById(R.id.deviceUtilTest_bt);
        deviceUtil_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimeTestActivity 活动
                startActivity(new Intent(MainActivity.this, DeviceInfoUtilActivity.class));
            }
        });


        // 日志记录按钮
        ImageButton logrecordButton = findViewById(R.id.logrcorder);
        logrecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 LogRecorderActivity 活动
                startActivity(new Intent(MainActivity.this, LogRecorderActivity.class));
            }
        });


        // ntp校时按钮
        ImageButton timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimeTestActivity 活动
                startActivity(new Intent(MainActivity.this, TimeTestActivity.class));
            }
        });

        // 截图按钮
        ImageButton shooter = findViewById(R.id.shooter);
        shooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.takeScreenshot();
            }
        });

        //系统UI
        ImageButton barShow = findViewById(R.id.systemUI);
        barShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SystemUIActivity.class));
            }
        });

        // 获取当前lunch名称
        ImageButton getPackage = findViewById(R.id.getPackage);
        getPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,launcher_activity.class));
            }
        });

        //硬件状态
        ImageButton HardwareStateButton = findViewById(R.id.HardwareStateButton);
        HardwareStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HardwareStateActivity.class));
            }
        });



    }

}
