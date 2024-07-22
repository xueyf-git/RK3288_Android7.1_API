package com.example.sdk;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class EthernetManagement {
    private final Activity mActivity;
    private EthernetConfigure ethernetConfigure;

    public EthernetManagement(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // 开关 Ethernet
    public int switchEthernet(boolean enable, String ifname) {
        String command = enable ? "ifconfig " + ifname + " up" : "ifconfig " + ifname + " down";
        boolean success = executeCommand(command);
        if (success) {
            Log.d("EthernetConfig", command + " success!");
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } else {
            Log.e("EthernetConfig", command + " failed!");
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    // 获取 Ethernet 开关状况
    public boolean isEthernetEnable(String ifname) {
        String command = "ifconfig " + ifname;
        String output = executeCommandAndGetOutput(command);
        return output != null && output.contains("UP");
    }

    // 设置以太网接口配置
    public int setEthernetConfig(EthernetConfigure ethernetConfigure, String ifname) {
        if("DHCP".equalsIgnoreCase(ethernetConfigure.getMode())){
            return configureDhcp(ifname);
        }else {

            boolean ipConfigSuccess = configureEthernet(ifname, ethernetConfigure.getIPv4Address(), ethernetConfigure.getSubnetMask());
            boolean gatewayConfigSuccess = setGateway(ethernetConfigure.getGateway());
            boolean dnsConfigSuccess = setDns(ethernetConfigure.getDns(), ethernetConfigure.getBackupDns());

            if (ipConfigSuccess && gatewayConfigSuccess && dnsConfigSuccess) {
                Log.e("EthernetManagement", "Configuration success!");
                return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
            } else {
                Log.e("EthernetManagement", "Configuration failed!");
                return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
            }
        }
    }

    // 配置以太网接口 IP 地址和子网掩码
    private boolean configureEthernet(String ifname, String ipAddress, String netmask) {
        String command = "ifconfig " + ifname + " " + ipAddress + " netmask " + netmask;
        return executeCommand(command);
    }

    // 设置网关
    private boolean setGateway(String gateway) {
        String command = "route add default gw " + gateway;
        return executeCommand(command);
    }

    // 设置 DNS
    private boolean setDns(String dns, String backupDns) {
        String dnsCommand = "setprop net.dns1 " + dns;
        String backupDnsCommand = "setprop net.dns2 " + backupDns;
        return executeCommand(dnsCommand) && executeCommand(backupDnsCommand);
    }

    // 配置 DHCP 使用 busybox udhcpc
    private int configureDhcp(String ifname) {
        String command = "busybox udhcpc -i " + ifname;
        boolean success = executeCommand(command);
        if (success) {
            Log.d("EthernetConfig", "DHCP configuration success!");
            return McErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } else {
            Log.e("EthernetConfig", "DHCP configuration failed!");
            return McErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }

    //测试
    Enumeration<NetworkInterface> networkInterfaceEnumeration;
    public void DHCP()
    {
        try {
            networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            int count = 0;
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface next = networkInterfaceEnumeration.nextElement();
                Log.d("network","getName获得网络设备名称=" + next.getName());
                Log.d("network","getDisplayName获得网络设备显示名称=" + next.getDisplayName());
                Log.d("network","getIndex获得网络接口的索引=" + next.getIndex());
                Log.d("network","isUp是否已经开启并运行=" + next.isUp());
                Log.d("network","isBoopback是否为回调接口=" + next.isLoopback());
                Log.d("network","**********************" + count++);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    // 获取当前以太网配置
    public EthernetConfigure getEthernetConfig(String ifname) {
        String ipAddress = null;
        String subnetMask = null;
        String gateway = null;
        String dns = null;
        String backupDns = null;
        String mode = "STATIC";

        // 获取IP地址和子网掩码
        String ifconfigOutput = executeCommandAndGetOutput("ifconfig " + ifname);
        if (ifconfigOutput != null) {
            String[] lines = ifconfigOutput.split("\n");
            for (String line : lines) {
                if (line.trim().startsWith("inet addr:")) {
                    String[] parts = line.trim().split("\\s+");
                    for (String part : parts) {
                        if (part.startsWith("addr:")) {
                            ipAddress = part.split(":")[1];
                        } else if (part.startsWith("Mask:")) {
                            subnetMask = part.split(":")[1];
                        }
                    }
                }
            }
        }

        // 获取网关
        String routeOutput = executeCommandAndGetOutput("ip route");
        Log.d("EthernetConfig", "Route output: " + routeOutput);
        if (routeOutput != null) {
            String[] lines = routeOutput.split("\n");
            for (String line : lines) {
                if (line.startsWith("default via ")) {
                    gateway = line.split("\\s+")[2];
                    break;
                }
            }
        }

        // 获取 DNS
        dns = executeCommandAndGetOutput("getprop net.dns1");
        backupDns = executeCommandAndGetOutput("getprop net.dns2");

        // 检查 DHCP 客户端的运行状态以确定模式
        if (isDhcpEnabled(ifname)) {
            mode = "DHCP";
        }

        return new EthernetConfigure(mode, ipAddress, subnetMask, gateway, dns, backupDns);
    }

    // 检查 DHCP 客户端是否正在运行
    private boolean isDhcpEnabled(String ifname) {
        String dhcpOutput = executeCommandAndGetOutput("busybox ps | grep udhcpc");
        return dhcpOutput != null && dhcpOutput.contains("udhcpc -i " + ifname);
    }

    // 执行 shell 命令
    public static boolean executeCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            Log.e("EthernetConfig", command + " failed!", e);
            return false;
        } finally {
            closeResources(os, process);
        }
    }

    // 执行 shell 命令并获取输出
    private static String executeCommandAndGetOutput(String command) {
        Process process = null;
        DataOutputStream os = null;
        BufferedReader reader = null;
        StringBuilder output = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString();
        } catch (IOException | InterruptedException e) {
            Log.e("EthernetConfig", command + " failed!", e);
            return null;
        } finally {
            closeResources(os, reader, process);
        }
    }

    private static void closeResources(DataOutputStream os, Process process) {
        try {
            if (os != null) {
                os.close();
            }
            if (process != null) {
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeResources(DataOutputStream os, BufferedReader reader, Process process) {
        try {
            if (os != null) {
                os.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (process != null) {
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
