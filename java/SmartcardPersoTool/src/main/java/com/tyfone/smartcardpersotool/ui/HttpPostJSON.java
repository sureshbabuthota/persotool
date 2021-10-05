/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.ui;

import com.tyfone.smartcardpersotool.utility.GetPropertyValues;
import com.tyfone.smartcardpersotool.model.JacksonParser;
import com.tyfone.smartcardpersotool.model.initperso.BuildPersoObject;
import com.tyfone.smartcardpersotool.model.initperso.Personalisation;
import com.tyfone.smartcardpersotool.model.javacard.Smartcard;
import com.tyfone.smartcardpersotool.model.perso.BuildInitPersoObject;
import com.tyfone.smartcardpersotool.model.perso.CryptoValues;
import com.tyfone.smartcardpersotool.model.persoresponse.PersoResponse;
import com.tyfone.smartcardpersotool.model.tokenstatus.TokenStatus;
import com.tyfone.smartcardpersotool.utility.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.swing.JTextArea;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Kishan
 */
public class HttpPostJSON {

    String[] IDs;
    String tsn;
    String csn = "";
    Smartcard scardObj;
    JTextArea logTextArea;
    String username = "u4ia";
    String password = "u4iaUSERapi";

    public HttpPostJSON(Smartcard scardObj, String TSN, JTextArea logTextAreaObj) {
        this.scardObj = scardObj;
        tsn = TSN;
        csn = readCSN(scardObj);
        logTextArea = logTextAreaObj;
    }

    public void setAppletID(String[] IDs) {
        this.IDs = IDs;
    }

    public String readCSN(Smartcard scardObj) {
        CommandAPDU selectCM;
        CommandAPDU getCPLC;
        String CM_AID;
        try {
            CM_AID = GetPropertyValues.getPropValue("CM_AID");
            if (CM_AID == null) {
                CM_AID = new String("A000000151000000");
            }
            String len = Utilities.toHex((byte) (CM_AID.length() / 2));
            if (len.length() == 1) {
                len = "0" + len;
            }
            String CM_APDU = GetPropertyValues.getPropValue("SELECT_APPLET_APDU") + len + CM_AID;
            byte[] cm_apdu = Utilities.hexStringToBytes(CM_APDU);
            byte[] getcplc = Utilities.hexStringToBytes("80CA9F7F00");
            selectCM = new CommandAPDU(cm_apdu);

            getCPLC = new CommandAPDU(getcplc);

            //Select Card Manager.
            scardObj.transmitAPDU(selectCM);
            //Send 80CA9F7F00 APDU to read the CSN of a card.
            ResponseAPDU response = scardObj.transmitAPDU(getCPLC);

            csn = Utilities.bytesToHexString(response.getData());
            if (csn.length() < 52) {
                System.out.println("Card is not prepersonalized..");
                return null;
            }
        } catch (IOException | CardException ex) {
            ex.printStackTrace();
        }
        return csn.substring(26, 42);
    }

    public String transmitAPDU(String APDU) {
        CommandAPDU selectCM;
        byte[] cm_apdu = Utilities.hexStringToBytes(APDU);
        try {
            selectCM = new CommandAPDU(cm_apdu);
            //Select Card Manager.
            ResponseAPDU response = scardObj.transmitAPDU(selectCM);

            return Utilities.bytesToHexString(response.getData()) + Utilities.toHex((byte) (response.getSW1())) + Utilities.toHex((byte) (response.getSW2()));
        } catch (CardException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private ArrayList parseData(String data) {
        byte[] dataValue = Utilities.hexStringToBytes(data);
        int length = dataValue.length;
        int dataLen = 0;
        int index = 0;

        ArrayList<String> datalist = new ArrayList<String>();

        while (length > 3) {
            if (dataValue[index] == (byte) 0x81) {
                dataLen = dataValue[index + 1];
                String data1 = Utilities.bytesToHexString(dataValue, index + 2, dataLen);
                datalist.add(data1);
                index = index + dataLen + 2;
                length = length - dataLen - 2;
            }
        }
        return datalist;
    }

    public String getStatus() {        
        String line = "";
        StringBuffer[] result = new StringBuffer[2];        
        for (int index = 0; index < 2; index++) {
            result[index] = new StringBuffer();
        }

        try {
            String URL = GetPropertyValues.getPropValue("GET_TOKEN_STATE");

            URL = URL.replaceFirst("token_serial_number", tsn);

            HttpGet httpGet = new HttpGet(URL);
            String auth = new StringBuffer(username).append(":").append(password).toString();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            System.out.println("URL : " + URL);
            httpGet.setHeader("AUTHORIZATION", authHeader);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Stream", "true");

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != 200) {
                LogMessage("Card Personalization Failed \n");
                LogMessage(response.getStatusLine().getReasonPhrase());
                return null;
            }
            String respCode = "";
            respCode = String.valueOf(response.getStatusLine().getStatusCode());
            result[0] = result[0].append(respCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = reader.readLine()) != null) {
                result[1].append(line);
            }
        } catch (IOException exc) {
            System.out.println("Property File is not found");
        }
        //Convert Json string to object.
        TokenStatus tokenStatusObj = (TokenStatus) JacksonParser.getInstance().mapStringToModel(TokenStatus.class, result[1].toString());
        return tokenStatusObj.getStatus().getName();
    }

    public void personalize() {
        String URL = "";
        ArrayList<String> datalist = null;
        //Build Perso JSON Object
        BuildPersoObject buildPersoObj = new BuildPersoObject();

        //Select HW Token Applet
        String len = Utilities.toHex((byte) (IDs[1].length() / 2));
        if (len.length() == 1) {
            len = "0" + len;
        }

        String selectApplet = "00A40400" + len + IDs[1];

        String response = transmitAPDU(selectApplet);
        try {
            //Setup APDU
            response = transmitAPDU(GetPropertyValues.getPropValue("SETUP_APDU"));
            if (!response.endsWith("9000")) {
                System.out.println("Selected Applet not exist");
                return;
            }
            response = transmitAPDU(GetPropertyValues.getPropValue("GET_PUBLICKEY_ADMINPIN"));
            if (!response.endsWith("9000")) {
                System.out.println("Error while retreiving Public key and Admin PIN " + response);
                return;
            }
            datalist = parseData(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Read Admin and Public Key.       
        try {
            URL = GetPropertyValues.getPropValue("INIT_URL");
        } catch (IOException exc) {
            System.out.println("Property File is not found");
        }

        URL = URL.replaceFirst("token_serial_number", tsn);

        Personalisation persoObj = buildPersoObj.buildObject(csn, datalist.get(0), IDs[1], IDs[0], datalist.get(1));
        //persoJson
        String jsonData = JacksonParser.getInstance().mapModelToString(persoObj);

        HttpPost httpPost = createPOSTConnectivity(URL, username, password);

        StringBuffer[] responseJSON = executeHttpRequest(jsonData, httpPost);

        if (!responseJSON[0].toString().equalsIgnoreCase("200")) {
            System.out.println("Failed to personalize the card: " + responseJSON[1].toString());
            return;
        }
        //Convert Json string to object.
        PersoResponse persoResponseObj = (PersoResponse) JacksonParser.getInstance().mapStringToModel(PersoResponse.class, responseJSON[1].toString());

        //Read Challenge bytes from the U4iA Response.
        String u4iaChallenge = persoResponseObj.getChallengeInfo().getAuthData().getChallenge();

        //Read the Transaction ID = 
        String transactionID = persoResponseObj.getChallengeInfo().getAuthData().getTransactionId();
        //Build InitPerso JSON Request.
        BuildInitPersoObject initPersoObj = new BuildInitPersoObject();

        //Signature Data = > Public Key + Admin PIN
        String data = datalist.get(0) + datalist.get(1);

        //Data Signature:: 
        try {
            //Setup APDU
            String GET_ADMIN_SIGN = GetPropertyValues.getPropValue("GET_SIGNED_PUBLIC_ADMIN_C");
            //Select HW Token Applet
            len = Utilities.toHex((byte) (u4iaChallenge.length() / 2));
            if (len.length() == 1) {
                len = "0" + len;
            }
            String APDU = GET_ADMIN_SIGN + len + u4iaChallenge;

            response = transmitAPDU(APDU);
            datalist = parseData(response);
            if (!response.endsWith("9000")) {
                System.out.println("GET Card Signature Failed..");
                return;
            }
            APDU = GetPropertyValues.getPropValue("SET_CARD_STATE");
            response = transmitAPDU(APDU);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Read the Signature of data [publicKey + AdminPIN]
        String dataSignature = datalist.get(0);

        CryptoValues cryptoObject = initPersoObj.buildObject(data, dataSignature);
        jsonData = JacksonParser.getInstance().mapModelToString(cryptoObject);

        try {
            URL = GetPropertyValues.getPropValue("PERSO_URL");
            URL = URL.replaceFirst("token_serial_number", tsn);
            URL = URL.replaceFirst("transaction_id", transactionID);
            httpPost = createPOSTConnectivity(URL, username, password);
            responseJSON = executeHttpRequest(jsonData, httpPost);
            LogMessage("Card Personalization completed Successfully ");
        } catch (IOException excep) {
            System.out.println("Property File is not found");
        }
//        }
        //send Set Card State APDU: 802B0000        
    }

    HttpPost createPOSTConnectivity(String restUrl, String username, String password) {
        HttpPost post = new HttpPost(restUrl);

        String auth = new StringBuffer(username).append(":").append(password).toString();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        System.out.println("URL : " + restUrl);
        post.setHeader("AUTHORIZATION", authHeader);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream", "true");
        return post;
    }

    StringBuffer[] executeHttpRequest(String jsonData, HttpPost httpPost) {
        HttpResponse response = null;
        String line = "";
        StringBuffer[] result = new StringBuffer[2];
        for (int index = 0; index < 2; index++) {
            result[index] = new StringBuffer();
        }
        try {
            if (jsonData != null) {
                httpPost.setEntity(new StringEntity(jsonData));
                System.out.println("Post parameters : " + jsonData);
            }

            HttpClient client = HttpClientBuilder.create().build();
            response = client.execute(httpPost);

            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() != 200) {
                LogMessage("Card Personalization Failed \n");
                LogMessage(response.getStatusLine().getReasonPhrase());
            }
            String respCode = "";
            respCode = String.valueOf(response.getStatusLine().getStatusCode());
            result[0] = result[0].append(respCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = reader.readLine()) != null) {
                result[1].append(line);
            }
        } catch (UnsupportedEncodingException ex) {
            System.out.println("error while encoding api url : " + ex);
        } catch (IOException ex) {
            System.out.println("ioException occured while sending http request : " + ex);
        } finally {
            httpPost.releaseConnection();
        }
        System.out.println("JSON Response from the Server:");
        System.out.println(result[1].toString());

        return result;
        //return response.getStatusLine().getStatusCode();
    }

    public void LogMessage(final String text) {
        logTextArea.append(text);
        int cnt = 0;
        cnt = logTextArea.getText().length();
        logTextArea.append("\r\n");
        logTextArea.select(cnt, cnt);
        logTextArea.update(logTextArea.getGraphics());
    }
}
