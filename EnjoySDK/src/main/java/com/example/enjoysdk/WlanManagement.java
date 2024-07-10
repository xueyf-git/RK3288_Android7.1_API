package com.example.enjoysdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WlanManagement {
    private final Activity mActivity;
    public WlanManagement(Activity activity) { this.mActivity = activity; }

    private WifiManager wifiManager;

    /**
     * wifi信息类
     */
    public static class WiFiInfo {
        private String ssid;
        private String bssid;
        private String ipAddress;
        private String gateway;
        private String netmask;
        private String dns1;
        private String dns2;
        private String macAddress;

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public void setBssid(String bssid) {
            this.bssid = bssid;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public void setNetmask(String netmask) {
            this.netmask = netmask;
        }

        public void setDns1(String dns1) {
            this.dns1 = dns1;
        }

        public void setDns2(String dns2) {
            this.dns2 = dns2;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getSsid() {
            return this.ssid;
        }

        public String getBssid() {
            return this.bssid;
        }

        public String getIpAddress() {
            return this.ipAddress;
        }

        public String getGateway() {
            return this.gateway;
        }

        public String getNetmask() {
            return this.netmask;
        }

        public String getDns1() {
            return this.dns1;
        }

        public String getDns2() {
            return this.dns2;
        }

        public String getMacAddress() {
            return this.macAddress;
        }

    }

    /**
     * 根据布尔参数切换wifi打开关闭
     * @param enable 配置参数（true 表示打开，false 表示关闭）
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int enableWiFi(boolean enable) {
        wifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        try {
            if (enable) {
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                    return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
                }
            } else {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
                }
            }
        } catch (Exception e) {
            return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
        return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
    }


    /**
     * 获取wifi列表
     * @return 成功返回wifi列表
     */
    public List<String> getWiFiList() {
        wifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        List<String> result = new ArrayList<>();
        Set<String> uniqueSSIDs = new HashSet<>();

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            @SuppressLint("MissingPermission") List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {
                String ssid = scanResult.SSID;
                if (ssid != null && !ssid.isEmpty() && !uniqueSSIDs.contains(ssid)) {
                    uniqueSSIDs.add(ssid);
                    result.add(ssid);
                }
            }
        } else {
            result.add("WiFi is disabled.");
        }
        for (String wifiInfo : result) {
            Log.d("getWiFiList", wifiInfo);
        }
        return result;
    }


    /**
     * 进行wifi连接
     * @param ssid ： wifi ssid
     * @param password ： wifi 密码
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int connectToWiFi(String ssid, String password) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        if (wifiManager.enableNetwork(netId, true) && wifiManager.reconnect()) {
            return EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } else {
            return EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    /**
     * 刷新wifi列表
     * @return 成功返回wifi列表
     */
    public List<String> refreshWiFiList() {
        List<String> result = getWiFiList();
        for (String wifiInfo : result) {
            Log.d("WiFiList", wifiInfo);
        }
        return result;
    }

    /**
     * 获取当前连接wifi信息
     * @return 成功返回wifi信息列表
     */
    public List<WiFiInfo> showConnectedWiFiInfo() {
        List<WiFiInfo> result = new ArrayList<>();
        if (wifiManager!= null && wifiManager.isWifiEnabled()) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            String ipAddress = Formatter.formatIpAddress(dhcpInfo.ipAddress);
            String gateway = Formatter.formatIpAddress(dhcpInfo.gateway);
            String netmask = Formatter.formatIpAddress(dhcpInfo.netmask);
            String dns1 = Formatter.formatIpAddress(dhcpInfo.dns1);
            String dns2 = Formatter.formatIpAddress(dhcpInfo.dns2);

            @SuppressLint("HardwareIds") String macAddress = wifiManager.getConnectionInfo().getMacAddress();
            String ssid = wifiManager.getConnectionInfo().getSSID();
            String bssid = wifiManager.getConnectionInfo().getBSSID();

            WiFiInfo wifiInfo = new WiFiInfo();
            wifiInfo.setSsid(ssid);
            wifiInfo.setBssid(bssid);
            wifiInfo.setIpAddress(ipAddress);
            wifiInfo.setGateway(gateway);
            wifiInfo.setNetmask(netmask);
            wifiInfo.setDns1(dns1);
            wifiInfo.setDns2(dns2);
            wifiInfo.setMacAddress(macAddress);

            result.add(wifiInfo);
        }
        return result;
    }

}
