/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.utility;

import java.io.File;

/**
 *
 * @author Sureshbabu
 */
public class Utilities {
    public static String bytesToHexString(byte[] bytes) {
        String sbStr = "";
        try
        {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        sbStr = sb.toString();
        }
        catch(Exception e)
        {
            sbStr="";
        }
        return sbStr;
    }
    
    public static String bytesToHexString(byte[] bytes, int offset, int length) {
        String sbStr = "";
        try
        {
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i = offset; i < (offset+length); i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        sbStr = sb.toString();
        }
        catch(Exception e)
        {
            sbStr="";
        }
        return sbStr;
    }
    
    public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
    }
    
    /**
     * This method converts hex string to byte array
     * 
     * @param s - hex string
     * @return byte[] - hex string to byte array
     */
    public static byte[] hexStringToBytes(String s) {
        byte[] data=null;
        try
        {
            int len = s.length();
            data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                     + Character.digit(s.charAt(i+1), 16));
            }
        }
        catch(Exception e)
        {
            data=null;
        }
    return data;
    }
    /**
     * This method will convert byte to hexadecimal string.
     * 
     * @param b - single byte value
     * @return string - 2 chars length of he string
     */
    static public String toHex(byte b) {
        Integer I = new Integer((((int) b) << 24) >>> 24);
        int i = I.intValue();
        if (i < (byte) 16)
            return "0" + Integer.toString(i, 16);
        else
            return Integer.toString(i, 16);
    }
    
    /**
     * This method will convert hexadecimal string to ascii string
     * 
     * @param hexStr - hex string 
     * @return string - ascii string of byte values
     * 
     */
    public static String hexToAscii(String hexStr) {
    StringBuilder output = new StringBuilder("");
    try{
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
    }
    catch(Exception ex)
    {
    }         
    return output.toString();
}
    /**
     * This method will convert integer to hexadecimal string.
     * 
     * @param i - integer or long value
     * @return string - input integer value as 4 chars string  
     */
    static public String toHex(int i) {
        byte b[] = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >>> 24);
        b[1] = (byte) ((i & 0x00ff0000) >>> 16);
        b[2] = (byte) ((i & 0x0000ff00) >>> 8);
        b[3] = (byte) ((i & 0x000000ff));        
        return toHex(b[0]) + toHex(b[1]) + toHex(b[2]) + toHex(b[3]);
	}
    
    /**
    * Right way to delete a non empty directory in Java 
    * 
    * @param dir - folder path
    * @return bool - true on success, false on failure
    */
    public static boolean deleteDirectory(File dir) {
        boolean isDone=true;
	if (dir.isDirectory()) {
            File[] children = dir.listFiles(); 
            for (int i = 0; i < children.length; i++) {
            boolean success = deleteDirectory(children[i]); 
            if (!success) { 
                isDone = false; 
                break;
            } 
            } 
            isDone = dir.delete();
        }
        else
            isDone = dir.delete();
        
        return isDone;
    }

}
