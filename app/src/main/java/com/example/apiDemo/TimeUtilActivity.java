package com.example.apiDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdk.QYSDK;

public class TimeUtilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tiem_util);

        QYSDK qySDK = new QYSDK(this);

        Button setTime_bt = findViewById(R.id.setTime_bt);
        setTime_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qySDK.setTime(15,0,0);
                qySDK.setDate(2024,6,8);
            }
        });
    }
}