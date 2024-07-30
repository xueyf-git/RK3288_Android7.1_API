package com.example.apiDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.McErrorCode;
import com.example.sdk.QYSDK;

public class BootAnimationActivity extends AppCompatActivity {
    QYSDK qysdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boot_animation);
        qysdk = new QYSDK(this);

        //设置开机动画
        Button setBootAnimation_bt = findViewById(R.id.setBootAnimation_bt);
        EditText setBootAnimation_et = findViewById(R.id.setBootAnimation_et);
        TextView setBootAnimation_tv = findViewById(R.id.setBootAnimationSuccess_tv);
        setBootAnimation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sourcePath = setBootAnimation_et.getText().toString().trim();
                if (!sourcePath.isEmpty()) {
                    int ret = qysdk.setBootAnimation(sourcePath);
                    if(ret == McErrorCode.ENJOY_COMMON_SUCCESSFUL)setBootAnimation_tv.setText("成功！");
                    else setBootAnimation_tv.setText("失败！");
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setBootAnimation_et.setText("输入不能为空！请重新输入！");
                }

            }
        });

        //重置开机动画
        Button resetBootAnimation_bt = findViewById(R.id.resetBootAnimation_bt);
        TextView resetBootAnimation_tv = findViewById(R.id.resetBootAnimationSuccess_tv);
        resetBootAnimation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ret = qysdk.resetBootAnimation();
                if(ret == McErrorCode.ENJOY_COMMON_SUCCESSFUL) resetBootAnimation_tv.setText("成功！");
                else resetBootAnimation_tv.setText("失败！");
            }
        });


        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BootAnimationActivity.this, MainActivity.class));
            }
        });

    }
}