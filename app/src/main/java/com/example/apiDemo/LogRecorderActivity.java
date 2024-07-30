package com.example.apiDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sdk.QYSDK;

public class LogRecorderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_recorder);
        QYSDK qySDK = new QYSDK(this);

        // 获取 enableLogRecorder 按钮
        Button enableLogRecorderButton = findViewById(R.id.enableLogRecorder);
        enableLogRecorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 qySDK 中的 enableLogRecorder 方法
                qySDK.enableLogRecorder(true);
            }
        });

        // 获取 disableLogRecorder 按钮
        Button disableLogRecorderButton = findViewById(R.id.disableLogRecorder);
        disableLogRecorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 qySDK 中的 enableLogRecorder 方法
                qySDK.enableLogRecorder(false);
            }
        });

        // 获取 getLogRecorderButton 按钮
        Button getLogRecorderButton = findViewById(R.id.isLogRecorderEnable);
        TextView getLogRecorder_tv = findViewById(R.id.isLogRecorderEnable_tv);
        getLogRecorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 qySDK 中的 isLogRecorderEnabled 方法
                boolean recStatus = qySDK.isLogRecorderEnabled();
                getLogRecorder_tv.setText(String.valueOf(recStatus));
            }
        });

        // 获取 getLogRecorderTime 按钮
        Button getLogRecorderTime = findViewById(R.id.getLogRecorderTime);
        TextView getLogRecorderTime_tv = findViewById(R.id.logRecorderTime_tv);
        getLogRecorderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 qySDK 中的 getRecorderTime 方法
                int recTime = qySDK.getRecorderTime();
                getLogRecorderTime_tv.setText(String.valueOf(recTime));
            }
        });

        // 获取 setLogRecorderTime 按钮
        Button setLogRecorderTime = findViewById(R.id.setLogRecorderTime);
        EditText setLogReocrderTime_ev = findViewById(R.id.setLogRecorderTime_ev);
        setLogRecorderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取 EditText 中的文本内容
                String numberText = setLogReocrderTime_ev.getText().toString().trim();

                if (!numberText.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int hour = Integer.parseInt(numberText);
                        // 调用 qySDK 中的 setRecorderTime 方法
                        qySDK.setRecorderTime(hour);

                    } catch (NumberFormatException e) {
                        setLogReocrderTime_ev.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setLogReocrderTime_ev.setText("输入不能为空！请重新输入！");
                }
            }
        });

        // 获取 exportLog 按钮
        Button exportLog = findViewById(R.id.exportLog);
        TextView exportLog_tv = findViewById(R.id.exportLog_tv);
        exportLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 qySDK 中的 logExport 方法
                String exp = qySDK.logExport();
                exportLog_tv.setText(String.valueOf(exp));
            }
        });

        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogRecorderActivity.this, MainActivity.class));
            }
        });


    }
}