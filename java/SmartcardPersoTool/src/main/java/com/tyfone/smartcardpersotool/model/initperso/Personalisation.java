
package com.tyfone.smartcardpersotool.model.initperso;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personalisation {

    @SerializedName("csn")
    @Expose
    private String csn;
    @SerializedName("enablePrePerso")
    @Expose
    private boolean enablePrePerso;
    @SerializedName("prePersoFuse")
    @Expose
    private boolean prePersoFuse;
    @SerializedName("prePersoScripts")
    @Expose
    private PrePersoScripts prePersoScripts;
    @SerializedName("secureElementInfo")
    @Expose
    private SecureElementInfo secureElementInfo;
    @SerializedName("setOfApplets")
    @Expose
    private List<SetOfApplet> setOfApplets = null;

    public String getCsn() {
        return csn;
    }

    public void setCsn(String csn) {
        this.csn = csn;
    }

    public boolean isEnablePrePerso() {
        return enablePrePerso;
    }

    public void setEnablePrePerso(boolean enablePrePerso) {
        this.enablePrePerso = enablePrePerso;
    }

    public boolean isPrePersoFuse() {
        return prePersoFuse;
    }

    public void setPrePersoFuse(boolean prePersoFuse) {
        this.prePersoFuse = prePersoFuse;
    }

    public PrePersoScripts getPrePersoScripts() {
        return prePersoScripts;
    }

    public void setPrePersoScripts(PrePersoScripts prePersoScripts) {
        this.prePersoScripts = prePersoScripts;
    }

    public SecureElementInfo getSecureElementInfo() {
        return secureElementInfo;
    }

    public void setSecureElementInfo(SecureElementInfo secureElementInfo) {
        this.secureElementInfo = secureElementInfo;
    }

    public List<SetOfApplet> getSetOfApplets() {
        return setOfApplets;
    }

    public void setSetOfApplets(List<SetOfApplet> setOfApplets) {
        this.setOfApplets = setOfApplets;
    }

}
