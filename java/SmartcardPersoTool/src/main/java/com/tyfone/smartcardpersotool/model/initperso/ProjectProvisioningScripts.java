
package com.tyfone.smartcardpersotool.model.initperso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectProvisioningScripts {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("version")
    @Expose
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
