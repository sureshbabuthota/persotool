/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.model.javacard;

import com.tyfone.smartcardpersotool.utility.Utilities;
import com.tyfone.smartcardpersotool.model.javacard.AppletUtility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Sureshbabu
 */
public class TAUPPersonalization {
    
    public boolean ValidateAppletFile(String capFilepath)
    {
        boolean isValid = false;  
        String tempDir=""; 
        String[] pkgids=null;
        
        AppletUtility appUtil = new AppletUtility();

        //extract the cap file
        String temp = System.getProperty("user.dir")+appUtil.OUTPUT_TEMP_DIR;
        tempDir = appUtil.extractCapFile(capFilepath, temp);

        if(tempDir == "")
            return isValid;
        
        //get the PACKAGE ID INFO
        File f = new File(tempDir + "\\Header.cap");
        if(f.exists())
            pkgids = appUtil.getPackageID(tempDir + "\\Header.cap");
        
        if(pkgids != null)
            isValid=true;
        
        //Delete tempXML folder and its content
        tempDir = System.getProperty("user.dir");
        tempDir += appUtil.OUTPUT_TEMP_DIR;
        Utilities.deleteDirectory(new File(tempDir));
        
        return isValid;
    }
    
    public boolean ValidatePrePersoFile(String txtfile)
    {
        boolean isValid = true;
        BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(txtfile));
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);                    
                    if(!line.startsWith("#")){
                        if(line.length()%2 != 0){
                            isValid = false;
                            break;
                        }
                        else if(!line.matches("^[0-9a-fA-F]+$")) {
                            isValid = false;
                            break;
                        }
                    }
                    // read next line
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                    isValid = false;
                    e.printStackTrace();
            }
        return isValid;
    }
    
}
