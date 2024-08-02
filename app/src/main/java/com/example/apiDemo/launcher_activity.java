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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.QYSDK;

public class launcher_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launcher);
        QYSDK qySDK = new QYSDK(this);
        Application context = (Application) getApplicationContext();

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(launcher_activity.this, MainActivity.class));
            }
        });
    }
}