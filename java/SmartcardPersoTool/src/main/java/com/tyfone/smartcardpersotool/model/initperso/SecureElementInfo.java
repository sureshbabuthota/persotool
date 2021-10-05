
package com.tyfone.smartcardpersotool.model.initperso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecureElementInfo {

    @SerializedName("partNumber")
    @Expose
    private String partNumber;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

}
