package com.example.apiDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.McErrorCode;
import com.example.sdk.QYSDK;

public class PowerManagementTest extends AppCompatActivity {

    QYSDK qySDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_power_management_test);

        //去除顶部显示应用名称栏目
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        //启用触摸唤醒
        Button enableTouchWake_bt = findViewById(R.id.enableTouchWake_bt);
        enableTouchWake_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.setTouchWake(true);
            }
        });

        //禁用触摸唤醒
        Button disableTouchWake_bt = findViewById(R.id.disableTouchWake_bt);
        disableTouchWake_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.setTouchWake(false);
            }
        });

        //启用定时触摸唤醒
        Button enableTimedToTouchWake = findViewById(R.id.enableTimedToTouchWake_bt);
        enableTimedToTouchWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.setTimedTouchWake(true);
            }
        });

        //禁用定时触摸唤醒
        Button disableTimedToTouchWake = findViewById(R.id.disableTimedToTouchWake_bt);
        disableTimedToTouchWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.setTimedTouchWake(false);
            }
        });


        //增加定时触摸唤醒计划
        Button addScheduleToTouchWake_bt = findViewById(R.id.addScheduleToTouchWake_bt);
        EditText addStartToTouchWake_ev = findViewById(R.id.addStartToTouchWake_ev);
        EditText addEndToTouchWake_ev = findViewById(R.id.addEndToTouchWake_ev);
        addScheduleToTouchWake_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = addStartToTouchWake_ev.getText().toString().trim();
                String end = addEndToTouchWake_ev.getText().toString().trim();
                if(!start.isEmpty() || !end.isEmpty()){
                    qySDK.addScheduleToTouchWake(start,end,false);
                }else {
                    addEndToTouchWake_ev.setText("两个输入都不能为空，请重输！");
                }
            }
        });

        //删除定时触摸计划
        Button deleteScheduleToTouchWake_bt = findViewById(R.id.deleteScheduleToTouchWake_bt);
        EditText deleteStartToTouchWake_ev = findViewById(R.id.deleteStartToTouchWake_ev);
        EditText deleteEndToTouchWake_ev = findViewById(R.id.deleteEndToTouchWake_ev);
        deleteScheduleToTouchWake_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = deleteStartToTouchWake_ev.getText().toString().trim();
                String end = deleteEndToTouchWake_ev.getText().toString().trim();
                if(!start.isEmpty() || !end.isEmpty()){
                    qySDK.deleteScheduleToTouchWake(start,end,false);
                }else {
                    deleteEndToTouchWake_ev.setText("两个输入都不能为空，请重输！");
                }
            }
        });

        //清除定时触摸计划
        Button cancelTimedTouchWake_bt = findViewById(R.id.cancelTimedToWake);
        cancelTimedTouchWake_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.cancelTimedToTouchWake();
            }
        });

        //获取定时触摸计划状态
        Button getTimedTouchWakeState_bt = findViewById(R.id.getTimedToWakeState_bt);
        TextView getTimedTouchWakeState_tv = findViewById(R.id.getTimedTouchWakeState_tv);
        getTimedTouchWakeState_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = qySDK.getTimedTouchWakeState();
                if (a == McErrorCode.ENJOY_COMMON_SUCCESSFUL) {
                    getTimedTouchWakeState_tv.setText("定时触摸计划已启用");
                }
                if (a == McErrorCode.ENJOY_COMMON_ERROR_SDK_NOT_SUPPORT) {
                    getTimedTouchWakeState_tv.setText("定时触摸计划未启用");
                }
            }
        });


        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PowerManagementTest.this, MainActivity.class));
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