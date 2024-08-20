package com.example.sdk;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EthernetManagement {
    private final Activity mActivity;
    private EthernetConfigure ethernetConfigure;
    private  static boolean isDhcp = false;
    Enumeration<NetworkInterface> networkInterfaceEnumeration;
    ArrayList<String> availableInterface = new ArrayList<>();
    String[] interfaces = null;

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


    //获取所有以太网端口设备
    public String[] getEthernetDevices() {
        try {
            networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface next = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> ias = next.getInetAddresses();
                while (ias.hasMoreElements()){
                    ia = ias.nextElement();
                    if(ia instanceof Inet6Address){
                        continue;
                    }
                    String ip = ia.getHostAddress();
                    if(next.getName().startsWith("eth")){
                        availableInterface.add(next.getName());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        int size = availableInterface.size();

        if (size > 0) {
            interfaces = new String[size];
            for (int i = 0; i < size; i++) {
                interfaces[i] = availableInterface.get(i);
            }
        }
        return interfaces;
    }

    // 获取当前以太网配置
    public EthernetConfigure getEthernetConfig(String ifname) {
        String ipAddress = null;
        String subnetMask = null;
        String gateway = null;
        String dns = null;
        String backupDns = null;
        String mode = "STATIC";

        try {
            // 获取 EthernetManager 实例
            Class<?> ethernetManagerCls = Class.forName("android.net.EthernetManager");
            //noinspection ResourceType
            Object ethernetManager = mActivity.getSystemService("ethernet");

            // 获取当前 IpConfiguration
            Method getConfigurationMethod = ethernetManagerCls.getDeclaredMethod("getConfiguration");
            Object ipConfiguration = getConfigurationMethod.invoke(ethernetManager);

            // 获取 IpAssignment 属性
            Field ipAssignmentField = ipConfiguration.getClass().getField("ipAssignment");
            Object ipAssignment = ipAssignmentField.get(ipConfiguration);

            if (ipAssignment.toString().equals("DHCP")) {
                mode = "DHCP";
            } else if (ipAssignment.toString().equals("STATIC")) {
                mode = "STATIC";
            }

            // 获取 IP 地址和子网掩码
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new EthernetConfigure(mode, ipAddress, subnetMask, gateway, dns, backupDns);
    }


    // 获取以太网MAC地址
    public String getEthernetMacAddress(String ifname){

        String ifconfigOutput = executeCommandAndGetOutput("ifconfig " + ifname);
        if (ifconfigOutput != null) {
            // 使用正则表达式匹配 MAC 地址
            Pattern pattern = Pattern.compile("((HWaddr|ether) ([0-9a-fA-F]{2}[:-]){5}([0-9a-fA-F]{2}))");
            Matcher matcher = pattern.matcher(ifconfigOutput);
            if (matcher.find()) {
                return matcher.group(0).split(" ")[1].trim();
            }
        }
        return null;
    }

    //获取以太网当前连接状态
    public String getEthernetConnectState(String ifname){
        String ifconfigOutput = executeCommandAndGetOutput("ifconfig " + ifname);
        if (ifconfigOutput != null) {
            boolean isUp = ifconfigOutput.contains("UP");
            boolean isRunning = ifconfigOutput.contains("RUNNING");
            boolean hasIp = ifconfigOutput.contains("inet addr:");

            if (hasIp) {
                return "CONNECTED";
            } else if (isUp && isRunning && !hasIp) {
                return "DISCONNECTING";
            } else if (isUp && !isRunning) {
                return "CONNECTING";
            } else if (!isUp) {
                return "DISCONNECTED";
            }
        }
        return "UNKNOWN";
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

    /**
     * 设置以太网动态获取IP
     */
    public static boolean setDynamicIp(Activity activity) {
        try {
            Class<?> ethernetManagerCls = Class.forName("android.net.EthernetManager");
            //获取EthernetManager实例
            //noinspection ResourceType
            Object ethManager = activity.getSystemService("ethernet");
            //创建IpConfiguration
            Class<?> ipConfigurationCls = Class.forName("android.net.IpConfiguration");
            Object ipConfiguration = ipConfigurationCls.newInstance();
            //获取ipAssignment、proxySettings的枚举值
            Map<String, Object> ipConfigurationEnum = getIpConfigurationEnum(ipConfigurationCls);
            //设置ipAssignment
            Field ipAssignment = ipConfigurationCls.getField("ipAssignment");
            ipAssignment.set(ipConfiguration, ipConfigurationEnum.get("IpAssignment.DHCP"));
            //设置proxySettings
            Field proxySettings = ipConfigurationCls.getField("proxySettings");
            proxySettings.set(ipConfiguration, ipConfigurationEnum.get("ProxySettings.NONE"));
            //获取EthernetManager的setConfiguration()
            Method setConfigurationMethod = ethernetManagerCls.getDeclaredMethod("setConfiguration", ipConfiguration.getClass());
            //设置动态IP
            setConfigurationMethod.invoke(ethManager, ipConfiguration);
            isDhcp = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置以太网静态IP地址
     *
     * @param address ip地址
     * @param mask    子网掩码
     * @param gate    网关
     * @param dns     dns
     * @param dns2    dns2
     */
    public static boolean setEthernetStaticIp(Activity activity, String address, String mask, String gate, String dns,String dns2) {
        try {
            Class<?> ethernetManagerCls = Class.forName("android.net.EthernetManager");
            //获取EthernetManager实例
            //noinspection ResourceType
            Object ethManager = activity.getSystemService("ethernet");
            //创建StaticIpConfiguration
            Object staticIpConfiguration = newStaticIpConfiguration(address, gate, mask, dns, dns2);
            //创建IpConfiguration
            Object ipConfiguration = newIpConfiguration(staticIpConfiguration);
            //获取EthernetManager的setConfiguration()
            Method setConfigurationMethod = ethernetManagerCls.getDeclaredMethod("setConfiguration", ipConfiguration.getClass());
            //保存静态ip设置
            saveIpSettings(activity, address, mask, gate, dns, dns2);
            //设置静态IP
            setConfigurationMethod.invoke(ethManager, ipConfiguration);
            isDhcp = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取StaticIpConfiguration实例
     */
    private static Object newStaticIpConfiguration(String address, String gate, String mask, String dns,String dns2) throws Exception {
        Class<?> staticIpConfigurationCls = Class.forName("android.net.StaticIpConfiguration");
        //实例化StaticIpConfiguration
        Object staticIpConfiguration = staticIpConfigurationCls.newInstance();
        Field ipAddress = staticIpConfigurationCls.getField("ipAddress");
        Field gateway = staticIpConfigurationCls.getField("gateway");
        Field domains = staticIpConfigurationCls.getField("domains");
        Field dnsServers = staticIpConfigurationCls.getField("dnsServers");
        //设置ipAddress
        ipAddress.set(staticIpConfiguration, newLinkAddress(address, mask));
        //设置网关
        gateway.set(staticIpConfiguration, InetAddress.getByName(gate));
        //设置掩码
        domains.set(staticIpConfiguration, mask);
        //设置dns
        ArrayList<InetAddress> dnsList = (ArrayList<InetAddress>) dnsServers.get(staticIpConfiguration);
        dnsList.add(InetAddress.getByName(dns));

        // 添加第二个dns
        if (dns2 != null && !dns2.isEmpty()) {
            dnsList.add(InetAddress.getByName(dns2));
        }
        return staticIpConfiguration;
    }

    /**
     * 获取LinkAddress实例
     */
    private static Object newLinkAddress(String address, String mask) throws Exception {
        Class<?> linkAddressCls = Class.forName("android.net.LinkAddress");
        Constructor<?> linkAddressConstructor = linkAddressCls.getDeclaredConstructor(InetAddress.class, int.class);
        return linkAddressConstructor.newInstance(InetAddress.getByName(address), getPrefixLength(mask));
    }

    /**
     * 获取IpConfiguration实例
     */
    private static Object newIpConfiguration(Object staticIpConfiguration) throws Exception {
        Class<?> ipConfigurationCls = Class.forName("android.net.IpConfiguration");
        Object ipConfiguration = ipConfigurationCls.newInstance();
        //设置StaticIpConfiguration
        Field staticIpConfigurationField = ipConfigurationCls.getField("staticIpConfiguration");
        staticIpConfigurationField.set(ipConfiguration, staticIpConfiguration);
        //获取ipAssignment、proxySettings的枚举值
        Map<String, Object> ipConfigurationEnum = getIpConfigurationEnum(ipConfigurationCls);
        //设置ipAssignment
        Field ipAssignment = ipConfigurationCls.getField("ipAssignment");
        ipAssignment.set(ipConfiguration, ipConfigurationEnum.get("IpAssignment.STATIC"));
        //设置proxySettings
        Field proxySettings = ipConfigurationCls.getField("proxySettings");
        proxySettings.set(ipConfiguration, ipConfigurationEnum.get("ProxySettings.STATIC"));
        return ipConfiguration;
    }

    /**
     * 获取IpConfiguration的枚举值
     */
    private static Map<String, Object> getIpConfigurationEnum(Class<?> ipConfigurationCls) {
        Map<String, Object> enumMap = new HashMap<>();
        Class<?>[] enumClass = ipConfigurationCls.getDeclaredClasses();
        for (Class<?> enumC : enumClass) {
            Object[] enumConstants = enumC.getEnumConstants();
            if (enumConstants == null) continue;
            for (Object enu : enumConstants) {
                enumMap.put(enumC.getSimpleName() + "." + enu.toString(), enu);
            }
        }
        return enumMap;
    }

    /**
     * 保存静态ip设置
     */
    private static void saveIpSettings(Activity activity, String address, String mask, String gate, String dns, String dns2) {
        ContentResolver contentResolver = activity.getContentResolver();
        Settings.Global.putString(contentResolver, "ethernet_static_ip", address);
        Settings.Global.putString(contentResolver, "ethernet_static_mask", mask);
        Settings.Global.putString(contentResolver, "ethernet_static_gateway", gate);
        Settings.Global.putString(contentResolver, "ethernet_static_dns1", dns);
        Settings.Global.putString(contentResolver, "ethernet_static_dns2", dns2);
    }

    /**
     * 获取长度
     */
    private static int getPrefixLength(String mask) {
        String[] strs = mask.split("\\.");
        int count = 0;
        for (String str : strs) {
            if (str.equals("255")) {
                ++count;
            }
        }
        return count * 8;
    }
}
