
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetOfApplet {

    @SerializedName("unlockPin")
    @Expose
    private Object unlockPin;
    @SerializedName("adminPin")
    @Expose
    private Object adminPin;
    @SerializedName("installDate")
    @Expose
    private long installDate;
    @SerializedName("isCurrent")
    @Expose
    private Object isCurrent;
    @SerializedName("updateMode")
    @Expose
    private Object updateMode;
    @SerializedName("appletId")
    @Expose
    private String appletId;
    @SerializedName("instanceId")
    @Expose
    private String instanceId;
    @SerializedName("tempAdminPin")
    @Expose
    private Object tempAdminPin;
    @SerializedName("projectProvisioningScripts")
    @Expose
    private Object projectProvisioningScripts;
    @SerializedName("appletInfo")
    @Expose
    private AppletInfo appletInfo;
    @SerializedName("setOfCryptoKeys")
    @Expose
    private Object setOfCryptoKeys;

    public Object getUnlockPin() {
        return unlockPin;
    }

    public void setUnlockPin(Object unlockPin) {
        this.unlockPin = unlockPin;
    }

    public Object getAdminPin() {
        return adminPin;
    }

    public void setAdminPin(Object adminPin) {
        this.adminPin = adminPin;
    }

    public long getInstallDate() {
        return installDate;
    }

    public void setInstallDate(long installDate) {
        this.installDate = installDate;
    }

    public Object getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Object isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Object getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(Object updateMode) {
        this.updateMode = updateMode;
    }

    public String getAppletId() {
        return appletId;
    }

    public void setAppletId(String appletId) {
        this.appletId = appletId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Object getTempAdminPin() {
        return tempAdminPin;
    }

    public void setTempAdminPin(Object tempAdminPin) {
        this.tempAdminPin = tempAdminPin;
    }

    public Object getProjectProvisioningScripts() {
        return projectProvisioningScripts;
    }

    public void setProjectProvisioningScripts(Object projectProvisioningScripts) {
        this.projectProvisioningScripts = projectProvisioningScripts;
    }

    public AppletInfo getAppletInfo() {
        return appletInfo;
    }

    public void setAppletInfo(AppletInfo appletInfo) {
        this.appletInfo = appletInfo;
    }

    public Object getSetOfCryptoKeys() {
        return setOfCryptoKeys;
    }

    public void setSetOfCryptoKeys(Object setOfCryptoKeys) {
        this.setOfCryptoKeys = setOfCryptoKeys;
    }

}
