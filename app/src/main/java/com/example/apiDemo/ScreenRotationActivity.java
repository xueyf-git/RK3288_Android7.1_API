package com.example.apiDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.QYSDK;

public class ScreenRotationActivity extends AppCompatActivity {
    private QYSDK qysdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen_rotation);

        qysdk =new QYSDK(this);

        //设置屏幕旋转角度
        Button setScreenRotation_bt = findViewById(R.id.setScreenRotate_bt);
        EditText setScreenRotation_et = findViewById(R.id.setScreenRotation_et);
        setScreenRotation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rotation_str = setScreenRotation_et.getText().toString().trim();
                if (!rotation_str.isEmpty()) {
                    try {
                        // 将文本内容转换为整数（如果输入的确实是数字）
                        int rotation = Integer.parseInt(rotation_str);
                        qysdk.setScreenRotation(rotation);

                    } catch (NumberFormatException e) {
                        setScreenRotation_et.setText("输入的数字不规范！请重新输入！");
                    }
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    setScreenRotation_et.setText("输入不能为空！请重新输入！");
                }
            }
        });

        //获取屏幕旋转角度
        Button getSystemRotation_bt = findViewById(R.id.getSystemRotation_bt);
        TextView getSystemRotation_tv = findViewById(R.id.getSystemRotation_tv);
        getSystemRotation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rotation = qysdk.getSystemRotation();
                getSystemRotation_tv.setText("当前屏幕旋转角度："+rotation);
            }
        });
    }
}