
package com.tyfone.smartcardpersotool.model.initperso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetOfCryptoKey {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("keyType")
    @Expose
    private String keyType;
    @SerializedName("keyObject")
    @Expose
    private KeyObject keyObject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public KeyObject getKeyObject() {
        return keyObject;
    }

    public void setKeyObject(KeyObject keyObject) {
        this.keyObject = keyObject;
    }

}
