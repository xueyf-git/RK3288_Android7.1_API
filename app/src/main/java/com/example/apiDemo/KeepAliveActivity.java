package com.example.apiDemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.QYSDK;

import java.util.ArrayList;
import java.util.List;

public class KeepAliveActivity extends AppCompatActivity {
    QYSDK qySDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keep_alive);
        qySDK = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        List<String> stringList = new ArrayList<>();
        stringList.add("com.example.myapplication");

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

        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KeepAliveActivity.this, MainActivity.class));
            }
        });
    }
}