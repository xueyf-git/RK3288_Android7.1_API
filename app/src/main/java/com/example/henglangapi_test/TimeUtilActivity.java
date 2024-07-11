package com.example.henglangapi_test;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.enjoysdk.EnjoySDK;

public class TimeUtilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tiem_util);

        EnjoySDK enjoySDK = new EnjoySDK(this);

        Button setTime_bt = findViewById(R.id.setTime_bt);
        setTime_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enjoySDK.setTime(15,0,0);
                enjoySDK.setDate(2024,6,8);
            }
        });
    }
}