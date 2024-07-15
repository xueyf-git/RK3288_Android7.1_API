package com.example.apiDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;

public class PowerManagementTest extends AppCompatActivity {

    QYSDK qySDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_power_management_test);

        qySDK = new QYSDK(this);
        //注册低电量警告
        qySDK.registerLowBatteryReceiver();

        //显示是否进行低电量警告
        TextView isLBW_tv = findViewById(R.id.isLowBatteryStatus_tv);
        isLBW_tv.setText("当前状态为："+qySDK.getIsLowBatteryWarning());

        //设置是否进行低电量警告
        Button isLBW_bt = findViewById(R.id.isLBW_bt);
        isLBW_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLBW = qySDK.getIsLowBatteryWarning();
                isLBW = !isLBW;
                qySDK.setIsLowBatteryWarning(isLBW);
                isLBW_tv.setText("当前状态为："+qySDK.getIsLowBatteryWarning());
            }
        });

        //屏幕亮度调整
        SeekBar seekBarBrightness = findViewById(R.id.screenBrightnessCtl);
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当 SeekBar 进度改变时调用 QYSDK 方法设置屏幕亮度
                qySDK.setScreenBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 当用户开始拖动 SeekBar 时的处理
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当用户停止拖动 SeekBar 时的处理
            }
        });


        //系统重启reboot
        Button reboot = findViewById(R.id.reboot_bt);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.reboot();
            }
        });

        //电池当前电量显示
        int batteryLevel = qySDK.batteryLevel();
        TextView batteryLevel_tv = findViewById(R.id.batteryLevel_tv);
        batteryLevel_tv.setText("当前电量为："+batteryLevel+"%");

        //电池充电状态显示
        String chargeStatus = qySDK.chargeStatus();
        TextView chargeStatus_tv = findViewById(R.id.chargingStatus_tv);
        chargeStatus_tv.setText(chargeStatus);


        //系统关机
        Button shutdown = findViewById(R.id.shutdown_bt);
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean confirm = true;
                qySDK.shutdown(confirm);
            }
        });

        //禁用系统休眠
        Button disableSystemGoToSleep_bt = findViewById(R.id.disableSystemGoToSleep_bt);
        disableSystemGoToSleep_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qySDK.enableScreenOn();
            }
        });

        //启用系统休眠
        Button enableSystemGoToSleep_bt = findViewById(R.id.enableSystemGoToSleep_bt);
        enableSystemGoToSleep_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qySDK.disableScreenOn();
            }
        });

        //设置系统自动休眠最大间隔
        Button setMaximumTimeoutToLock_bt = findViewById(R.id.setMaximumTimeToLock_bt);
        EditText setMaximumTimeoutToLock_ed = findViewById(R.id.setMaximumTimeToLock_ed);
        setMaximumTimeoutToLock_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberText = setMaximumTimeoutToLock_ed.getText().toString().trim();

                if (!numberText.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int timeout = Integer.parseInt(numberText);
                        // 调用 qySDK 中的 setRecorderTime 方法
                        qySDK.setMaximumTimeToLock(timeout);

                    } catch (NumberFormatException e) {
                        setMaximumTimeoutToLock_ed.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setMaximumTimeoutToLock_ed.setText("输入不能为空！请重新输入！");
                }
            }
        });

        //设置屏幕自动休眠最大间隔
        Button setScreenOffTimeout_bt = findViewById(R.id.setScreenOffTimeout_bt);
        EditText setScreenOffTimeout_ed = findViewById(R.id.setScreenOffTimeout_ed);
        setScreenOffTimeout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberText = setScreenOffTimeout_ed.getText().toString().trim();

                if (!numberText.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int timeout = Integer.parseInt(numberText);
                        // 调用 qySDK 中的 setRecorderTime 方法
                        qySDK.setScreenOffTimeout(timeout);

                    } catch (NumberFormatException e) {
                        setScreenOffTimeout_ed.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setScreenOffTimeout_ed.setText("输入不能为空！请重新输入！");
                }
            }
        });

    }
    //注销低电量警告
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在活动销毁时注销低电量广播接收器
        qySDK.unregisterLowBatteryReceiver();
    }
}