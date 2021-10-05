
package com.tyfone.smartcardpersotool.model.persoresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersoResponse {

    @SerializedName("adminPin")
    @Expose
    private Object adminPin;
    @SerializedName("hwToken")
    @Expose
    private Object hwToken;
    @SerializedName("challengeInfo")
    @Expose
    private ChallengeInfo challengeInfo;

    public Object getAdminPin() {
        return adminPin;
    }

    public void setAdminPin(Object adminPin) {
        this.adminPin = adminPin;
    }

    public Object getHwToken() {
        return hwToken;
    }

    public void setHwToken(Object hwToken) {
        this.hwToken = hwToken;
    }

    public ChallengeInfo getChallengeInfo() {
        return challengeInfo;
    }

    public void setChallengeInfo(ChallengeInfo challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

}
