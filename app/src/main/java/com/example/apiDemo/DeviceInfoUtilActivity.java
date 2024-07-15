package com.example.apiDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;

public class DeviceInfoUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_info_util_test);
        QYSDK enjoySDK = new QYSDK(this);
        TextView textView = findViewById(R.id.textView);


        //获取生产商信息
        Button getFactoryInfo = findViewById(R.id.getFactoryInfo_bt);
        getFactoryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getFactoryInfo();
                textView.setText("生产商信息："+res);
            }
        });

        //获取产品信息
        Button getProductInfo = findViewById(R.id.getProductInfo_bt);
        getProductInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getProductInfo();
                textView.setText("产品型号："+res);
            }
        });

        //获取定制信息
        Button getSpecialInfo = findViewById(R.id.getSpecialInfo_bt);
        getSpecialInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("定制信息："+enjoySDK.getSpecialInfo());
            }
        });


        //获取cpu类型信息
        Button getCpuTypeInfo = findViewById(R.id.getCpuTypeInfo_bt);
        getCpuTypeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getCpuTypeInfo();
                textView.setText("cpu类型信息："+res);
            }
        });

        //获取cpu序列信息
        Button getCpuSerialInfo = findViewById(R.id.getCpuSerial_bt);
        getCpuSerialInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getCpuSerial();
                textView.setText("cpu序列信息："+res);
            }
        });

        //获取Android版本号
        Button getAndroidVersionInfo = findViewById(R.id.getAndroidVersionInfo_bt);
        getAndroidVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getAndroidVersionInfo();
                textView.setText("Android版本号："+res);
            }
        });

        //获取获取平台版本信息
        Button getPlatformVersionInfo = findViewById(R.id.getPlatformVersionInfo_bt);
        getPlatformVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getPlatformVersionInfo();
                textView.setText("平台版本信息："+res);
            }
        });

        //获取获取系统版本信息
        Button getSystemVersionInfo = findViewById(R.id.getSystemVersionInfo_bt);
        getSystemVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getSystemVersion();
                textView.setText("系统版本信息："+res);
            }
        });

        //获取获取Boot版本信息
        Button getBootVersion = findViewById(R.id.getbootVersion_bt);
        getBootVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getBootVersion();
                textView.setText("Boot版本为："+res);
            }
        });

        //获取获取Oem版本信息
        Button getOemVersion = findViewById(R.id.getOemVersion_bt);
        getOemVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getOemVersion();
                textView.setText("Oem版本为："+res);
            }
        });

        //设置触点数量信息
        Button setTouchPointerCount = findViewById(R.id.setTouchPointerCount_bt);
        EditText setTouchPointerCount_ed = findViewById(R.id.setTouchPointerCount_ed);
        setTouchPointerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberText = setTouchPointerCount_ed.getText().toString().trim();
                if (!numberText.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int count = Integer.parseInt(numberText);
                        enjoySDK.setTouchPointerCount(count);

                    } catch (NumberFormatException e) {
                        setTouchPointerCount_ed.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setTouchPointerCount_ed.setText("输入不能为空！请重新输入！");
                }
            }
        });

        //设置触摸监听器
        View touchView = findViewById(R.id.touchView);


        //获取触点数量信息
        Button getTouchPointerCount_bt = findViewById(R.id.getTouchPointerCount_bt);
        getTouchPointerCount_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchView.post(new Runnable() {
                    @Override
                    public void run() {
                        enjoySDK.initTouchListener(touchView);
                        int res = enjoySDK.getTouchPointerCount();
                        textView.setText("触点数量为："+res);
                    }
                });

            }
        });

        //获取获取固件版本信息
        Button getFirmwareVersion = findViewById(R.id.getFirmwareVersion_bt);
        getFirmwareVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = enjoySDK.getFirmwareVersion();
                textView.setText("固件版本为："+res);
            }
        });

        //获取获取固件版本号信息
        Button getFirmwareVersionCode = findViewById(R.id.getFirmwareVersionCode_bt);
        getFirmwareVersionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = enjoySDK.getFirmwareVersionCode();
                textView.setText("固件版本号为："+res);
            }
        });
    }
}