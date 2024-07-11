package com.example.henglangapi_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sdk.QYSDK;


public class TimeTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_test_layout);
        QYSDK qySDK = new QYSDK(this);

        // 获取 switchAutoTimeButton 按钮
        Button switchAutoTimeButton = findViewById(R.id.switchAutoTimeButton);
        switchAutoTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // 调用 qySDK 中的 toggleAutoTime 方法
                    qySDK.toggleAutoTime();
            }
        });

        Button openNTP = findViewById(R.id.openNTP);
        openNTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    qySDK.switchAutoDateAndTime(true);
            }
        });

        Button closeNTP = findViewById(R.id.closeNTP);
        closeNTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    qySDK.switchAutoDateAndTime(false);
            }
        });



    }
}