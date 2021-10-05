
package com.tyfone.smartcardpersotool.model.perso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthData {

    @SerializedName("dataEncrypted")
    @Expose
    private boolean dataEncrypted;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("dataSignature")
    @Expose
    private String dataSignature;

    public boolean isDataEncrypted() {
        return dataEncrypted;
    }

    public void setDataEncrypted(boolean dataEncrypted) {
        this.dataEncrypted = dataEncrypted;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataSignature() {
        return dataSignature;
    }

    public void setDataSignature(String dataSignature) {
        this.dataSignature = dataSignature;
    }

}
