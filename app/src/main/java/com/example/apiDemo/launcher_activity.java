package com.example.apiDemo;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
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