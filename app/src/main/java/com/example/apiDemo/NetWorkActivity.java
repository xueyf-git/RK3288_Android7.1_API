package com.example.apiDemo;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.sdk.EthernetConfigure;
import com.example.sdk.EthernetManagement;
import com.example.sdk.McErrorCode;
import com.example.sdk.QYSDK;
import com.example.sdk.WlanManagement;

import java.util.List;

public class NetWorkActivity extends AppCompatActivity implements com.example.apiDemo.InputDialogFragment.OnInputListener {
    private ListView wifiListView;
    private List<String> wifiList;
    QYSDK qySDK;
    EthernetManagement ethernetManagement;
    EthernetConfigure ethernetConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);
        qySDK = new QYSDK(this);
        ethernetManagement = new EthernetManagement(this);
        ethernetConfig = ethernetManagement.getEthernetConfig("eth0");

        //Wifi模块

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

        //Ethernet模块
        TextView getEthernetMacAddress_tv;
        TextView getEthernetStatus_tv;

        //获取Ethernet状态
        boolean eth_state = qySDK.isEthernetEnable("eth0");

        //获取Mac地址

        getEthernetMacAddress_tv = findViewById(R.id.macAddress_tv);
        String mac = qySDK.getEthernetMacAddress("eth0");
        getEthernetMacAddress_tv.setText(mac);

        //获取Ethernet连接状态
        getEthernetStatus_tv = findViewById(R.id.ethernetConnectionState_tv);
        String eth_st = qySDK.getEthernetConnectState("eth0");
        getEthernetStatus_tv.setText(eth_st);

        //设置Ethernet开关
        Switch switchEthernet = findViewById(R.id.ethernet_switch);
        switchEthernet.setChecked(eth_state);
        switchEthernet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int ans = qySDK.switchEthernet(isChecked, "eth0");
                if (ans == McErrorCode.ENJOY_COMMON_SUCCESSFUL) {
                    Toast.makeText(NetWorkActivity.this,
                            isChecked ? "Ethernet启用成功" : "Ethernet禁用成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NetWorkActivity.this,
                            isChecked ? "Ethernet启用失败" : "Ethernet禁用失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //获取EthernetConfig
        TextView ipAddress_tv = findViewById(R.id.ipAddress_tv);
        TextView subNetMask_tv = findViewById(R.id.subNetMask_tv);
        TextView gateway_tv =findViewById(R.id.gateway_tv);
        TextView DNS_tv = findViewById(R.id.DNS_tv);
        TextView backupDNS_tv = findViewById(R.id.backupDNS_tv);
        TextView mode_tv = findViewById(R.id.mode_tv);

        // 使用获取的Ethernet配置
        ipAddress_tv.setText(""+ethernetConfig.getIPv4Address());
        subNetMask_tv.setText(""+ethernetConfig.getSubnetMask());
        gateway_tv.setText(""+ethernetConfig.getGateway());
        DNS_tv.setText(""+ethernetConfig.getDns());
        backupDNS_tv.setText(""+ethernetConfig.getBackupDns());
        mode_tv.setText(""+ethernetConfig.getMode());

        //刷新
        ImageButton ethernetRefresh_bt = findViewById(R.id.ethernetRefresh_bt);
        ethernetRefresh_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 使用获取的Ethernet配置
                qySDK.getEthernetConfig("eth0");
                ipAddress_tv.setText(""+ethernetConfig.getIPv4Address());
                subNetMask_tv.setText(""+ethernetConfig.getSubnetMask());
                gateway_tv.setText(""+ethernetConfig.getGateway());
                DNS_tv.setText(""+ethernetConfig.getDns());
                backupDNS_tv.setText(""+ethernetConfig.getBackupDns());
                mode_tv.setText(""+ethernetConfig.getMode());
                getEthernetStatus_tv.setText(""+qySDK.getEthernetConnectState("eth0"));
            }
        });

        Button getEthernetConfigure_bt = findViewById(R.id.getEthernetConfigure_bt);
        getEthernetConfigure_bt.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            com.example.apiDemo.InputDialogFragment inputDialogFragment = new com.example.apiDemo.InputDialogFragment();
            inputDialogFragment.show(fragmentManager, "InputDialogFragment");
        });




        //获取所有以太网连接设备

        Button getEthernetDevices_bt = findViewById(R.id.getEthernetDevice_bt);
        TextView getEthernetDevices_tv = findViewById(R.id.ethernetDevice_tv);
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

    public void sendInput(String IdAddress, String SubnetMask,String Gateway,String DNS,String BackupDNS,String Mode) {

        ethernetConfig.setIPv4Address(IdAddress);
        ethernetConfig.setSubnetMask(SubnetMask);
        ethernetConfig.setGateway(Gateway);
        ethernetConfig.setDns(DNS);
        ethernetConfig.setBackupDns(BackupDNS);
        ethernetConfig.setMode(Mode);
        qySDK.setEthernetConfig(ethernetConfig,"eth0");
        Toast.makeText(this,IdAddress+"\n"+SubnetMask+"\n"+Gateway+"\n"+DNS+"\n"+BackupDNS+"\n"+Mode+"\n",Toast.LENGTH_SHORT).show();
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