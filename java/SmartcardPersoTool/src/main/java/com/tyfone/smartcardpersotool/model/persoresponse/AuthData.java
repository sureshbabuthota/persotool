
package com.tyfone.smartcardpersotool.model.persoresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthData {

    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("transactionText")
    @Expose
    private Object transactionText;
    @SerializedName("isNotificationSent")
    @Expose
    private Object isNotificationSent;
    @SerializedName("challenge")
    @Expose
    private String challenge;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getTransactionText() {
        return transactionText;
    }

    public void setTransactionText(Object transactionText) {
        this.transactionText = transactionText;
    }

    public Object getIsNotificationSent() {
        return isNotificationSent;
    }

    public void setIsNotificationSent(Object isNotificationSent) {
        this.isNotificationSent = isNotificationSent;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

}
