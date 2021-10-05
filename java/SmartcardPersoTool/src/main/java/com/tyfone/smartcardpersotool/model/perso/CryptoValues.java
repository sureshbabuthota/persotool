
package com.tyfone.smartcardpersotool.model.perso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptoValues {

    @SerializedName("context")
    @Expose
    private String context;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("authData")
    @Expose
    private AuthData authData;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

}
