
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issuers {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("masterKey")
    @Expose
    private Object masterKey;
    @SerializedName("internalId")
    @Expose
    private Object internalId;
    @SerializedName("projectId")
    @Expose
    private String projectId;
    @SerializedName("publicKey")
    @Expose
    private Object publicKey;
    @SerializedName("privateKey")
    @Expose
    private Object privateKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(Object masterKey) {
        this.masterKey = masterKey;
    }

    public Object getInternalId() {
        return internalId;
    }

    public void setInternalId(Object internalId) {
        this.internalId = internalId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Object getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Object publicKey) {
        this.publicKey = publicKey;
    }

    public Object getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Object privateKey) {
        this.privateKey = privateKey;
    }

}
