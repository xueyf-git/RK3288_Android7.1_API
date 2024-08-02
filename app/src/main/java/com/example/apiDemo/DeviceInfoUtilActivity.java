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

import com.example.sdk.QYSDK;

public class DeviceInfoUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_info_util_test);
        QYSDK enjoySDK = new QYSDK(this);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //获取生产商信息
        TextView getFactoryInfo = findViewById(R.id.getFactoryInfo_tv);
        getFactoryInfo.setText(""+enjoySDK.getFactoryInfo());

        //获取产品信息
        TextView getProductInfo = findViewById(R.id.getProductInfo_tv);
        getProductInfo.setText(""+enjoySDK.getProductInfo());

        //获取定制信息
        TextView getSpecialInfo = findViewById(R.id.getSpecialInfo_tv);
        getSpecialInfo.setText(""+enjoySDK.getSpecialInfo());

        //获取cpu类型信息
        TextView getCpuTypeInfo = findViewById(R.id.getCPUTypeInfo_tv);
        getCpuTypeInfo.setText(""+enjoySDK.getCpuTypeInfo());

        //获取cpu序列信息
        TextView getCpuSerialInfo = findViewById(R.id.getCPUSerial_tv);
        getCpuSerialInfo.setText(""+enjoySDK.getCpuSerial());

        //获取Android版本号
        TextView getAndroidVersionInfo = findViewById(R.id.getAndroidVersionInfo_tv);
        getAndroidVersionInfo.setText(""+enjoySDK.getAndroidVersionInfo());

        //获取获取平台版本信息
        TextView getPlatformVersionInfo = findViewById(R.id.getPlatformVersionInfo_tv);
        String res = enjoySDK.getPlatformVersionInfo();
        getAndroidVersionInfo.setText(""+enjoySDK.getAndroidVersionInfo());

        //获取获取系统版本信息
        TextView getSystemVersionInfo = findViewById(R.id.getSystemVersionInfo_tv);
        getSystemVersionInfo.setText(""+enjoySDK.getSystemVersion());

        //获取获取Boot版本信息
        TextView getBootVersion = findViewById(R.id.getBootVersion_tv);
        getBootVersion.setText(""+enjoySDK.getBootVersion());

        //获取获取Oem版本信息
        TextView getOemVersion = findViewById(R.id.getOemVersion_tv);
        getOemVersion.setText(""+enjoySDK.getOemVersion());

//        //设置触点数量信息
//        Button setTouchPointerCount = findViewById(R.id.setTouchPointerCount_bt);
//        EditText setTouchPointerCount_ed = findViewById(R.id.setTouchPointerCount_ed);
//        setTouchPointerCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String numberText = setTouchPointerCount_ed.getText().toString().trim();
//                if (!numberText.isEmpty()) {
//                    try {
//                        // 将文本内容转换为整数（如果输入的确实是数字）
//                        int count = Integer.parseInt(numberText);
//                        enjoySDK.setTouchPointerCount(count);
//
//                    } catch (NumberFormatException e) {
//                        setTouchPointerCount_ed.setText("输入的数字不规范！请重新输入！");
//                    }
//                } else {
//                    // 如果 EditText 为空，可以提示用户输入内容
//                    setTouchPointerCount_ed.setText("输入不能为空！请重新输入！");
//                }
//            }
//        });
//
//        //设置触摸监听器
//        View touchView = findViewById(R.id.touchView);
//
//
//        //获取触点数量信息
//        Button getTouchPointerCount_bt = findViewById(R.id.getTouchPointerCount_bt);
//        getTouchPointerCount_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                touchView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        enjoySDK.initTouchListener(touchView);
//                        int res = enjoySDK.getTouchPointerCount();
//                        textView.setText("触点数量为："+res);
//                    }
//                });
//
//            }
//        });

        //获取获取固件版本信息
        TextView getFirmwareVersion = findViewById(R.id.getFirmwareVersion_tv);
        getFirmwareVersion.setText(""+enjoySDK.getFirmwareVersion());

        //获取获取固件版本号信息
        TextView getFirmwareVersionCode = findViewById(R.id.getFirmwareVersionCode_tv);
        getFirmwareVersionCode.setText(""+enjoySDK.getFirmwareVersionCode());

        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeviceInfoUtilActivity.this, MainActivity.class));
            }
        });
    }
}