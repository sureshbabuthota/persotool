
package com.tyfone.smartcardpersotool.model.initperso;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetOfApplet {

    @SerializedName("unlockPin")
    @Expose
    private String unlockPin;
    @SerializedName("adminPin")
    @Expose
    private String adminPin;
    @SerializedName("installDate")
    @Expose
    private long installDate;
    @SerializedName("isCurrent")
    @Expose
    private boolean isCurrent;
    @SerializedName("updateMode")
    @Expose
    private String updateMode;
    @SerializedName("packageId")
    @Expose
    private String packageId;
    @SerializedName("appletId")
    @Expose
    private String appletId;
    @SerializedName("instanceId")
    @Expose
    private String instanceId;
    @SerializedName("tempAdminPin")
    @Expose
    private String tempAdminPin;
    @SerializedName("projectProvisioningScripts")
    @Expose
    private ProjectProvisioningScripts projectProvisioningScripts;
    @SerializedName("appletInfo")
    @Expose
    private AppletInfo appletInfo;
    @SerializedName("setOfCryptoKeys")
    @Expose
    private List<SetOfCryptoKey> setOfCryptoKeys = null;

    public String getUnlockPin() {
        return unlockPin;
    }

    public void setUnlockPin(String unlockPin) {
        this.unlockPin = unlockPin;
    }

    public String getAdminPin() {
        return adminPin;
    }

    public void setAdminPin(String adminPin) {
        this.adminPin = adminPin;
    }

    public long getInstallDate() {
        return installDate;
    }

    public void setInstallDate(long installDate) {
        this.installDate = installDate;
    }

    public boolean isIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
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

    public String getTempAdminPin() {
        return tempAdminPin;
    }

    public void setTempAdminPin(String tempAdminPin) {
        this.tempAdminPin = tempAdminPin;
    }

    public ProjectProvisioningScripts getProjectProvisioningScripts() {
        return projectProvisioningScripts;
    }

    public void setProjectProvisioningScripts(ProjectProvisioningScripts projectProvisioningScripts) {
        this.projectProvisioningScripts = projectProvisioningScripts;
    }

    public AppletInfo getAppletInfo() {
        return appletInfo;
    }

    public void setAppletInfo(AppletInfo appletInfo) {
        this.appletInfo = appletInfo;
    }

    public List<SetOfCryptoKey> getSetOfCryptoKeys() {
        return setOfCryptoKeys;
    }

    public void setSetOfCryptoKeys(List<SetOfCryptoKey> setOfCryptoKeys) {
        this.setOfCryptoKeys = setOfCryptoKeys;
    }

}
