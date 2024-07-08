package com.example.henglangapi_test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.enjoysdk.EnjoySDK;

public class PowerManagementTest extends AppCompatActivity {

    EnjoySDK enjoySDK = new EnjoySDK(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_power_management_test);

        //注册低电量警告
        enjoySDK.registerLowBatteryReceiver();

        //显示是否进行低电量警告
        TextView isLBW_tv = findViewById(R.id.isLowBatteryStatus_tv);
        isLBW_tv.setText("当前状态为："+enjoySDK.getIsLowBatteryWarning());

        //设置是否进行低电量警告
        Button isLBW_bt = findViewById(R.id.isLBW_bt);
        isLBW_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLBW = enjoySDK.getIsLowBatteryWarning();
                isLBW = !isLBW;
                enjoySDK.setIsLowBatteryWarning(isLBW);
                isLBW_tv.setText("当前状态为："+enjoySDK.getIsLowBatteryWarning());
            }
        });

        //屏幕亮度调整
        SeekBar seekBarBrightness = findViewById(R.id.screenBrightnessCtl);
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当 SeekBar 进度改变时调用 EnjoySDK 方法设置屏幕亮度
                enjoySDK.setScreenBrightness(progress);
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
                int a = enjoySDK.reboot();
            }
        });

        //电池当前电量显示
        int batteryLevel = enjoySDK.batteryLevel();
        TextView batteryLevel_tv = findViewById(R.id.batteryLevel_tv);
        batteryLevel_tv.setText("当前电量为："+batteryLevel+"%");

        //电池充电状态显示
        String chargeStatus = enjoySDK.chargeStatus();
        TextView chargeStatus_tv = findViewById(R.id.chargingStatus_tv);
        chargeStatus_tv.setText(chargeStatus);


        //系统关机shutdown,uncompleted
        Button shutdown = findViewById(R.id.shutdown_bt);
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean confirm = false;
                enjoySDK.shutdown(confirm);
            }
        });

    }
    //注销低电量警告
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在活动销毁时注销低电量广播接收器
        enjoySDK.unregisterLowBatteryReceiver();
    }
}