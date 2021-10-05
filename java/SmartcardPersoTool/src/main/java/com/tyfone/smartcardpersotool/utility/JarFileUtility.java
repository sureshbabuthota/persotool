package com.tyfone.smartcardpersotool.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
   
/**
 * This class will provide the ability to access the contents of the JAR file.
 */
public class JarFileUtility{    
    public static String OutputTempDir = "\\tempXML";
    public static String [] capList = {"Header.cap", "Directory.cap","Import.cap", "Applet.cap", "Class.cap", 
                         "Method.cap", "StaticField.cap","ConstantPool.cap", "RefLocation.cap"};

    
    File dir;    
    String directoryPath;
    
    /** Creates a new instance of JarFileUtility 
     *  And it creates the temp directory in the current user directory.
     */
    public JarFileUtility() {
             String curentDir = System.getProperty("user.dir");
             File currentFile = new File(curentDir);   
             directoryPath =  currentFile.getAbsolutePath().concat(OutputTempDir);
             dir = new File(directoryPath);
             dir.mkdir();
    }
    /**
     * Retriews the data contents from the specified fileName which exist inside 
     * the CAP file or JAR file.
     * @param fileName of the file which exist inside the CAP file
     * @return Zero on success and one on failure.
     */
    private java.util.ArrayList<String> jarFiles;
    int showJar(String fileName) {
        JarFile file;
        jarFiles = new java.util.ArrayList<String>();    
        
        try {
            file = new JarFile(fileName);
            
            Enumeration entries = file.entries();
            
            while(entries.hasMoreElements()) {
                
                JarEntry entryName = ((JarEntry)entries.nextElement());
                String name = entryName.getName();
                
                String nameFile = getFile(name);
                jarFiles.add(nameFile);
                if(nameFile != null){
                    String strURL = "jar:file:/"+fileName+ "!/" + name;
                    URL entryURL = new URL(strURL);
                    JarURLConnection conn = (JarURLConnection)entryURL.openConnection();
                    InputStream in = conn.getInputStream();
                    nameFile = directoryPath.concat("\\"+nameFile);
                    
                    File outFile = new File(nameFile);
                    FileOutputStream out = new FileOutputStream(outFile);
                    byte[] buffer = new byte[1024];
                    int size = 0;
                    while((size = in.read(buffer))>0) {
                        out.write(buffer,0,size);
                    }
                    out.close();
                    in.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        }
        return 0;
    }
    
    ArrayList<String> getJarFileName(){
        return jarFiles;
    }
    
    /**
     * This method will compare the inputed name with the required list of files 
     * If the name matches with any of it in the list then it will return the 
     * name as the output otherwise null.
     * @param filename - name of file
     * @return on success file name itself and on failure null
     */
    protected String getFile(String  filename) {
        StringTokenizer st = new StringTokenizer( filename, "/" );
        while(st.hasMoreElements()) {
            filename = st.nextToken();                 
        }
        
        for(String fileName:capList){
            if(fileName.equalsIgnoreCase(filename)){
                return fileName;
            }
        }
        return null;
    }
    
     /**
      * This method will delete all the files created in the tempXML folder.
      * @return boolean - true on success, false on failure
      */
     protected boolean  deleteFiles() {
         for(File file:dir.listFiles()){
            file.delete();
        }
        return dir.delete();       
    }
        
     /**
      * This method will return the full path of the temXML folder.
      * @return string - Absolute path of the tempXML directory.
      */
     protected String getDirPath(){
         return directoryPath;
     }
  
}
