
package com.tyfone.smartcardpersotool.model.initperso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppletInfo {

    @SerializedName("aid")
    @Expose
    private String aid;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

}
