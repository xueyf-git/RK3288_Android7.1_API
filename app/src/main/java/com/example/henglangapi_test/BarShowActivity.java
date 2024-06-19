package com.example.henglangapi_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.enjoysdk.EnjoySDK;


public class BarShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_show_layout);
        EnjoySDK enjoySDK = new EnjoySDK(this);

        // 同时显示状态栏导航栏按钮
        Button statusBarBarAndNavigationShow = findViewById(R.id.BarAndNavigationShow);
        statusBarBarAndNavigationShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.switchStatusBarAndNavigationOverwrite(true);
            }
        });

        // 同时隐藏状态栏导航栏按钮
        Button statusBarAndNavigationHide = findViewById(R.id.BarAndNavigationHide);
        statusBarAndNavigationHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.switchStatusBarAndNavigationOverwrite(false);
            }
        });

        // 显示状态栏按钮
        Button statusBarShow = findViewById(R.id.BarShow);
        statusBarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setStatusBarShowStatus(true);
            }
        });

        // 隐藏状态栏按钮
        Button statusBarHide = findViewById(R.id.BarHide);
        statusBarHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setStatusBarShowStatus(false);
            }
        });

        // 显示导航栏按钮
        Button statusNavigationShow = findViewById(R.id.NavigationShow);
        statusNavigationShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setNavigationBarShowStatus(true);
            }
        });

        // 隐藏导航栏按钮
        Button statusNavigationHide = findViewById(R.id.NavigationHide);
        statusNavigationHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setNavigationBarShowStatus(false);
            }
        });






    }
}