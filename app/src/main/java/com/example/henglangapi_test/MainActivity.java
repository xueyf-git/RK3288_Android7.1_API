package com.example.henglangapi_test;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enjoysdk.EnjoySDK;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnjoySDK enjoySDK = new EnjoySDK(this);
        Application context = (Application) getApplicationContext();

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
                enjoySDK.setHomePackage("com.example.test");
            }
        });




    }

}
