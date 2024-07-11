package com.example.henglangapi_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;

import android.widget.Switch;

import com.example.sdk.QYSDK;
import com.example.sdk.WlanManagement;


public class Wifi extends Activity {
    private ListView wifiListView;
    private List<String> wifiList;
    QYSDK qySDK = new QYSDK(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);


        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch wifiSwitch = findViewById(R.id.wifi_switch);
        wifiSwitch.setChecked(true);

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                qySDK.enableWiFi(isChecked);
                if (isChecked) {
                    wifiListView.setVisibility(View.VISIBLE);
                } else {
                    wifiListView.setVisibility(View.GONE);
                }

            }
        });

        wifiListView = findViewById(R.id.wifi_list_view);
        wifiList = qySDK.getWiFiList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
        wifiListView.setAdapter(adapter);


        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedSSID = wifiList.get(position);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Wifi.this);
                dialogBuilder.setTitle("输入 WiFi 密码");

                final EditText input = new EditText(Wifi.this);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        qySDK.connectToWiFi(selectedSSID, password);
                    }
                });

                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialogBuilder.show();
            }
        });

        Button refreshWifi = findViewById(R.id.refreshWifi);
        refreshWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wifiList = qySDK.refreshWiFiList();
                showWiFiInfo();
            }
        });


    }

    private void showWiFiInfo() {
        List<WlanManagement.WiFiInfo> result = qySDK.showConnectedWiFiInfo();

        StringBuilder infoBuilder = new StringBuilder();
        for (WlanManagement.WiFiInfo wifiInfo : result) {
            infoBuilder.append("SSID: ").append(wifiInfo.getSsid()).append("\n");
            infoBuilder.append("BSSID: ").append(wifiInfo.getBssid()).append("\n");
            infoBuilder.append("IP Address: ").append(wifiInfo.getIpAddress()).append("\n");
            infoBuilder.append("Gateway: ").append(wifiInfo.getGateway()).append("\n");
            infoBuilder.append("Netmask: ").append(wifiInfo.getNetmask()).append("\n");
            infoBuilder.append("DNS1: ").append(wifiInfo.getDns1()).append("\n");
            infoBuilder.append("DNS2: ").append(wifiInfo.getDns2()).append("\n");
            infoBuilder.append("MAC Address: ").append(wifiInfo.getMacAddress()).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WiFi Information");
        builder.setMessage(infoBuilder.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }


}
