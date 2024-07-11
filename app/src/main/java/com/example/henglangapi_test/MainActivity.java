package com.example.henglangapi_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enjoysdk.EnjoySDK;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnjoySDK enjoySDK = new EnjoySDK(this);
        Application context = (Application) getApplicationContext();

        List<String> stringList = new ArrayList<>();
        stringList.add("com.example.myapplication");


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
                enjoySDK.addKeepAliveAPP(stringList);
                enjoySDK.enableKeepAlive(true);
            }
        });

        Button testButton = findViewById(R.id.test);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.removeKeepAliveAPP();
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

        // 时间设置功能测试
        Button timeUtilButton = findViewById(R.id.timeUtilTest_bt);
        timeUtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TimeUtilActivity.class));

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
                enjoySDK.takeScreenshot();
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
                String receivedString = enjoySDK.getHomePackage();
                Toast.makeText(context, "Received: " + receivedString, Toast.LENGTH_LONG).show();
            }
        });

        // 切换当前桌面lunch
        Button setPackage = findViewById(R.id.setPackage);
        setPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setHomeScreenApp();
            }
        });

        // 传参方式发送page包名
        Button launcherButton = findViewById(R.id.launcherButton);
        launcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setHomePackage("com.example.henglangapi_test");
            }
        });

        // 传参方式发送page包名设置原来的luncher
        Button launcher3Button = findViewById(R.id.launcher3Button);
        launcher3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setHomePackage("com.android.launcher3");
            }
        });


    }

}
