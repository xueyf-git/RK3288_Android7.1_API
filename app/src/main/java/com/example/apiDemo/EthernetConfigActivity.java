package com.example.apiDemo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

public class EthernetConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ethernet_config);

        QYSDK qysdk = new QYSDK(this);
        EthernetManagement ethernetManagement = new EthernetManagement(this);
        EthernetConfigure ethernetConfig = ethernetManagement.getEthernetConfig("eth0");

        //设置eth0网口ethernet开
        Button switchEthernet_enable_bt = findViewById(R.id.switchEthernet_enable_bt);
        switchEthernet_enable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ans = qysdk.switchEthernet(true,"eth0");
            }
        });

        //设置eth0网口ethernet关
        Button switchEthernet_disable_bt = findViewById(R.id.switchEthernet_disable_bt);
        switchEthernet_disable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ans = qysdk.switchEthernet(false,"eth0");
            }
        });

        Button isEthernetEnable_bt = findViewById(R.id.isEthernetEnbale_bt);
        TextView isEthernetEnable_tv = findViewById(R.id.isEthernetEnbale_tv);
        isEthernetEnable_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ans = qysdk.isEthernetEnable("eth0");
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
                int a = qysdk.setEthernetConfig(eth,"eth0");
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
                    String mac = qysdk.getEthernetMacAddress(ifname);
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
                    String mac = qysdk.getEthernetConnectState(ifname);
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
                String[] ethernetDevices = qysdk.getEthernetDevices();
                StringBuilder arrayContent = new StringBuilder();
                for(String str : ethernetDevices){
                    arrayContent.append(str).append("\n");
                }
                getEthernetDevices_tv.setText(arrayContent.toString());
            }
        });


    }
}