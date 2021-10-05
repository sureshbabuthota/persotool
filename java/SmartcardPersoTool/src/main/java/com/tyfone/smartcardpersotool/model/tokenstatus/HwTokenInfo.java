
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HwTokenInfo {

    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("hwName")
    @Expose
    private String hwName;
    @SerializedName("hwVersion")
    @Expose
    private String hwVersion;
    @SerializedName("hwDescription")
    @Expose
    private Object hwDescription;
    @SerializedName("hwType")
    @Expose
    private String hwType;
    @SerializedName("pcbALot")
    @Expose
    private String pcbALot;
    @SerializedName("pcbLot")
    @Expose
    private String pcbLot;
    @SerializedName("cmLotId")
    @Expose
    private String cmLotId;
    @SerializedName("numberOfConfiguredTokens")
    @Expose
    private long numberOfConfiguredTokens;
    @SerializedName("numberOfManufacturedTokens")
    @Expose
    private long numberOfManufacturedTokens;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getHwName() {
        return hwName;
    }

    public void setHwName(String hwName) {
        this.hwName = hwName;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public Object getHwDescription() {
        return hwDescription;
    }

    public void setHwDescription(Object hwDescription) {
        this.hwDescription = hwDescription;
    }

    public String getHwType() {
        return hwType;
    }

    public void setHwType(String hwType) {
        this.hwType = hwType;
    }

    public String getPcbALot() {
        return pcbALot;
    }

    public void setPcbALot(String pcbALot) {
        this.pcbALot = pcbALot;
    }

    public String getPcbLot() {
        return pcbLot;
    }

    public void setPcbLot(String pcbLot) {
        this.pcbLot = pcbLot;
    }

    public String getCmLotId() {
        return cmLotId;
    }

    public void setCmLotId(String cmLotId) {
        this.cmLotId = cmLotId;
    }

    public long getNumberOfConfiguredTokens() {
        return numberOfConfiguredTokens;
    }

    public void setNumberOfConfiguredTokens(long numberOfConfiguredTokens) {
        this.numberOfConfiguredTokens = numberOfConfiguredTokens;
    }

    public long getNumberOfManufacturedTokens() {
        return numberOfManufacturedTokens;
    }

    public void setNumberOfManufacturedTokens(long numberOfManufacturedTokens) {
        this.numberOfManufacturedTokens = numberOfManufacturedTokens;
    }

}
