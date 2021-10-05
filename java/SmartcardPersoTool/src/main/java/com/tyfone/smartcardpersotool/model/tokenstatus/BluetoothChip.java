
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BluetoothChip {

    @SerializedName("macId")
    @Expose
    private String macId;
    @SerializedName("serialNumber")
    @Expose
    private Object serialNumber;
    @SerializedName("nvmChecksum")
    @Expose
    private Object nvmChecksum;
    @SerializedName("friendlyName")
    @Expose
    private String friendlyName;
    @SerializedName("partNumber")
    @Expose
    private String partNumber;
    @SerializedName("manufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("enableOta")
    @Expose
    private Object enableOta;
    @SerializedName("nvmFormatVersion")
    @Expose
    private String nvmFormatVersion;
    @SerializedName("firmwareVersion")
    @Expose
    private String firmwareVersion;
    @SerializedName("advertisingTimeout")
    @Expose
    private long advertisingTimeout;
    @SerializedName("connectionTimeout")
    @Expose
    private long connectionTimeout;

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public Object getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Object serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Object getNvmChecksum() {
        return nvmChecksum;
    }

    public void setNvmChecksum(Object nvmChecksum) {
        this.nvmChecksum = nvmChecksum;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Object getEnableOta() {
        return enableOta;
    }

    public void setEnableOta(Object enableOta) {
        this.enableOta = enableOta;
    }

    public String getNvmFormatVersion() {
        return nvmFormatVersion;
    }

    public void setNvmFormatVersion(String nvmFormatVersion) {
        this.nvmFormatVersion = nvmFormatVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public long getAdvertisingTimeout() {
        return advertisingTimeout;
    }

    public void setAdvertisingTimeout(long advertisingTimeout) {
        this.advertisingTimeout = advertisingTimeout;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

}
