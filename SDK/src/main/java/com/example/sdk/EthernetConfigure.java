package com.example.sdk;

public class EthernetConfigure {
    private String mode;
    private String IPv4Address;
    private String subnetMask;
    private String gateway;
    private String Dns;
    private String backupDns;


    public EthernetConfigure(String mode, String iPv4Address, String subnetMask, String gateway, String dns, String backupDns) {
        this.mode = mode;
        IPv4Address = iPv4Address;
        this.subnetMask = subnetMask;
        this.gateway = gateway;
        Dns = dns;
        this.backupDns = backupDns;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIPv4Address() {
        return IPv4Address;
    }

    public void setIPv4Address(String IPv4Address) {
        this.IPv4Address = IPv4Address;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return Dns;
    }

    public void setDns(String dns) {
        Dns = dns;
    }

    public String getBackupDns() {
        return backupDns;
    }

    public void setBackupDns(String backupDns) {
        this.backupDns = backupDns;
    }
}
