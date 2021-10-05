/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.ui;
import org.json.simple.*;
/**
 *
 * @author Kishan
 */
public class JSONBuilder {
    protected JSONObject createJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("csn", "csn1");
        jsonObject.put("enablePrePerso",true);
        jsonObject.put("prePersoFuse",true);
        
        JSONObject prePersoScript = new JSONObject();
        prePersoScript.put("version","3");
        prePersoScript.put("name","wetgddx");        
        jsonObject.put("prePersoScripts", prePersoScript);
        
        JSONArray setOfApplets = new JSONArray();
        
        JSONObject appletInfo = new JSONObject();
        appletInfo.put("unlockPin","3030303030303030");
        appletInfo.put("adminPin","3030303030303030");
        appletInfo.put("installDate",1461053523828L);
        appletInfo.put("isCurrent",true);
        appletInfo.put("updateMode","yes");
        appletInfo.put("packageId","2432536425");
        appletInfo.put("appletId","6586868687686");
        appletInfo.put("instanceId","ytr76r789");
        appletInfo.put("tempAdminPin","654646546");
        /*projectProvisioningScripts":{ */
        JSONObject projectProvision = new JSONObject();
        projectProvision.put("name","projectProvisioningScripts1");
        projectProvision.put("version","1");
       
        appletInfo.put("projectProvisioningScripts", projectProvision);
               
        JSONObject appletInfoObj = new JSONObject();
        appletInfoObj.put("aid","798798");
        
        appletInfo.put("appletInfo", appletInfoObj);
        
        //"setOfCryptoKeys":[
        
        JSONObject secureElementInfo = new JSONObject();
        secureElementInfo.put("partNumber","76575");
        
        
        JSONArray cryptoKeysArray = new JSONArray();
        JSONObject cryptoKeys = new JSONObject();
        cryptoKeys.put("name","TRANSACTION_SIGNING_KEY");    
        cryptoKeys.put("keyType","ECDSA_PUBLIC_KEY");
         
        JSONObject keyObj = new JSONObject();
        keyObj.put("uncompressedPublicKey", "04aa28d8ce8dceb12fd3505447b483a8278261ffee27d262b0f39c28cf8360b815ab444346c193af0c83350dd997be1695252113dada87794f0f3350f4cd63e234");    
        cryptoKeys.put("keyObject",keyObj);   
        cryptoKeysArray.add(cryptoKeys);
        appletInfo.put("setOfCryptoKeys", cryptoKeysArray);
        
        setOfApplets.add(appletInfo);
        jsonObject.put("setOfApplets", setOfApplets); 
        jsonObject.put("secureElementInfo", secureElementInfo);
        return jsonObject;
    }
}
