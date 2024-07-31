package com.example.apiDemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
                String receivedString = qySDK.getHomePackage();
                Toast.makeText(context, "Received: " + receivedString, Toast.LENGTH_LONG).show();
            }
        });

        // 切换当前桌面lunch
        ImageButton setPackage = findViewById(R.id.setPackage);
        setPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomeScreenApp();
            }
        });

        // 传参方式发送page包名
        ImageButton launcherButton = findViewById(R.id.launcherButton);
        launcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomePackage("com.example.apiDemo");
            }
        });

        // 传参方式发送page包名设置原来的luncher
        ImageButton launcher3Button = findViewById(R.id.launcher3Button);
        launcher3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setHomePackage("com.android.launcher3");
            }
        });


    }

}
