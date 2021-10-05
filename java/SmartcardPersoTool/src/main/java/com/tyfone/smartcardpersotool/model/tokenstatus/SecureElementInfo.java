
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecureElementInfo {

    @SerializedName("partNumber")
    @Expose
    private String partNumber;
    @SerializedName("jcopPart")
    @Expose
    private String jcopPart;
    @SerializedName("jcopVersion")
    @Expose
    private String jcopVersion;
    @SerializedName("gpVersion")
    @Expose
    private String gpVersion;
    @SerializedName("jcVersion")
    @Expose
    private String jcVersion;
    @SerializedName("availableEeprom")
    @Expose
    private String availableEeprom;
    @SerializedName("availableRam")
    @Expose
    private String availableRam;
    @SerializedName("romInfoValue")
    @Expose
    private String romInfoValue;
    @SerializedName("manufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("transportKey")
    @Expose
    private Object transportKey;
    @SerializedName("transportKeyPgpId")
    @Expose
    private Object transportKeyPgpId;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getJcopPart() {
        return jcopPart;
    }

    public void setJcopPart(String jcopPart) {
        this.jcopPart = jcopPart;
    }

    public String getJcopVersion() {
        return jcopVersion;
    }

    public void setJcopVersion(String jcopVersion) {
        this.jcopVersion = jcopVersion;
    }

    public String getGpVersion() {
        return gpVersion;
    }

    public void setGpVersion(String gpVersion) {
        this.gpVersion = gpVersion;
    }

    public String getJcVersion() {
        return jcVersion;
    }

    public void setJcVersion(String jcVersion) {
        this.jcVersion = jcVersion;
    }

    public String getAvailableEeprom() {
        return availableEeprom;
    }

    public void setAvailableEeprom(String availableEeprom) {
        this.availableEeprom = availableEeprom;
    }

    public String getAvailableRam() {
        return availableRam;
    }

    public void setAvailableRam(String availableRam) {
        this.availableRam = availableRam;
    }

    public String getRomInfoValue() {
        return romInfoValue;
    }

    public void setRomInfoValue(String romInfoValue) {
        this.romInfoValue = romInfoValue;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Object getTransportKey() {
        return transportKey;
    }

    public void setTransportKey(Object transportKey) {
        this.transportKey = transportKey;
    }

    public Object getTransportKeyPgpId() {
        return transportKeyPgpId;
    }

    public void setTransportKeyPgpId(Object transportKeyPgpId) {
        this.transportKeyPgpId = transportKeyPgpId;
    }

}
