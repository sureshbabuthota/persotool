
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrePersoScripts {

    @SerializedName("fileContent")
    @Expose
    private Object fileContent;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("fileChecksum")
    @Expose
    private Object fileChecksum;
    @SerializedName("fileLocation")
    @Expose
    private Object fileLocation;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isCurrent")
    @Expose
    private Object isCurrent;

    public Object getFileContent() {
        return fileContent;
    }

    public void setFileContent(Object fileContent) {
        this.fileContent = fileContent;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(Object fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Object getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(Object fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Object isCurrent) {
        this.isCurrent = isCurrent;
    }

}
