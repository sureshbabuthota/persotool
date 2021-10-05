
package com.tyfone.smartcardpersotool.model.persoresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChallengeInfo {

    @SerializedName("context")
    @Expose
    private String context;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("authData")
    @Expose
    private AuthData authData;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

}
