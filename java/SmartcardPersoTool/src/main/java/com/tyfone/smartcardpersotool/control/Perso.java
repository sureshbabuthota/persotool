/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Kishan
 */
public class Perso {
    public static void main(String[] args){
        Perso obj = new Perso();
        try{
         String resp = obj.readConfigFile();
         System.out.println("Response "+resp);
        } catch(IOException ex){
            
        }
    }
  /* Reads the config file.
   * 
   * @return The string value of the ID
   * @throws IOException
   *           - if an error occurred when reading from the input stream.
   */
  private static String readConfigFile() throws IOException {
    Properties prop = new Properties();
    InputStream input = ClassLoader.getSystemResourceAsStream("config.properties");
    prop.load(input);
    return prop.getProperty("CM_AID");
  }
}
