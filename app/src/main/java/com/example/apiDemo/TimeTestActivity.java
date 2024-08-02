package com.example.apiDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;


public class TimeTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.time_test_layout);
        QYSDK qySDK = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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


        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimeTestActivity.this, MainActivity.class));
            }
        });

        Button setTime_bt = findViewById(R.id.setTime_bt);
        EditText setTime_et = findViewById(R.id.setTime_et);
        setTime_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberText = setTime_et.getText().toString().trim();
                String[] timeParts = numberText.split(":");



                if (!numberText.isEmpty()) {
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);
                    int second = Integer.parseInt(timeParts[2]);
                    qySDK.setTime(hour,minute,second);
                    Toast.makeText(TimeTestActivity.this,
                            "时间设置成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setTime_et.setText("输入不能为空！请重新输入！");
                }
            }
        });

        Button setDate_bt = findViewById(R.id.setDate_bt);
        EditText setDate_et = findViewById(R.id.setDate_et);
        setDate_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberText = setDate_et.getText().toString().trim();
                String[] timeParts = numberText.split("-");



                if (!numberText.isEmpty()) {
                    int year = Integer.parseInt(timeParts[0]);
                    int month = Integer.parseInt(timeParts[1]);
                    int day = Integer.parseInt(timeParts[2]);
                    qySDK.setDate(year,month-1,day);
                    Toast.makeText(TimeTestActivity.this,
                            "日期设置成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setTime_et.setText("输入不能为空！请重新输入！");
                }
            }
        });



    }
}