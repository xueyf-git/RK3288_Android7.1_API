package com.example.apiDemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sdk.QYSDK;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QYSDK qySDK = new QYSDK(this);
        Application context = (Application) getApplicationContext();

        List<String> stringList = new ArrayList<>();
        stringList.add("com.example.myapplication");


        //屏幕旋转测试
        Button ScreenRotation_bt = findViewById(R.id.ScreenRotationTest_bt);
        ScreenRotation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScreenRotationActivity.class));
            }
        });

        //开机动画测试
        Button BootAnimation_bt = findViewById(R.id.BootAnimationTest_bt);
        BootAnimation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BootAnimationActivity.class));
            }
        });

        //wifi测试
        Button wifiShow = findViewById(R.id.wifiShow);
        wifiShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Wifi.class));
            }
        });


        Button keepaliveButton = findViewById(R.id.keepalive);
        keepaliveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.addKeepAliveAPP(stringList);
                qySDK.enableKeepAlive(true);
            }
        });

        Button testButton = findViewById(R.id.test);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.removeKeepAliveAPP();
            }
        });

        // 电源管理按钮
        Button powerManagementButton = findViewById(R.id.powerManagementButton);
        powerManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PowerManagementTest.class));
            }
        });

        // 以太网配置按钮
        Button ethernetConfig_bt = findViewById(R.id.EthernetConfigTest_bt);
        ethernetConfig_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 LogRecorderActivity 活动
                startActivity(new Intent(MainActivity.this, EthernetConfigActivity.class));
            }
        });

        // 时间设置功能测试
        Button timeUtilButton = findViewById(R.id.timeUtilTest_bt);
        timeUtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TimeUtilActivity.class));

            }
        });

        // 固件信息按钮
        Button deviceUtil_bt = findViewById(R.id.deviceUtilTest_bt);
        deviceUtil_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimeTestActivity 活动
                startActivity(new Intent(MainActivity.this, DeviceInfoUtilActivity.class));
            }
        });


        // 日志记录按钮
        Button logrecordButton = findViewById(R.id.logrcorder);
        logrecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 LogRecorderActivity 活动
                startActivity(new Intent(MainActivity.this, LogRecorderActivity.class));
            }
        });


        // ntp校时按钮
        Button timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimeTestActivity 活动
                startActivity(new Intent(MainActivity.this, TimeTestActivity.class));
            }
        });

        // 截图按钮
        Button shooter = findViewById(R.id.shooter);
        shooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.takeScreenshot();
            }
        });

        //状态栏 导航栏 显示/隐藏 按钮
        Button barShow = findViewById(R.id.barShow);
        barShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BarShowActivity.class));
            }
        });

        // 获取当前lunch名称
        Button getPackage = findViewById(R.id.getPackage);
        getPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedString = qySDK.getHomePackage();
                Toast.makeText(context, "Received: " + receivedString, Toast.LENGTH_LONG).show();
            }
        });

        // 切换当前桌面lunch
        Button setPackage = findViewById(R.id.setPackage);
        setPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomeScreenApp();
            }
        });

        // 传参方式发送page包名
        Button launcherButton = findViewById(R.id.launcherButton);
        launcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomePackage("com.example.apiDemo");
            }
        });

        // 传参方式发送page包名设置原来的luncher
        Button launcher3Button = findViewById(R.id.launcher3Button);
        launcher3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomePackage("com.android.launcher3");
            }
        });


    }

}
