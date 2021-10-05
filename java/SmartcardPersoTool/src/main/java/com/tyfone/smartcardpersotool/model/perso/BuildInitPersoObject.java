/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.model.perso;

/**
 *
 * @author Kishan
 */
public class BuildInitPersoObject {
    AuthData authDataObject;
    CryptoValues cryptoValueObject;
    
    public BuildInitPersoObject(){
        super();        
    }
    
    public CryptoValues buildObject(String data, String dataSignature){
        cryptoValueObject = new CryptoValues();
        authDataObject = new AuthData();
        
        cryptoValueObject.setContext("ST_ACTIVATION");
        cryptoValueObject.setType("HW_TOKEN_SIGNATURE");
        authDataObject.setData(data);
        authDataObject.setDataEncrypted(true);
        authDataObject.setDataSignature(dataSignature);
        cryptoValueObject.setAuthData(authDataObject);
        return cryptoValueObject;
    }
    
}
