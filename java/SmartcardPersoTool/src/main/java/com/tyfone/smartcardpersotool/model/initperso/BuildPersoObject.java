/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.model.initperso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kishan
 */
public class BuildPersoObject { 
    private Personalisation persoObject;
    private PrePersoScripts prePersoScriptObj;
    private SecureElementInfo seInfoObj;
    private List<SetOfApplet> setOfAppletList;
    private SetOfApplet setOfAppletObj;
    private List<SetOfCryptoKey> cryptoKeysList;
    private SetOfCryptoKey cryptoKeys;
    private KeyObject keyObject;
    private AppletInfo appletInfo;
    private ProjectProvisioningScripts provScript;
    
    public BuildPersoObject(){
        super();
    }
    
    public Personalisation buildObject(String CSN, String publicKey, String AID, String pkgID, String AdminPIN){
        persoObject = new Personalisation();
        persoObject.setCsn(CSN);
        persoObject.setEnablePrePerso(true);
        persoObject.setPrePersoFuse(true);
        
        cryptoKeys = new SetOfCryptoKey();
        keyObject = new KeyObject();
        keyObject.setUncompressedPublicKey(publicKey);
        cryptoKeys.setKeyObject(keyObject);
        cryptoKeys.setName("TRANSACTION_SIGNING_KEY");
        cryptoKeys.setKeyType("ECDSA_PUBLIC_KEY");
        cryptoKeysList = new ArrayList<SetOfCryptoKey>();
        cryptoKeysList.add(cryptoKeys);
        
        appletInfo = new AppletInfo();
        appletInfo.setAid(AID);
        
        setOfAppletList = new ArrayList<SetOfApplet>();
        setOfAppletObj = new SetOfApplet();
        setOfAppletObj.setUnlockPin("");
        setOfAppletObj.setUpdateMode("yes");
        setOfAppletObj.setIsCurrent(true);
        setOfAppletObj.setAdminPin(AdminPIN);
        setOfAppletObj.setAppletId(AID);
        setOfAppletObj.setInstanceId(AID);
        setOfAppletObj.setPackageId(pkgID);
        setOfAppletObj.setTempAdminPin(AdminPIN);
        provScript = new ProjectProvisioningScripts();
        provScript.setName("projectProvisioningScripts1");
        provScript.setVersion("1");
        setOfAppletObj.setProjectProvisioningScripts(provScript);        
        setOfAppletObj.setAppletInfo(appletInfo);
        setOfAppletObj.setSetOfCryptoKeys(cryptoKeysList);
        setOfAppletList.add(setOfAppletObj);
        
        seInfoObj = new SecureElementInfo();
        seInfoObj.setPartNumber("P5CD081");
        
        prePersoScriptObj = new PrePersoScripts();
        prePersoScriptObj.setVersion("3");
        prePersoScriptObj.setName("wetgddx");
        
        persoObject.setPrePersoScripts(prePersoScriptObj);
        persoObject.setSecureElementInfo(seInfoObj);
        persoObject.setSetOfApplets(setOfAppletList);
        System.out.println(persoObject.toString());
        return persoObject;               
    }
    
}
