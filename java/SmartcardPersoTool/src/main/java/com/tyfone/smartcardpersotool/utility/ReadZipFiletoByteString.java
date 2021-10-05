/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.utility;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Kishan
 */
public class ReadZipFiletoByteString {

    public static void main(String[] args) {
        String zipFilePath = "E:\\Tyfone\\HardwareTokens\\cap\\hwtoken.cap";
        FileInputStream zipFile = null;
        File zipfptr = new File(zipFilePath);
        byte[] byteArray = new byte[(int)zipfptr.length()];
        try {
        zipFile = new FileInputStream(zipfptr);
        zipFile.read(byteArray);
        String beforeEncode = Utility.toHexString(byteArray);
        System.out.println("CAP File length "+beforeEncode.length());
        
        String text = Base64.encodeBase64String(byteArray);//.encodeBytes(byteArray);
        //String capFile = Utilities.bytesToHexString(byteArray);
        //System.out.println("CAP File "+text);
        String value = new String(byteArray, "UTF-8");
        System.out.println("CAP File "+text);
        }catch(Exception e){
            
        }
    }

}
