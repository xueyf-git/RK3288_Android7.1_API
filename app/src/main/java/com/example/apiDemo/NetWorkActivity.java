package com.example.apiDemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sdk.EthernetConfigure;
import com.example.sdk.EthernetManagement;
import com.example.sdk.McErrorCode;
import com.example.sdk.QYSDK;
import com.example.sdk.WlanManagement;

import java.util.List;

public class NetWorkActivity extends AppCompatActivity {
    private ListView wifiListView;
    private List<String> wifiList;
    QYSDK qySDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_net_work);
        qySDK = new QYSDK(this);


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

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NetWorkActivity.this);
                dialogBuilder.setTitle("输入 WiFi 密码");

                final EditText input = new EditText(NetWorkActivity.this);
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

        EthernetManagement ethernetManagement = new EthernetManagement(this);
        EthernetConfigure ethernetConfig = ethernetManagement.getEthernetConfig("eth0");

        //设置eth0网口ethernet开
        Button switchEthernet_enable_bt = findViewById(R.id.switchEthernet_enable_bt);
        switchEthernet_enable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ans = qySDK.switchEthernet(true,"eth0");
            }
        });

        //设置eth0网口ethernet关
        Button switchEthernet_disable_bt = findViewById(R.id.switchEthernet_disable_bt);
        switchEthernet_disable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ans = qySDK.switchEthernet(false,"eth0");
            }
        });

        Button isEthernetEnable_bt = findViewById(R.id.isEthernetEnbale_bt);
        TextView isEthernetEnable_tv = findViewById(R.id.isEthernetEnbale_tv);
        isEthernetEnable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ans = qySDK.isEthernetEnable("eth0");
                isEthernetEnable_tv.setText("当前Ethernet开关状态为："+ans);
            }
        });

        //获取EthernetConfig
        Button getEthernetConfig_bt = findViewById(R.id.getEthernetConfig_bt);
        getEthernetConfig_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 使用获取的Ethernet配置
                Log.d("EthernetConfig", "IP Address: " + ethernetConfig.getIPv4Address());
                Log.d("EthernetConfig", "Subnet Mask: " + ethernetConfig.getSubnetMask());
                Log.d("EthernetConfig", "Gateway: " + ethernetConfig.getGateway());
                Log.d("EthernetConfig", "DNS: " + ethernetConfig.getDns());
                Log.d("EthernetConfig", "Backup DNS: " + ethernetConfig.getBackupDns());
                Log.d("EthernetConfig","mode:"+ethernetConfig.getMode());
//                qysdk.DHCP();
            }
        });

        //设置EthernetConfig
        Button setEthernetConfig_bt = findViewById(R.id.setEthernetConfig_bt);
        setEthernetConfig_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EthernetConfigure eth = new EthernetConfigure("DHCP",null,null,null,null,null);
                int a = qySDK.setEthernetConfig(eth,"eth0");
                if(a == McErrorCode.ENJOY_COMMON_SUCCESSFUL){
                    Log.d("EthernetConfig","DHCP configuration applied successfully");
                } else{
                    Log.e("EthernetConfig","Failed to apply DHCP configuration");
                }
            }
        });

        //获取Mac地址
        Button getEthernetMacAddress_bt = findViewById(R.id.getEthernetMACAddress_bt);
        EditText getEthernetMacAddress_et = findViewById(R.id.getEthernetMACAddress_et);
        TextView getEthernetMacAddress_tv = findViewById(R.id.getEthernetMACAddress_tv);

        getEthernetMacAddress_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ifname = getEthernetMacAddress_et.getText().toString().trim();
                if (!ifname.isEmpty()) {
                    String mac = qySDK.getEthernetMacAddress(ifname);
                    getEthernetMacAddress_tv.setText(mac);
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    getEthernetMacAddress_et.setText("输入不能为空！请重新输入！");
                }
            }
        });

        //获取Ethernet连接状态
        Button getEthernetStatus_bt = findViewById(R.id.getEthernetStatus_bt);
        EditText getEthernetStatus_et = findViewById(R.id.getEthernetStatus_et);
        TextView getEthernetStatus_tv = findViewById(R.id.getEthernetStatus_tv);

        getEthernetStatus_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ifname = getEthernetStatus_et.getText().toString().trim();
                if (!ifname.isEmpty()) {
                    String mac = qySDK.getEthernetConnectState(ifname);
                    getEthernetStatus_tv.setText(mac);
                } else {
                    // 如果 EditText 为空，可以提示用户输入内容
                    getEthernetStatus_et.setText("输入不能为空！请重新输入！");
                }
            }
        });

        //获取所有以太网连接设备
        Button getEthernetDevices_bt = findViewById(R.id.getEthernetDevices_bt);
        TextView getEthernetDevices_tv = findViewById(R.id.getEthernetDevices_tv);
        getEthernetDevices_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ethernetDevices = qySDK.getEthernetDevices();
                StringBuilder arrayContent = new StringBuilder();

                if (ethernetDevices != null) {
                    for (String str : ethernetDevices) {
                        arrayContent.append(str).append("\n");
                    }
                } else {
                    arrayContent.append("No Ethernet devices found.");
                }
                getEthernetDevices_tv.setText(arrayContent.toString());
            }
        });

        ImageButton mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NetWorkActivity.this, MainActivity.class));
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