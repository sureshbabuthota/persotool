
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("issuanceMode")
    @Expose
    private Object issuanceMode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Object getIssuanceMode() {
        return issuanceMode;
    }

    public void setIssuanceMode(Object issuanceMode) {
        this.issuanceMode = issuanceMode;
    }

}
