/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.model.javacard;

import com.tyfone.smartcardpersotool.utility.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/************************************************************************************************************************
* Follow the below steps to Load/Delete an Applet
************************************************************************************************************************
*  Before we Load/Delete an Applet, we need to authenticate with the JavaCard Manager
*  and then execute the list of APDUs to Load or Delete the Applet.
*  
*  As part of the JavaCard Manager Mutual Authentication we need to below steps:
*  Mutual authentication makes use of the 3 DES-ECB 192 bit key. 
*  Both the host and the JavaCard must use the same set of 3-DES-ECB keys to authenticate.
* 
* 1. First generate 8 bytes Random number, we follow the following steps to generate 
*    Random number:
*    temp = '' + datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
*   *temp = "2018.01.09 12:19:47"
*    Random_number = md5.new(temp).hexdigest() * <- 8 bytes
* 
* 2. Frame an APDU as below
*   APDU to card = "8050000008"+randomnumber
* 
* 3. Transmit the above APDU to the SideCard and get the response
*   cardresponse = "00005026009195977187FF02000C8A702B8428CEB0D1D80B2FA592AE9000"
* 
* 4. Get the SideCard's tranport key
*   DES3_CBC_KEY = "404142434445464748494a4b4c4d4e4f4041424344454647"
* 5. Call the 'getCardAuthenticateAPDU' method to get the card manager authenticate APDU
*	 auth_apdu = getCardAuthenticateAPDU(randomnumber, cardresponse, DES3_CBC_KEY)
* 6. Now, select cardmanager and transmit the auth_apdu to the SideCard to authenticate the SideCard's cardmanager
* 7. On successful authentication, follow the below steps to load/delete applet into/from SideCard
* 8 Call 'getLoadAppletAPDUs' method to get the list of APDUs to load applet into SideCard
*   applet_file_path = 'C:/CSC/echo.cap'
*   aid - applet instance ID (optional)
*   appSpecificParam - application specific parameters (optional)
*   appPrivileges - applet specific privilege parameters (optional)
*   apdusList = getLoadAppletAPDUs(applet_file_path,'','','') * to load applet
*
*   Call 'getDeleteAppletAPDUs' method to get the list of APDUs to delete the applet from SideCard
*   applet_file_path = 'C:/CSC/echo.cap' (optional)
*   pkg_id - applet package ID (optional)
*   NOTE: any one argument is mandatory
*   apdusList = getDeleteAppletAPDUs(applet_file_path,'') * to delete applet
*
*
* 9. Transmit the lsit of APDUs into SideCard to load/delete the applet into/from SideCard
************************************************************************************************************************/

/**
 *
 * @author Sureshbabu
 */
public class AppletUtility {

public static final int CAP_HEADER_FILE_DATA_OFFSET_PACKAGE_AID_LENGTH = 12;
public static final int CAP_HEADER_FILE_DATA_OFFSET_PACKAGE_AID = 13;
public static final int CAP_APPLET_FILE_DATA_OFFSET_APPLET_COUNT = 3;

public static final int BLOCK_SIZE=400;
public static final int PKGID_LENGTH_OFFEST_STR = 24;
public static final int PKGID_LENGTH_END_STR = 26;
public static final int AID_APPLET_COUNT_OFFSET_STR = 6;
public static final int AID_APPLET_COUNT_END_STR = 8;

// ExtAuth Constants
public static final int APDU_HEADER_LENGTH = 5;
public static final int CMAC_LENGTH = 8;
public static final int OFFSET_CDATA = 10;
public static final int OFFSET_CLS = 2;
public static final int OFFSET_P2 = 8;

// Different types of Security levels
public static final int PLAIN = 0;
public static final int C_MAC = 1;
public static final int C_ENC = 3;
public static final int CMAC_ENC = 3;

public static final String ZERO_STR = "0";
public static final String HEX_ZERO = "00";
public static final String DELETE_APDU_CONST = "4F";
public static final String END_BLOCK = "80";
public static final String NOT_LAST_BLOCK = "00";

public static final String SECURITY_AID = "A000000151000000";
public static final String SECURITY_AID_PART = "000000";
public static final String LOAD_TAG_VALUE = "C482";
public static final String DEL_HEADER = "80E40000";
public static final String DEL_APDU_HEADER = "80E40080";
public static final String LOAD_CLS_INS = "80E8";
public static final String INSTALL_CLS_INS = "80E6";
public static final String INSTALL_HEADER = "80E60C00";
public static final String APP_PREV_VALUES = "00";
public static final String INSTALL_PARAM = "C900";
public static final String SELECT_JCOP_MANAGER_HEADER = "00A40400";
public static final String GET_CARD_INFO_APDU = "80F21000024F0000";
public static final String GET_INSTANCEs_ID_APDU = "80F24000024F0000";
//CAP file - sub files
public static final String[] capList = {"Header.cap","Directory.cap","Import.cap","Applet.cap","Class.cap","Method.cap","StaticField.cap","ConstantPool.cap","RefLocation.cap"};

public static final String OUTPUT_TEMP_DIR = "\\tempXML";   


public void AppletUtility(){    
}

public final class Packages {
    public String packageId=null;
    public String[] aid=null;
}

public ArrayList<Packages> pakegesList=null;

/********************************************************************************
* This method appends '0' to input string if the length of the input string is odd length
* @strData string with even length
********************************************************************************/
private String zeroFill(String strData){
    String length=strData;
    if(strData!= null)
    {
        if(strData.length() == 1){
            length = "0"+ strData;
            return length;
        }
        else if(strData.length()%2 != 0){
            length = "0" +  strData;
        }
                    
    }
    return length;
}

/********************************************************************************
* This method calculates input string and returns the length in hex string format
* @data - input hex string
* @returns input hex strings length in hex string format
********************************************************************************/
private String lengthInHex(String data){
    String length = null;
    int len = data.length()/2;
    length = Integer.toHexString(len);
    length = zeroFill(length);
    return length;
}

/*******************************************************************************/
/**
 * This method returns a smart card authenticate APDU
 * @param securityLevel - communication security level plain or encrypt. default will be plain
 * @param randomnumber - locally generated random number in  hex string
 * @param cardresponses - card response in hex string
 * @param des3_cbc_key - 24 bytes of key in hex string
 * @return an authenticate APDU string
 * 
 */
public String getCardAuthenticateAPDU(int securityLevel, String randomnumber, String cardresponses, String des3_cbc_key){
    String DES3_CBC_IV = "0000000000000000";
    String AUTH_APDU_HEADER = "8050000008";
    String CRYPTOGRAM_APDU_HEADER = "8482000010";
    //Eight byte padding values as required by the DES algorithm
    String PAD = "8000000000000000";
    byte[] pad = { (byte) 0x80, 0, 0, 0, 0, 0, 0, 0 };

    int STRLEN_8BYTES = 16;
    int SEQNO_START_POS_STR = 24;
    int CRYPTO_START_POS_STR = 40;
    int BYTE_LEN_STR = 2;

    //16 bytes
    byte[] derivationData = {0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    final byte[] ivAllZeros = { 0, 0, 0, 0, 0, 0, 0, 0 };
    boolean isAuthenticated = false;
    
    byte[] hostCrypto;
    byte[] cardCrypto;
    
    byte[] encSessionKey;
    byte[] macSessionKey;
    byte[] staticEncKeyBytes;
    byte[] CMAC=null;
            
    SecretKeySpec desKey;
       
    String host_cryptogram = "";
    String final_enc_str   = "";
    String ex_auth_apdu = null;
    
    if (randomnumber.length() < 1)
        return "";
    if ("".equals(cardresponses) || cardresponses.length() == 4)
        return "";
    else{
        try {        
            encSessionKey = new byte[24];
            macSessionKey = new byte[24];            
            cardCrypto = new byte[8];
            hostCrypto = new byte[8];

            byte[] cardRandom = new byte[8];
            byte[] sequenceNumber = new byte[2];

            byte[] cardResponse = Utilities.hexStringToBytes(cardresponses);
            byte[] hostRandom =   Utilities.hexStringToBytes(randomnumber);

//          String host_rand_no = randomnumber.substring(0,STRLEN_8BYTES);            

            // get the card random number from 28bytes of response
            System.arraycopy(cardResponse, 12, cardRandom, 0, 8);
            // get the sequence number 2 byte from 28bytes of response - 26:26+2
            System.arraycopy(cardResponse, 12, sequenceNumber, 0, 2);

            derivationData[2] = (byte)sequenceNumber[0];
            derivationData[3] = (byte)sequenceNumber[1];

            staticEncKeyBytes = Utilities.hexStringToBytes(des3_cbc_key);
            // Initialize to generate session keys
            desKey = new SecretKeySpec(staticEncKeyBytes, "DESede");
            Cipher cipher;
        
            cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
            IvParameterSpec ivSpec = new IvParameterSpec(ivAllZeros);
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);
            
            // Generate ENC session key
            derivationData[1] = (byte) 0x82;
            cipher.doFinal(derivationData, 0, derivationData.length, encSessionKey, 0);
            System.arraycopy(encSessionKey, 0, encSessionKey, 16, 8); // Make a
            // 192 bit
            // key out
            // of a 128
            // bit key
            
            // Generate C-MAC session key
            derivationData[1] =(byte) 0x01;
            cipher.doFinal(derivationData, 0, derivationData.length, macSessionKey, 0);
            System.arraycopy(macSessionKey, 0, macSessionKey, 16, 8); // Make a
            // 192 bit
            // key out
            // of a 128
            // bit key
            
            // Initialize key and cipher for Card Cryptogram creation
            desKey = new SecretKeySpec(encSessionKey, "DESede");
            cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
            
            // Gather data card cryptogram
            byte[] data = new byte[24];
            System.arraycopy(hostRandom, 0, data, 0, 8);
            System.arraycopy(cardRandom, 0, data, 8, 8);
            System.arraycopy(pad, 0, data, 16, 8);
                        
            // Generate Card Cryptogram. Use 8-LSB
            ivSpec = new IvParameterSpec(ivAllZeros);
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);
            byte[] cryptogram = cipher.doFinal(data);
            byte[] cardCryptogram = new byte[8];
            System.arraycopy(cryptogram, 16, cardCryptogram, 0, cardCryptogram.length);
            System.arraycopy(cardCryptogram,0, cardCrypto,0,cardCryptogram.length);
            
            // Gather data for host cryptogram
            System.arraycopy(cardRandom, 0, data, 0, 8);
            System.arraycopy(hostRandom, 0, data, 8, 8);
            System.arraycopy(pad, 0, data, 16, 8);
            
            // Generate Host Cryptogram. Use 8-LSB
            ivSpec = new IvParameterSpec(ivAllZeros);
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);
            cryptogram = cipher.doFinal(data);
            byte[] hostCryptogram = new byte[8];
            System.arraycopy(cryptogram, 16, hostCryptogram, 0, hostCryptogram.length);
            System.arraycopy(hostCryptogram,0, hostCrypto, 0, hostCryptogram.length);
            
            host_cryptogram = Utilities.bytesToHexString(hostCrypto);
                    
            System.out.println("Host Crypto : "+host_cryptogram);
            
            // Gather data for C-MAC
            data[0] = (byte) 0x84;
            data[1] = (byte) 0x82;
            data[2] = (byte)securityLevel;
            data[3] = 0;
            data[4] = 0x10;
            System.arraycopy(hostCryptogram, 0, data, 5, 8);
            System.arraycopy(pad, 0, data, 13, 3);
            
            // Initialize key and cipher for C-MAC
            // For SCP 02 you must encrypt the first n-1 block by single des.
            // Use the last 8 bytes of macSessionKey as the DES key.
            SecretKeySpec desSingleKey = new SecretKeySpec(macSessionKey, 0, 8, "DES");
            Cipher singleDesCipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
            
            // Calculate the first n - 1 block. For this case, n = 1
            ivSpec = new IvParameterSpec(ivAllZeros);
            singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
            byte ivForLastBlock[] = singleDesCipher.doFinal(data, 0, 8);
            
            SecretKeySpec encdesKey = new SecretKeySpec(macSessionKey, "DESede");
            cipher = Cipher.getInstance("DESede/CBC/NoPadding", "SunJCE");
            
            // Generate C-MAC. Use 8-LSB
            // For the last block, you can use TripleDES EDE with ECB mode, now I
            // select the CBC and
            // use the last block of the previous encryption result as ICV.
            ivSpec = new IvParameterSpec(ivForLastBlock);
            cipher.init(Cipher.ENCRYPT_MODE, encdesKey, ivSpec);
            byte tmpcMac[];
            tmpcMac = cipher.doFinal(data, 8, 8);
            CMAC = new byte[8];            
            System.arraycopy(tmpcMac,0,CMAC,0,tmpcMac.length);            
            
        } catch (NoSuchProviderException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException ex) {            
            System.out.println("Error in the AppletUtility ::"+ ex.getMessage());
        }
        
        if(CMAC!=null){
            final_enc_str =  Utilities.bytesToHexString(CMAC);

            ex_auth_apdu = CRYPTOGRAM_APDU_HEADER + host_cryptogram + final_enc_str.substring(0,STRLEN_8BYTES);
        }
    }

    return ex_auth_apdu;
    }

/**
 * This method processes selected cap file and returns the list of apdus to delete applet.
 * 
 * @param capFilepath - selected cap file path
 * @param pkg_id - applet package ID, hex string
 * @return string list of apdus on success or null on failure
 */
public ArrayList<String> getDeleteAppletAPDUs(String capFilepath, String pkg_id){
    String[] blockData;
    int numBlock;
    String[] PKGID;
    String[] AID;
    String[] installAPDU=null;
    String[] delAID;
    String uploadAPDU;
    int blockSize;
    int secLevel;

    String finalLoadDatas="";
    String hostRandomNumber = "";
    String cardResponse = "";
    secLevel = 0;
    blockSize = BLOCK_SIZE;
    Boolean deleteXML=true;
    ArrayList<String> capFileAPDUs=null;
    String tempDir="";  
    
    if (!pkg_id.isEmpty()){
        delAID = new String[1];    
	delAID[0] = getDeleteAPDU(pkg_id);
    }
    else
    {            //extract the cap file
        String temp = System.getProperty("user.dir")+OUTPUT_TEMP_DIR;
        tempDir = extractCapFile(capFilepath, temp);

        if(tempDir == "")
            return capFileAPDUs;

        //get the PACKAGE ID INFO
        File f = new File(tempDir + "\\Header.cap");
        if(f.exists())
            PKGID = getPackageID(tempDir + "\\Header.cap");
        else
            return capFileAPDUs;

        //et the Applet ID info
        f = new File(tempDir + "\\Applet.cap");
        if(f.exists())
            AID = getAppletInstanceId(tempDir + "\\Applet.cap");
        else
            return capFileAPDUs;

        if(AID != null)
            delAID = new String[AID.length + 1];
        else 
            delAID = new String[PKGID.length];

        if (AID.length > 0){
            //delAID will have the full APDU for deleting an Applet, after calling getDeleteAPDU method.
            for (int i=0; i<AID.length;i++){
                delAID[i] = getDeleteAPDU(AID[i]);
            }

            // Each cap file consists of only one package, thats why PKGID index is always zero.
            // Last delAID element will have the full APDU for deleting the Package.
            delAID[AID.length] = getDeleteAPDU(PKGID[0]);
        }
        else{
            delAID[0] = getDeleteAPDU(PKGID[0]);
        }
             
        //Delete tempXML folder and its content
        tempDir = System.getProperty("user.dir");
        tempDir += OUTPUT_TEMP_DIR;
        Utilities.deleteDirectory(new File(tempDir));
    }
    
    //delete applet
    //applet delete APDUs
    capFileAPDUs = frameDeleteAppletAPDUs(secLevel, delAID);
   
    
    return capFileAPDUs;
}
/********************************************************************************/
/**
 * 
 * Creates the list of Delete APDUs by reading the AIDs specified in the input
 * 
 * @param secLevel - communication security level plan or encrypt. default will be plain
 * @param delAID - list of package ID/Instance ID
 * @return Arraylist of APDU strings
 */
public ArrayList<String> frameDeleteAppletAPDUs(int secLevel, String[] delAID){
    int index=0;    
    String resStr = "";
    String data = "";
    int cnt = delAID.length;
    
    ArrayList<String> apdusList = new ArrayList<String>();
    /*-----------------------Delete APDU------------------*/
    for(index=0; index<cnt;index++)
        data = delAID[index];

    data = DEL_APDU_HEADER+data.substring(8);
    
    if( secLevel == CMAC_ENC || secLevel == C_MAC ){
        index=0;
        //ToDo: Form the encrypted data packet command.
        //resStr =  data; //encrypted data
    }else {
        resStr = data;
    }
    apdusList.add(resStr);
    return apdusList;
}

/**
 * This method Constructs the Delete APDU for the given package Id
 * 
 * @param packageId- selected applet Package ID
 * @return the APDU to delete the package as string
 */
public String getDeleteAPDU(String packageId){
         String length = lengthInHex(packageId);
         packageId = length.concat(packageId);
         packageId = DELETE_APDU_CONST.concat(packageId);
         length = lengthInHex(packageId);
         packageId = length.concat(packageId);
         packageId = DEL_HEADER.concat(packageId);
         return packageId;
    }

/*******************************************************************************/
/**
 * This method Constructs the Upload Command APDU using Package Id
 * 
 * @param packageId - Selected Applet Package ID
 * @param numBlock - Applet cap file binary data divided into no.of blocks
 * @return the Applet install APDU  as string
 */
public String getUploadAPDU(String packageId, int numBlock){
    String apdu = INSTALL_CLS_INS;
//    String len = zeroFill(Integer.toHexString((numBlock-1)));       
//    len = len.concat(HEX_ZERO);
    //TODO: len has not going to be concatniated .
    apdu = apdu.concat("0200");
    String part_apdu = lengthInHex(packageId);
    part_apdu = part_apdu.concat(packageId);
    String temp = lengthInHex(SECURITY_AID);
    part_apdu = part_apdu.concat(temp);
    part_apdu = part_apdu.concat(SECURITY_AID);
    part_apdu = part_apdu.concat(SECURITY_AID_PART);
    String length = lengthInHex(part_apdu);
    length=length.concat(part_apdu);
    apdu = apdu.concat(length);     
    return apdu;
}  

/**
 * Creates the list of APDUs to load the applet from the selected cap file
 * 
 * @param secLevel - communication security level plan or encrypt. default will be plain
 * @param delAID - list of package ID/Instance ID
 * @param uploadAPDU - applet upload APDU string
 * @param blockData - array of cap file data chunks
 * @param installAPDU - - applet install APDU string
 * @return list of apdu strings
 */
public ArrayList<String> LoadAppletAPDUs(int secLevel, String[] delAID, String uploadAPDU, String[] blockData, String[] installAPDU){
    int index = 0;
    ArrayList<String> apdusList;//String[] apdusList=null;
    apdusList = new ArrayList<>();
    String resStr = "";
    String deleteData = "";
    
    int aidLength = delAID.length;
    
    //apdusList = new String[cnt];
    /*------------------Delete APDU------------------*/
    for (index=0;index<aidLength;index++)
        deleteData = delAID[index];
    
    deleteData = DEL_APDU_HEADER  + deleteData.substring(8);
    if (secLevel == CMAC_ENC || secLevel == C_MAC){
        index=0;
        // TBD
        //deleteData = getEncrypedData(deleteData
        //resStr = deleteData
    }else{
        resStr = deleteData;
        apdusList.add(resStr);
        index += 1;
    }
    /* ----------------Upload APDU-------------------*/
    if (secLevel == CMAC_ENC || secLevel == C_MAC){
        index=0;
        // dataEnc = getEncrypedData(uploadAPDU)
        // resStr =  dataEnc
    }else{
        resStr = uploadAPDU;
    apdusList.add(resStr);
    index += 1;
    }
    
    /* ----------------Load APDU----------------------*/
    for (String blockData1 : blockData) {
        if (secLevel == CMAC_ENC || secLevel == C_MAC) {
            index=0;
            // dataEnc = getEncrypedData(blockData[i])
            // resStr =  dataEnc
        } else {
            resStr = blockData1;
            apdusList.add(resStr);
            index += 1;
        }
    }
    /* ----------------Install APDU---------------------*/
    if (installAPDU.length > 0){
        for (String installAPDU1 : installAPDU) {
            if (secLevel == CMAC_ENC || secLevel == C_MAC) {
                index=0;
                //TODO: Implement/Test encryption part.
                //dataEnc = getEncrypedData(installAPDU[i])
                //resStr = dataEnc
            } else {
                resStr = installAPDU1;
                apdusList.add(resStr);
                index += 1;
            }
        }    
    }
    
    return apdusList;
}

/**
 * This method parses the input data to set the privilege parameters
 * @param data - input data to parse the privilege
 * @return the privilege values as string
 */
private String calculatPrivilege(String data){
    int index = 0;
    int finalOutput = 0;   
    
    String[] priv = {"S", "V", "E", "L", "T", "D", "P", "M"};
    int [] value = {0x80, 0x40, 0x20, 0x10, 0x8, 0x4, 0x02, 0x1};

    while(index <= 7){
        boolean val = data.contains(priv[index]);
        if(val == true){
            finalOutput += value[index];
        }
        index++;
    }
    String res = Integer.toHexString(finalOutput);
    
   if(res.length()%2 != 0){
     res = "0".concat(res);
   }
    return res;
}


/*******************************************************************************/
/**
 * This method Constructs the Install Command APDU using Package Id and Applet Id
 * @param appletId - Applet ID or AID 
 * @param instanceId - Applet instance ID
 * @param appSpecificParam - Application specific params
 * @param appPrivileges - application previlages
 * @param packageId - selected applet Package ID
 * @return the Install APDU header with packageID and applet Id
 */
public String createInstallAPDU(String appletId, String instanceId, String appSpecificParam, String appPrivileges,String packageId){
 String apdu = "";
 String length = "";

 apdu = lengthInHex(packageId);
 apdu = apdu.concat(packageId);
 length = lengthInHex(appletId);
 length = length.concat(appletId);        

 if(appPrivileges == null && instanceId != null){
     String instId = "";            
     instId = lengthInHex(instanceId);
     instId = instId.concat(instanceId);                                
     length = length.concat(instId);
     apdu = apdu.concat(length);
     length = lengthInHex(APP_PREV_VALUES);
     length = length.concat(calculatPrivilege(APP_PREV_VALUES));//appPrivileges));            
 }
 if(appPrivileges != null){
     String instId = "";
     if(instanceId != null){
     instId = lengthInHex(instanceId);
     instId = instId.concat(instanceId);                                
     length = length.concat(instId);
     apdu = apdu.concat(length);
     length = lengthInHex(APP_PREV_VALUES);
     length = length.concat(calculatPrivilege(appPrivileges));
     } 
     else{
     //Attaching once again the length + AID
     length = length.concat(length);
     apdu = apdu.concat(length);
     length = lengthInHex(APP_PREV_VALUES);
     length = length.concat(calculatPrivilege(appPrivileges));            
     }
 } else if(instanceId == null && appPrivileges == null) {
     //Attaching once again the length + AID
     length = length.concat(length);
     apdu = apdu.concat(length);
     length = lengthInHex(APP_PREV_VALUES);
     length = length.concat(calculatPrivilege(APP_PREV_VALUES));//appPrivileges));            
}
 apdu = apdu.concat(length);
 if(appSpecificParam != null){
     String dataPart = lengthInHex(appSpecificParam);
     dataPart = "C9".concat(dataPart);
     length = lengthInHex(dataPart.concat(appSpecificParam));
     length = length.concat(dataPart);
     length = length.concat(appSpecificParam);
     apdu = apdu.concat(length);
 } else {
     length = lengthInHex(INSTALL_PARAM);
     length = length.concat(INSTALL_PARAM);
     apdu = apdu.concat(length);
 }

 //TODO: Find out the reason for this extra 00 byte in the INSTALL APDU
 apdu = apdu.concat(HEX_ZERO);
 length = lengthInHex(apdu);
 String installApdu = INSTALL_HEADER.concat(length);
 installApdu = installApdu.concat(apdu);
//        System.out.println(""+installApdu);
 return installApdu;
}
    
/*******************************************************************************/
/**
 * This method will create APDU header for each of the Upload data block.
 * 
 * @param blockData - input of cap file data as array of blocks
 * @return array of blocks, each block with APDU header
 */
private String[] blockDataWithHeader(String[] blockData){
    String apduHeader = null;
    String[] blocks = null;
    int numBlock = blockData.length;
    blocks = new String[numBlock];
    for(int blockNumber = 0; blockNumber < numBlock; blockNumber++){
        //Initialize tem with CLS and INS as 80E8
        apduHeader = LOAD_CLS_INS;

        if(blockNumber == (numBlock - 1)){
            apduHeader = apduHeader.concat(END_BLOCK);
        } else {
            apduHeader = apduHeader.concat(NOT_LAST_BLOCK);
        }

        String valueOfP2 = Integer.toHexString(blockNumber);
        valueOfP2 = zeroFill(valueOfP2);
        apduHeader = apduHeader.concat(valueOfP2);
        String lengthOfDataVal = lengthInHex(blockData[blockNumber]);             
        apduHeader = apduHeader.concat(lengthOfDataVal);
        blocks[blockNumber] = apduHeader.concat(blockData[blockNumber]);       
    }     

    return blocks;
}
/*******************************************************************************/
/**
 * This method Reads the file and returns the binary data
 * 
 * @param capfile - selected cap file
 * @return the binary data of passed cap file binary data
 */
public static byte[] getBytesFromFile(String capfile) {
        File file = new File(capfile);
        InputStream fileInputStreamObj;
         try {
             fileInputStreamObj = new FileInputStream(file);
             
             //Get the size of the file
             long fileSize = file.length();
             
             if(fileSize > Integer.MAX_VALUE){
                 //File fileInputStreamObj too large
                 //TODO: know how to handle
             }
             
             //Create the byte array to hold the data
             byte[] bytes = new byte[(int)fileSize];
             //Read in the bytes
             int offset = 0;
             int numRead = 0;
             
             while(offset < bytes.length && (numRead = fileInputStreamObj.read(bytes, offset,bytes.length - offset)) >=0){
                 offset += numRead;
             }
             //Ensure all the bytes have been read in
             if(offset <bytes.length){
                 throw new IOException("Could not completely read file "+file.getName());
             }
             //Close the input stream and return bytes
             fileInputStreamObj.close();
             return bytes;
         } catch (FileNotFoundException ex) {
             ex.printStackTrace();
         } catch (IOException e){
             e.printStackTrace();
         }
         return null;
     }
/*******************************************************************************
/**
 * This method retrieves the Package ID from the selected cap file's  file 'Applet.cap'
 * @param fileName - selected cap file's  file 'Applet.cap'
 * @return the selected applet's package ID
 */
public String[] getPackageID(String fileName){
    String[] pkgid=null;
    byte[] arrayBytes = getBytesFromFile(fileName);
    String inputString = Utilities.bytesToHexString(arrayBytes);

    String tag_str = inputString.substring(0,2);

    Integer tag = Integer.valueOf(tag_str,16);
    Integer size = Integer.valueOf(inputString.substring(2,6),16);

    int i = Integer.valueOf(inputString.substring(24,26),16);
    i *=2;
    pkgid = new String[2];
    pkgid[0] = inputString.substring(26, (26+i));
    if(inputString.length() > (26+i)){
        int j = Integer.valueOf(inputString.substring((26+i), (28+i)), 16);        
        if(j>=5){
        j *= 2;        
        pkgid[1] = inputString.substring((28+i), (28+i+j));
        }
        else{
            String temp = pkgid[0];
            pkgid = new String[1];
            pkgid[0] = temp;
        }
    }
    return pkgid;        
    }

/*******************************************************************************/
/**
 * This function will retrieves the Applet ID from the input string
 * @param fileName - 'Applet.cap' file path
 * @return the selected cap file's AppletID (AID)
 */
public  String[] getAppletInstanceId(String fileName){
    String[] aid=null;
    byte[] arrayBytes = getBytesFromFile(fileName);
    String inputString = Utilities.bytesToHexString(arrayBytes);
    String string = inputString.substring(0,2);
    Integer tag = Integer.valueOf(string,16);
    tag = Integer.valueOf(inputString.substring(2,6),16);
    int noOfApplets = Integer.valueOf(inputString.substring(6,8),16);
    int indexJ = 8 ,indexK =10;
    aid = new String[noOfApplets];
    for(int i = 0;i<noOfApplets;i++){
        int length = Integer.valueOf(inputString.substring(indexJ,indexK),16);
        length *= 2;
        aid[i]= inputString.substring(indexK,(length+indexK));
        length += 4;      
        indexJ = length+indexK;
        indexK = indexJ + 2;            
    }
    
    return aid;
}

/*******************************************************************************/
/**
 * This method prepares chunks of data of cap file binary data with specified block size
 * 
 * @param output - capfile binary data as input
 * @param blockSize - each block size e.g 400 chars...200bytes
 * @return array of blocks of capfile data
 */
public static String[] frameBlockData(String output, int blockSize) {
        if(output.length()<=1)
            return null;
        String[] blockData = null;
        
        int totalLength = output.length();
        totalLength /= 2;
        
        //Total length of the whole data block
        String hexadecimalValue = Integer.toHexString(totalLength);
        while(hexadecimalValue.length()<4){
            hexadecimalValue = ZERO_STR.concat(hexadecimalValue);
        }
        
        String partOutput = LOAD_TAG_VALUE.concat(hexadecimalValue);
        partOutput = partOutput.concat(output);
        
        int  startPos = 0;
        int  endPos = blockSize;
        int len = partOutput.length();
        int numBlock = (partOutput.length()/blockSize);
        int rem = partOutput.length() % blockSize;
        if(rem > 0)
            blockData = new String[numBlock+1];
        
        if(numBlock == 0 && totalLength > 10){
            blockData = new String[numBlock+1];
            blockData[numBlock] = partOutput;
        }
        for(int index = 0; index < numBlock; index++){            
            blockData[index] = partOutput.substring(startPos,endPos);
            startPos = endPos;
            int l = index + 1;
            if((index+1) < numBlock){ 
                endPos += blockSize;
            }
        }/* End of the for loop */
        
        if(endPos < (partOutput.length())){          
            blockData[numBlock] = partOutput.substring(endPos);
        }
        
        if(rem > 0) {
            numBlock++;
        }
        
        return blockData;
    }
/*******************************************************************************/
/**
 * This method reads  the files listed in the capList[] one by one as a sequence of bytes and
 * stores them in the String variable finalOutput.
 * @param tempDir - capfile extracted path
 * @param capList - list of capfiles to be read
 * @return cap file binary data as hex string
 */
public static String getcapFilesBinaryData(String tempDir, String[] capList){        
        byte[] output = null;
        String finalOutput = new String();        
        for(String filename: capList){
            String filepath = tempDir+ '/'+filename;
            //if(jarFiles.contains(file)){            
            output = getBytesFromFile(filepath);
            String temp = Utilities.bytesToHexString(output);
            finalOutput = finalOutput.concat(temp);
            //}
        }
        return finalOutput;
    }


/*******************************************************************************/
/**
 *  This method is to extract the selected cap file into sub cap files
 * 
 * @param filePath - selected cap  file path
 * @param destDir - cap file extract destination folder path...application path
 * @return cap file extracted directory path
 */
public static String extractCapFile(String filePath, String destDir){  
    String currentDir ="";
    File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(filePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                
                if(currentDir == "" && fileName.contains("Header.cap"))
                    currentDir = newFile.getParent();
                
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    return currentDir;
}

/*******************************************************************************/
/**
 * This method processes selected cap file and returns the list of apdus to load applet.
 * 
 * @param capFilepath - selected cap file path
 * @param instaid - applet instance AID, hex string
 * @param appSpecificParam - application specific parameters
 * @param appPrivileges- applet specific privilege parameters
 * @return string list of apdus on success or null on failure
 */
public ArrayList<String> getLoadAppletAPDUs(String capFilepath, String instaid, String appSpecificParam, String appPrivileges, String[] ID){
    //print 'cap file process start'
    String[] blockData;
    int numBlock;
    String[] PKGID;
    String[] AID;
    String[] installAPDU=null;
    String[] delAID;
    String uploadAPDU;
    int blockSize;
    int secLevel;

    String finalLoadDatas="";
    String hostRandomNumber = "";
    String cardResponse = "";
    secLevel = 0;
    blockSize = BLOCK_SIZE;
    Boolean deleteXML=true;
    ArrayList<String> capFileAPDUs=null;
    String tempDir="";
    
    //extract the cap file
    String temp = System.getProperty("user.dir")+OUTPUT_TEMP_DIR;
    tempDir = extractCapFile(capFilepath, temp);

    if(tempDir == "")
        return capFileAPDUs;

    //read all cap files binary data
    finalLoadDatas = getcapFilesBinaryData(tempDir, capList);

    //set header for each block
    blockData = frameBlockData(finalLoadDatas, blockSize);

    numBlock = blockData.length;

    //get the PACKAGE ID INFO
    File f = new File(tempDir + "\\Header.cap");
    if(f.exists())
        PKGID = getPackageID(tempDir + "\\Header.cap");
    else
        return capFileAPDUs;

    //et the Applet ID info
    f = new File(tempDir + "\\Applet.cap");
    if(f.exists())
        AID = getAppletInstanceId(tempDir + "\\Applet.cap");
    else
        return capFileAPDUs;
    
    if(AID != null)
        delAID = new String[AID.length + 1];
    else 
        delAID = new String[PKGID.length];

    if (AID.length > 0){
        //delAID will have the full APDU for deleting an Applet, after calling getDeleteAPDU method.
        for (int i=0; i<AID.length;i++){
            if (i == 0 && instaid != null)
                delAID[i] = getDeleteAPDU(instaid);
            else
                delAID[i] = getDeleteAPDU(AID[i]);
        }

        // Each cap file consists of only one package, thats why PKGID index is always zero.
        // Last delAID element will have the full APDU for deleting the Package.
        delAID[AID.length] = getDeleteAPDU(PKGID[0]);
    }
    else{
        delAID[0] = getDeleteAPDU(PKGID[0]);
    }

    // uploadAPDU will have the APDU for installing an applet.
    uploadAPDU = getUploadAPDU(PKGID[0], numBlock);
    //Sub Upload APDU's will be created
    blockData = blockDataWithHeader(blockData);
    //Install APDU block values
    int i = 0;
    if (AID.length > 0){
        installAPDU = new String[AID.length];
        for(String aId:AID){
            if(i == 0 && instaid != null)
                installAPDU[i] = createInstallAPDU(aId, instaid, appSpecificParam, appPrivileges, PKGID[0]);
            else
                installAPDU[i] = createInstallAPDU(aId, null,null,null, PKGID[0]);
            i++;
        }
    }  
    //load applet APDUs
    capFileAPDUs = LoadAppletAPDUs(secLevel, delAID, uploadAPDU,  blockData, installAPDU);
    //APDUs generated for selected applet load"


    //Delete tempXML folder and its content
    tempDir = System.getProperty("user.dir");
    tempDir += OUTPUT_TEMP_DIR;
    Utilities.deleteDirectory(new File(tempDir));
    
    ID[0] = PKGID[0];
    ID[1] = AID[0];
    if(instaid != null)
        ID[2] = instaid;
    else
        ID[2] = AID[0];
    

    return capFileAPDUs;
}

/**
 * This method parses the passed string and extracts the package ID and applet Ids
 * 
 * @param data - card response which contains card info
 * @return list of Packages structures
 */
public ArrayList<Packages> getPckages(String data){
    int index=0;
    int len=0;
    int cnt=0;
    String sub="";
    String lenStr="";
    ArrayList<Packages> packagesList = null;
    
    if(!data.isEmpty()){
        packagesList = new ArrayList<Packages>();
        do{
            Packages pkg = new Packages();
            index=0;
            lenStr=data.substring(index,2);
            len = Integer.parseInt(lenStr,16);
            len = len*2;
            index+=2;
            pkg.packageId = data.substring(index, index+len);
            index+=len;
            sub = data.substring(index,index+6);
            index+=6;
            cnt = Integer.parseInt( sub.substring(4,4+2),16);
            data = data.substring(index);
            pkg.aid = new String[cnt];
            index=0;
            for(int i=0;i<cnt;i++){
                len = Integer.parseInt( data.substring(index,2),16);
                len = len*2;
                index+=2;
                pkg.aid[i] = data.substring(index,index+len);
                index+=len;
            }
            packagesList.add(pkg);
            data = data.substring(index);            
        }while(!data.equals("9000"));
    }
    
    return packagesList;
}

/*******************************************************************************/
/**
 * This method parses the passed data and extracts the applet Ids
 * 
 * @param data - card response which contains card info
 * @return list of applet IDs
 */
public ArrayList<String> getInstaceIDs(String data){
    int index=0;
    int len=0;
    int cnt=0;
    String sub="";
    String lenStr="";
    ArrayList<String> instaceIdList = null;
    
    try
    {    
    if(!data.isEmpty()){
        instaceIdList = new ArrayList<String>();
        
        if(data.length()<=4)
            return instaceIdList;
        
        do{      
            String instanceID="";
            index=0;
            lenStr=data.substring(index,2);
            len = Integer.parseInt(lenStr,16);
            len = len*2;
            index+=2;
            instanceID = data.substring(index, index+len);
            instaceIdList.add(instanceID);
            index+=len; 
            index+=4; //0700 
            data = data.substring(index);            
        }while(!data.equals("9000"));
    }
    }
    catch(Exception ex)
    {
        
    }
    
    return instaceIdList;
}

}