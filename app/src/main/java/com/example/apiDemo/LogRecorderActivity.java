package com.example.apiDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdk.McErrorCode;
import com.example.sdk.QYSDK;

public class LogRecorderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_recorder);
        QYSDK qySDK = new QYSDK(this);
        boolean log_state = qySDK.isLogRecorderEnabled();

        //设置日志记录开关
        Switch switchEthernet = findViewById(R.id.logDumper_switch);
        switchEthernet.setChecked(log_state);
        switchEthernet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int ans = qySDK.enableLogRecorder(isChecked);
                if (ans == McErrorCode.ENJOY_COMMON_SUCCESSFUL) {
                    Toast.makeText(LogRecorderActivity.this,
                            isChecked ? "日志记录启用成功" : "日志记录禁用成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogRecorderActivity.this,
                            isChecked ? "日志记录启用失败" : "日志记录禁用失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 获取 getLogRecorderTime 按钮

        TextView getLogRecorderTime_tv = findViewById(R.id.logDumperTime_tv);
        int recTime = qySDK.getRecorderTime();
        getLogRecorderTime_tv.setText(String.valueOf(recTime)+"h");

        // 获取 setLogRecorderTime 按钮

        Button setLogRecorderTime = findViewById(R.id.setLogRecorderTime_bt);
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
                        int ans = qySDK.setRecorderTime(hour);
                        if (ans == McErrorCode.ENJOY_COMMON_SUCCESSFUL) {
                            Toast.makeText(LogRecorderActivity.this, "时间设置成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogRecorderActivity.this, "时间设置失败", Toast.LENGTH_SHORT).show();
                        }
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