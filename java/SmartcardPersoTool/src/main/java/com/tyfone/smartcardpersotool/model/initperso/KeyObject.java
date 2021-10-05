
package com.tyfone.smartcardpersotool.model.initperso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyObject {

    @SerializedName("uncompressedPublicKey")
    @Expose
    private String uncompressedPublicKey;

    public String getUncompressedPublicKey() {
        return uncompressedPublicKey;
    }

    public void setUncompressedPublicKey(String uncompressedPublicKey) {
        this.uncompressedPublicKey = uncompressedPublicKey;
    }

}
