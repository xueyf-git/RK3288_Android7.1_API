package com.example.apiDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.QYSDK;

public class SystemUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_system_uiactivity);
        QYSDK qySDK = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 同时显示状态栏导航栏按钮
        Button statusBarBarAndNavigationShow = findViewById(R.id.BarAndNavigationShow);
        statusBarBarAndNavigationShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.switchStatusBarAndNavigationOverwrite(true);
            }
        });

        // 同时隐藏状态栏导航栏按钮
        Button statusBarAndNavigationHide = findViewById(R.id.BarAndNavigationHide);
        statusBarAndNavigationHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.switchStatusBarAndNavigationOverwrite(false);
            }
        });

        // 显示状态栏按钮
        Button statusBarShow = findViewById(R.id.BarShow);
        statusBarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setStatusBarShowStatus(true);
            }
        });

        // 隐藏状态栏按钮
        Button statusBarHide = findViewById(R.id.BarHide);
        statusBarHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setStatusBarShowStatus(false);
            }
        });

        // 显示导航栏按钮
        Button statusNavigationShow = findViewById(R.id.NavigationShow);
        statusNavigationShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setNavigationBarShowStatus(true);
            }
        });

        // 隐藏导航栏按钮
        Button statusNavigationHide = findViewById(R.id.NavigationHide);
        statusNavigationHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qySDK.setNavigationBarShowStatus(false);
            }
        });

        //获取屏幕旋转角度

        TextView getSystemRotation_tv = findViewById(R.id.getSystemRotation_tv);

        int rotation = qySDK.getSystemRotation();

        getSystemRotation_tv.setText(""+rotation);

        //设置屏幕旋转角度
        Button setScreenRotation_bt = findViewById(R.id.setScreenRotate_bt);
        EditText setScreenRotation_et = findViewById(R.id.setScreenRotation_et);
        setScreenRotation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rotation_str = setScreenRotation_et.getText().toString().trim();
                if (!rotation_str.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int rotation = Integer.parseInt(rotation_str);
                        qySDK.setScreenRotation(rotation);
                        getSystemRotation_tv.setText(""+qySDK.getSystemRotation());
                    } catch (NumberFormatException e) {
                        setScreenRotation_et.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setScreenRotation_et.setText("输入不能为空！请重新输入！");
                }
            }
        });




        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SystemUIActivity.this, MainActivity.class));
            }
        });
    }
}