package com.sap.ownmydevice.models;

/**
 * Created by i300291 on 06/08/15.
 */

public class Device {
    private String url;
    private String id;
    private String deviceName;
    private String deviceModel;
    private String deviceOS;
    private String deviceArch;
    private String deviceMem;
    private String deviceProc;
    private String deviceEquipID;

    public Device(String deviceName, String deviceModel, String deviceOS, String deviceArch, String deviceMem, String deviceProc, String deviceEquipID, String deviceOwnerID, String deviceCurrOwnerID) {
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.deviceOS = deviceOS;
        this.deviceArch = deviceArch;
        this.deviceMem = deviceMem;
        this.deviceProc = deviceProc;
        this.deviceEquipID = deviceEquipID;
        this.deviceOwnerID = deviceOwnerID;
        this.deviceCurrOwnerID = deviceCurrOwnerID;
    }

    private String deviceOwnerID;
    private String deviceCurrOwnerID;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceArch() {
        return deviceArch;
    }

    public void setDeviceArch(String deviceArch) {
        this.deviceArch = deviceArch;
    }

    public String getDeviceMem() {
        return deviceMem;
    }

    public void setDeviceMem(String deviceMem) {
        this.deviceMem = deviceMem;
    }

    public String getDeviceProc() {
        return deviceProc;
    }

    public void setDeviceProc(String deviceProc) {
        this.deviceProc = deviceProc;
    }

    public String getDeviceEquipID() {
        return deviceEquipID;
    }

    public void setDeviceEquipID(String deviceEquipID) {
        this.deviceEquipID = deviceEquipID;
    }

    public String getDeviceOwnerID() {
        return deviceOwnerID;
    }

    public void setDeviceOwnerID(String deviceOwnerID) {
        this.deviceOwnerID = deviceOwnerID;
    }

    public String getDeviceCurrOwnerID() {
        return deviceCurrOwnerID;
    }

    public void setDeviceCurrOwnerID(String deviceCurrOwnerID) {
        this.deviceCurrOwnerID = deviceCurrOwnerID;
    }
}
