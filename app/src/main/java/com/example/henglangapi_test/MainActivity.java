package com.example.henglangapi_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;



import com.example.enjoysdk.EnjoySDK;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnjoySDK enjoySDK = new EnjoySDK(this);

        // 获取 timeButton 按钮
        Button timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimeTestActivity 活动
                startActivity(new Intent(MainActivity.this, TimeTestActivity.class));
            }
        });

        Button shooter = findViewById(R.id.shooter);
        shooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.takeScreenshot();
            }
        });

    }

}
