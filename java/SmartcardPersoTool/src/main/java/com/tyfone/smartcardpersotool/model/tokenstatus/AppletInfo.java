
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppletInfo {

    @SerializedName("packageId")
    @Expose
    private Object packageId;
    @SerializedName("aid")
    @Expose
    private String aid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("binaryValue")
    @Expose
    private Object binaryValue;
    @SerializedName("binaryLocation")
    @Expose
    private Object binaryLocation;
    @SerializedName("binaryChecksum")
    @Expose
    private Object binaryChecksum;

    public Object getPackageId() {
        return packageId;
    }

    public void setPackageId(Object packageId) {
        this.packageId = packageId;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getBinaryValue() {
        return binaryValue;
    }

    public void setBinaryValue(Object binaryValue) {
        this.binaryValue = binaryValue;
    }

    public Object getBinaryLocation() {
        return binaryLocation;
    }

    public void setBinaryLocation(Object binaryLocation) {
        this.binaryLocation = binaryLocation;
    }

    public Object getBinaryChecksum() {
        return binaryChecksum;
    }

    public void setBinaryChecksum(Object binaryChecksum) {
        this.binaryChecksum = binaryChecksum;
    }

}
