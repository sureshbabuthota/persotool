
package com.tyfone.smartcardpersotool.model.tokenstatus;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenStatus {

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("fwVersion")
    @Expose
    private Object fwVersion;
    @SerializedName("manufactureDate")
    @Expose
    private long manufactureDate;
    @SerializedName("switchPartNumber")
    @Expose
    private String switchPartNumber;
    @SerializedName("hwTokenInfo")
    @Expose
    private HwTokenInfo hwTokenInfo;
    @SerializedName("setOfSecureElements")
    @Expose
    private List<SetOfSecureElement> setOfSecureElements = null;
    @SerializedName("setOfCryptoKeys")
    @Expose
    private List<Object> setOfCryptoKeys = null;
    @SerializedName("issuers")
    @Expose
    private Issuers issuers;
    @SerializedName("hwTokenBatteryInfo")
    @Expose
    private HwTokenBatteryInfo hwTokenBatteryInfo;
    @SerializedName("hwTokenLaminationInfo")
    @Expose
    private HwTokenLaminationInfo hwTokenLaminationInfo;
    @SerializedName("listOfStatus")
    @Expose
    private Object listOfStatus;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("bluetoothChip")
    @Expose
    private BluetoothChip bluetoothChip;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Object getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(Object fwVersion) {
        this.fwVersion = fwVersion;
    }

    public long getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(long manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getSwitchPartNumber() {
        return switchPartNumber;
    }

    public void setSwitchPartNumber(String switchPartNumber) {
        this.switchPartNumber = switchPartNumber;
    }

    public HwTokenInfo getHwTokenInfo() {
        return hwTokenInfo;
    }

    public void setHwTokenInfo(HwTokenInfo hwTokenInfo) {
        this.hwTokenInfo = hwTokenInfo;
    }

    public List<SetOfSecureElement> getSetOfSecureElements() {
        return setOfSecureElements;
    }

    public void setSetOfSecureElements(List<SetOfSecureElement> setOfSecureElements) {
        this.setOfSecureElements = setOfSecureElements;
    }

    public List<Object> getSetOfCryptoKeys() {
        return setOfCryptoKeys;
    }

    public void setSetOfCryptoKeys(List<Object> setOfCryptoKeys) {
        this.setOfCryptoKeys = setOfCryptoKeys;
    }

    public Issuers getIssuers() {
        return issuers;
    }

    public void setIssuers(Issuers issuers) {
        this.issuers = issuers;
    }

    public HwTokenBatteryInfo getHwTokenBatteryInfo() {
        return hwTokenBatteryInfo;
    }

    public void setHwTokenBatteryInfo(HwTokenBatteryInfo hwTokenBatteryInfo) {
        this.hwTokenBatteryInfo = hwTokenBatteryInfo;
    }

    public HwTokenLaminationInfo getHwTokenLaminationInfo() {
        return hwTokenLaminationInfo;
    }

    public void setHwTokenLaminationInfo(HwTokenLaminationInfo hwTokenLaminationInfo) {
        this.hwTokenLaminationInfo = hwTokenLaminationInfo;
    }

    public Object getListOfStatus() {
        return listOfStatus;
    }

    public void setListOfStatus(Object listOfStatus) {
        this.listOfStatus = listOfStatus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public BluetoothChip getBluetoothChip() {
        return bluetoothChip;
    }

    public void setBluetoothChip(BluetoothChip bluetoothChip) {
        this.bluetoothChip = bluetoothChip;
    }

}
