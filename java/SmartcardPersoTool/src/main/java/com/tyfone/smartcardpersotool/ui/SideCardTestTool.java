/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.ui;

import com.tyfone.smartcardpersotool.utility.GetPropertyValues;
import com.tyfone.smartcardpersotool.utility.Utilities;
import com.tyfone.smartcardpersotool.model.javacard.TAUPPersonalization;
import com.tyfone.smartcardpersotool.model.javacard.AppletUtility;
import com.tyfone.smartcardpersotool.model.javacard.Smartcard;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Sureshbabu
 */
public class SideCardTestTool extends javax.swing.JFrame {

    Smartcard scardObj = null;
    private List<CardTerminal> readers = null;
    private int selectedReaderIndex = 0;
    private Card scard = null;
    private CardChannel channel = null;
    private String appletFilePath = "";
    private String packageId = "";
    private String cardTransportKey = "";
    private boolean isConnected = false;
    private boolean isReaderConnected = false;
    public String appVersion = "1.0.0";
    public static final String NO_READERS_FOUND = "No PC/SC Readers Found";
    public static final String CONNECT_SIDECARD = "Please connect SideCard and try again.";
    public static final String ENTER_VALID_APDU = "Please enter valid APDU command.";
    public static final String SELECT_APPLET_FILE = "Please select applet file.";
    public static final String SELECT_APPLET_FILE_OR_AID = "Please enter applet instance Id or package Id";
    public static final String APPLET_LOAD_COMPLETED = "Applet loading process completed.";
    public static final String APPLET_AID_LEN_ERR = "Invalid Instance ID. Applet Instance ID length should be in between 5 to 16 bytes";
    public static final String APPLET_LOAD_FAIL = "Applet loading failed.";
    public static final String GET_AID_COMPLETED = "Get AID process completed.";
    public static final String SELECT_APDUS_FILE = "Please select APDUs file.";
    public static final String SELECT_READER = "Please select smart card reader.";
    public static final String SMARTCARD_CONNECTED = "Smart card connected successfully.";
    public static final String SMARTCARD_DISCONNECTED = "Smart card disconnected ";
    public static final String ENTER_AID = "Please enter applet AID and try again.";
    public static final String APPLET_DELETE_SUCCESS = "Selected applet deleted successfully.";
    public static final String APPLET_DELETE_FAIL = "Selected applet delete failed.";
    public static final String APPLET_DELETE_COMPLETED = "Applet delete process completed.";
    public static final String CARD_RESETTING = "Smart card connection resetting....";
    public static final String CARD_RESET_SUCCESS = "Smart card connection reset successful";

    public static final String SELECT_PREPERSO_FILE = "Please select pre-perso script file.";
    public static final String ENTER_TSN_VALUE = "Please enter SideCard's TSN value.";
    public static final String SELECT_VALID_PREPERSO_FILE = "Invalid pre-perso script. Please select valid pre-perso script file.";
    public static final String SELECT_VALID_APPLET_FILE = "Invalid applet file. Please select valid applet file.";

    public static final String ENTER_VALID_TSN_VALUE = "Enter a valid TSN";

    private void EnabbleTransmitControls(boolean selected) {

        FW_jComboBox.setEnabled(selected);

        CLA_jTextField.setEnabled(!selected);
        INS_jTextField.setEnabled(!selected);
        P1_jTextField.setEnabled(!selected);
        P2_jTextField.setEnabled(!selected);
        LC_jTextField.setEnabled(!selected);
        DATA_jTextField.setEnabled(!selected);
        LE_jTextField.setEnabled(!selected);
    }

    public enum GPCommands {
        GP_GET_AIDS, GP_LOAD_APPLET, GP_DELETE_APPLET
    };

    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    public static String getJarContainingFolder(Class aclass) throws Exception {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

        File jarFile;
        if (codeSource.getLocation() != null) {
            jarFile = new File(codeSource.getLocation().toURI());
        } else {
            String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
            String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
            jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
            jarFile = new File(jarFilePath);
        }
        return jarFile.getParentFile().getAbsolutePath();
    }

    private void setIcon() {
        try {
            URL url = getClass().getClassLoader().getResource("SmartcardPersoTool.png");
            BufferedImage image = ImageIO.read(url);
            //   Image img = Toolkit.getDefaultToolkit().getImage("bird2.gif");
            setIconImage(image);
        } catch (IOException e) {
            System.out.println("SetIcon:: Exception.."+e.getMessage());
        }
    }

    private static OSType getOperatingSystemType() {
        OSType detectedOS = null;
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }

    public String SendAPDU(String readLine) {
        String respStr = "";
        //Log_jTextArea.append("APDU \t:=> "+readLine+"\r\n");
        //Log_jTextArea.update(Log_jTextArea.getGraphics());
        LogMessage("APDU \t:=> " + readLine);
        byte[] apduBytes = Utilities.hexStringToBytes(readLine);
        if (apduBytes != null) {
            byte[] resp = scardObj.transmitAPDU(apduBytes);
            if (resp != null) {
                respStr = Utilities.bytesToHexString(resp);
                LogMessage("Response \t:=> " + respStr);
            }
        } else {
            LogMessage("Invalid APDU command.\r\n");
        }

        //Log_jTextArea.update(Log_jTextArea.getGraphics());
        //Log_jTextArea.setCaretPosition(Log_jTextArea.getDocument().getLength());
        //Log_jTextArea.validate();
        return respStr;
    }

    public Boolean AuthenticateCM(String cmID, String des3_cbc_key) {
        Boolean isAuthenticated = false;

        String authAPDU = "";
        String apdu = "";
        String respStr = "";
        String tempDir = "";

        int securityLevel = 0;
        String randomnumber = "";
        String cardresponses = "";

        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:hh:mm:ss");
        String today = formatter.format(date);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(today.getBytes());
            randomnumber = Utilities.bytesToHexString(thedigest);
        } catch (Exception ex) {
            randomnumber = "";
        }
        //get first 8 bytes
        randomnumber = randomnumber.substring(16); //8 bytes

        //select cardmanager
        AppletUtility appUtil = new AppletUtility();
        int idLen = cmID.length() / 2;
        byte blen = (byte) idLen;
        String selectapdu = appUtil.SELECT_JCOP_MANAGER_HEADER + Utilities.toHex(blen) + cmID;
        respStr = SendAPDU(selectapdu);//appUtil.SELECT_JCOP_MANAGER);

        if (respStr.endsWith("9000")) {
            apdu = "8050000008" + randomnumber;
            cardresponses = SendAPDU(apdu);

            LogMessage("Authenticating with card manager...\r\n");
            //authenticate with card manager

            authAPDU = appUtil.getCardAuthenticateAPDU(securityLevel, randomnumber, cardresponses, des3_cbc_key);

            if (authAPDU != null) {
                respStr = SendAPDU(authAPDU);

                if (respStr.endsWith("9000")) {
                    isAuthenticated = true;
                    LogMessage("Card manager authenticated successfully.\r\n");
                } else {
                    LogMessage("Card manager authentication failed. Reason: check error code.\r\n");
                }
            } else {
                LogMessage("Card manager authentication failed. Reason: card check transport key.\r\n");
            }
        }
        return isAuthenticated;
    }

    public void GetCardInfo(String des3_cbc_key) {
        String apdu = "";
        String instrespStr = "";
        String respStr = "";
        ArrayList<String> instances = null;

        String cm = "A000000151000000";

        if (AuthenticateCM(cm, des3_cbc_key)) {
            //Log_jTextArea.append("\r\nCapturing card info..." +"\r\n\r\n");
            //Log_jTextArea.update(Log_jTextArea.getGraphics());
            LogMessage("Capturing card info...");
            AppletUtility appUtil = new AppletUtility();

            apdu = appUtil.GET_INSTANCEs_ID_APDU;
            instrespStr = SendAPDU(apdu);

            apdu = appUtil.GET_CARD_INFO_APDU;
            respStr = SendAPDU(apdu);

            appUtil.pakegesList = appUtil.getPckages(respStr);
            LogMessage("\r\nPackage IDs:...");

            if (appUtil.pakegesList.size() > 0) {
                for (int i = 0; i < appUtil.pakegesList.size(); i++) {
                    LogMessage("\t" + appUtil.pakegesList.get(i).packageId);
                    //for(int j=0;j<appUtil.pakegesList.get(i).aid.length;j++){
                    //    LogMessage("     Applet ID: "+ appUtil.pakegesList.get(i).aid[j]+"\t....."+Utilities.hexToAscii(appUtil.pakegesList.get(i).aid[j])+"\r\n"); 
                    // }
                }
            }

            instances = appUtil.getInstaceIDs(instrespStr);
            LogMessage("Instance IDs:...");

            if (instances.size() > 0) {
                for (int i = 0; i < instances.size(); i++) {
                    LogMessage("\t" + instances.get(i));
                }
            }
        }
    }

    public Boolean LoadApplet(String filepath, String aid, String des3_cbc_key, String[] ID) {
        ArrayList<String> apduslist = null;
        Boolean isLoaded = false;
        String apdu = "";
        String respStr = "";

        String cm = "A000000151000000";

        if (AuthenticateCM(cm, des3_cbc_key)) {
            AppletUtility appUtil = new AppletUtility();

            apduslist = appUtil.getLoadAppletAPDUs(filepath, aid, null, null, ID);

            if (apduslist != null) {
                try {
                    for (String apduStr : apduslist) {
                        respStr = "";
                        respStr = SendAPDU(apduStr);
                    }
                } catch (Exception ex) {
                }
                if (respStr.endsWith("9000")) {
                    isLoaded = true;
                } else {
                    isLoaded = false;
                }
            } else {
                isLoaded = false;
            }
        }
        return isLoaded;
    }

    public Boolean DeleteApplet(String pkgId, String des3_cbc_key) {
        ArrayList<String> apduslist = null;
        Boolean isDeleted = false;
        String apdu = "";
        String respStr = "";

        String cm = "A000000151000000";

        if (AuthenticateCM(cm, des3_cbc_key)) {
            AppletUtility appUtil = new AppletUtility();

            apduslist = appUtil.getDeleteAppletAPDUs(null, pkgId);

            if (apduslist != null) {
                for (String apduStr : apduslist) {
                    respStr = "";
                    respStr = SendAPDU(apduStr);
                    if (respStr.endsWith("9000")) {
                        isDeleted = true;
                    } else if (respStr.endsWith("6a88") || respStr.endsWith("6A88")) {
                        respStr = "";
                        apduStr = apduStr.replaceFirst(appUtil.DEL_APDU_HEADER, appUtil.DEL_HEADER);
                        respStr = SendAPDU(apduStr);
                        if (respStr.endsWith("9000")) {
                            isDeleted = true;
                        } else {
                            isDeleted = false;
                            LogMessage("Applet/package not found ...\r\n");
                        }
                        //Log_jTextArea.update(Log_jTextArea.getGraphics());
                    } else {
                        isDeleted = false;
                    }
                }
            } else {
                isDeleted = false;
            }
        }

        return isDeleted;
    }

    public  void LogMessage(final String text) {
        //EventQueue.invokeLater(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        Log_jTextArea.append(text);
        // Scroll down as we generate text.
        int cnt = 0;
        cnt = Log_jTextArea.getText().length();
        //Log_jTextArea.setCaretPosition(cnt);

        Log_jTextArea.append("\r\n");
        Log_jTextArea.select(cnt, cnt);
        Log_jTextArea.update(Log_jTextArea.getGraphics());
        //        } catch (Exception ex) {
        //        }                
        //    }
        //});
    }

    /*
    public void UpdateLog(String message)
    { 
        Log_jTextArea.append(message);
        Log_jTextArea.setCaretPosition(Log_jTextArea.getDocument().getLength()-1);
        Log_jTextArea.validate();
    }
    public void LogMessage(String msg)
    {
        new Thread(() -> UpdateLog(msg)).start();
    }
     */
    /**
     * Creates new form SideCardTestTool
     */
    public SideCardTestTool() {
        initComponents();
        //TODO: 
        jCheckBox1.setVisible(false);
        setIcon();
        appVersion = getAppVersion();
        setTitle("Side-X Test Tool V." + appVersion);
        scardObj = new Smartcard();
        loadReaders();
        setTextFieldsLimit();

        this.getContentPane().setBackground(Color.white);
    }

    private void setTextFieldsLimit() {
        CLA_jTextField.setDocument(new JTextFieldLimit(2));
        INS_jTextField.setDocument(new JTextFieldLimit(2));
        P1_jTextField.setDocument(new JTextFieldLimit(2));
        P2_jTextField.setDocument(new JTextFieldLimit(2));
        LC_jTextField.setDocument(new JTextFieldLimit(6));
        LE_jTextField.setDocument(new JTextFieldLimit(4));
        DATA_jTextField.setDocument(new JTextFieldLimit(2048));
        AID_jTextField.setDocument(new JTextFieldLimit(32));
        TSN_jTextField.setDocument(new JTextFieldLimit(32));
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadReaders() {
        readers = scardObj.getSmartcardReaders();
        Reader_jComboBox.removeAllItems();
        if (readers != null) {
            for (int i = 0; i < readers.size(); i++) {
                Reader_jComboBox.addItem(readers.get(i).getName());
            }
        }
    }

    private void validateAndMoveNextField(java.awt.event.KeyEvent evt, javax.swing.JTextField currentfld, javax.swing.JTextField nextfld) {
        Boolean isValidChar = false;
        String fldText = currentfld.getText();
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && ((c < 'a') || (c > 'f')) && ((c < 'A') || (c > 'F')) && (c != KeyEvent.VK_BACK_SPACE)) {
            evt.consume();  // ignore event
        } else {
            isValidChar = true;
        }
        if (isValidChar && (Character.isLetterOrDigit(c))) {
            fldText = fldText + c;
        }
        if (fldText.length() == 2 && nextfld != null) {
            nextfld.requestFocus();
        }
    }

    private void validateLCField(java.awt.event.KeyEvent evt, javax.swing.JTextField currentfld, javax.swing.JTextField nextfld) {
        Boolean isValidChar = false;
        String fldText = currentfld.getText();
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && ((c < 'a') || (c > 'f')) && ((c < 'A') || (c > 'F')) && (c != KeyEvent.VK_BACK_SPACE)) {
            evt.consume();  // ignore event
        } else {
            isValidChar = true;
        }
        if (isValidChar && (Character.isLetterOrDigit(c))) {
            fldText = fldText + c;
        }
        if (c != '\b' && (fldText.length() == 2 && nextfld != null)) {
            nextfld.requestFocus();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        Reader_jComboBox = new javax.swing.JComboBox<>();
        Connect_jButton = new javax.swing.JButton();
        Disconnect_jButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ATR_jLabel = new javax.swing.JLabel();
        MultiPane_jTabbedPane = new javax.swing.JTabbedPane();
        AppletLoad_jPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        AppletPath_jTextField = new javax.swing.JTextField();
        AppletLoad_jButton = new javax.swing.JButton();
        Browse_jButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        AID_jTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        AppletLoad_jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        DeleteInstanceId_jTextField = new javax.swing.JTextField();
        DeleteApplet_jButton = new javax.swing.JButton();
        ReadCardInfo_jButton = new javax.swing.JButton();
        APDU_jPanel = new javax.swing.JPanel();
        CLA = new javax.swing.JLabel();
        CLA1 = new javax.swing.JLabel();
        P1_jLabel = new javax.swing.JLabel();
        CLA3 = new javax.swing.JLabel();
        CLA4 = new javax.swing.JLabel();
        CLA5 = new javax.swing.JLabel();
        CLA6 = new javax.swing.JLabel();
        CLA_jTextField = new javax.swing.JTextField();
        INS_jTextField = new javax.swing.JTextField();
        P1_jTextField = new javax.swing.JTextField();
        P2_jTextField = new javax.swing.JTextField();
        LC_jTextField = new javax.swing.JTextField();
        DATA_jTextField = new javax.swing.JTextField();
        LE_jTextField = new javax.swing.JTextField();
        Transmit_jButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        APDU_jRadioButton = new javax.swing.JRadioButton();
        FW_jRadioButton = new javax.swing.JRadioButton();
        FW_jComboBox = new javax.swing.JComboBox<>();
        MultipleAPDUS_jPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        MultipleAPDUs_jTextField = new javax.swing.JTextField();
        APDUsFileBrowse_jButton = new javax.swing.JButton();
        MultipleTransmit_jButton = new javax.swing.JButton();
        HWTokenPerso_jPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        AppletloadPath_jTextField = new javax.swing.JTextField();
        Personalize_jButton = new javax.swing.JButton();
        appletbrowse_jButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        PrepersoPath_jTextField = new javax.swing.JTextField();
        prepersobrowse_jButton = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        TSN_jTextField = new javax.swing.JTextField();
        Log_jScrollPane = new javax.swing.JScrollPane();
        Log_jTextArea = new javax.swing.JTextArea();
        ExitjButton = new javax.swing.JButton();
        Reload_jButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        ClearLog_jButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Des3_CBC_Key_jTextField = new javax.swing.JTextField();
        Reset_jButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Side-X Test Tool V1.0");
        setBackground(new java.awt.Color(255, 255, 255));
        setName("TestTool_frame"); // NOI18N
        setResizable(false);

        jLabel1.setText("Smart card Reader:");

        Connect_jButton.setText("Connect");
        Connect_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Connect_jButtonActionPerformed(evt);
            }
        });

        Disconnect_jButton.setText("Disconnect");
        Disconnect_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Disconnect_jButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("ATR Value:");

        MultiPane_jTabbedPane.setBackground(java.awt.Color.white);

        AppletLoad_jPanel.setBackground(java.awt.SystemColor.window);
        AppletLoad_jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Select Applet File:");

        AppletPath_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));

        AppletLoad_jButton.setText("Load");
        AppletLoad_jButton.setPreferredSize(new java.awt.Dimension(63, 23));
        AppletLoad_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AppletLoad_jButtonActionPerformed(evt);
            }
        });

        Browse_jButton.setText("...");
        Browse_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Browse_jButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Enter Applet Instance ID:");

        AID_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));

        jLabel9.setText("(Optional)");

        javax.swing.GroupLayout AppletLoad_jPanelLayout = new javax.swing.GroupLayout(AppletLoad_jPanel);
        AppletLoad_jPanel.setLayout(AppletLoad_jPanelLayout);
        AppletLoad_jPanelLayout.setHorizontalGroup(
            AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppletLoad_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AppletPath_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(AID_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addComponent(Browse_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AppletLoad_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AppletLoad_jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(283, 283, 283))
        );
        AppletLoad_jPanelLayout.setVerticalGroup(
            AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppletLoad_jPanelLayout.createSequentialGroup()
                .addGroup(AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AppletLoad_jPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addComponent(AppletPath_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Browse_jButton)
                            .addComponent(AppletLoad_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(AppletLoad_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(AID_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(AppletLoad_jPanelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel9)))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        AppletLoad_jPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {AID_jTextField, AppletLoad_jButton, AppletPath_jTextField, Browse_jButton});

        MultiPane_jTabbedPane.addTab("Load Applet", AppletLoad_jPanel);

        AppletLoad_jPanel1.setBackground(java.awt.SystemColor.window);
        AppletLoad_jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setText("Enter Instance / Package ID:");

        DeleteInstanceId_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));

        DeleteApplet_jButton.setText("Delete");
        DeleteApplet_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteApplet_jButtonActionPerformed(evt);
            }
        });

        ReadCardInfo_jButton.setText("Read Card Info");
        ReadCardInfo_jButton.setActionCommand("ReadCardInfo_jButton");
        ReadCardInfo_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadCardInfo_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AppletLoad_jPanel1Layout = new javax.swing.GroupLayout(AppletLoad_jPanel1);
        AppletLoad_jPanel1.setLayout(AppletLoad_jPanel1Layout);
        AppletLoad_jPanel1Layout.setHorizontalGroup(
            AppletLoad_jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AppletLoad_jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(19, 19, 19)
                .addComponent(DeleteInstanceId_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AppletLoad_jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ReadCardInfo_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(DeleteApplet_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addContainerGap())
        );
        AppletLoad_jPanel1Layout.setVerticalGroup(
            AppletLoad_jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppletLoad_jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AppletLoad_jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addGroup(AppletLoad_jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(DeleteInstanceId_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(DeleteApplet_jButton))
                .addGap(18, 18, 18)
                .addComponent(ReadCardInfo_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(83, 83, 83))
        );

        MultiPane_jTabbedPane.addTab("Delete Applet", AppletLoad_jPanel1);

        APDU_jPanel.setBackground(java.awt.SystemColor.window);
        APDU_jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        CLA.setText("CLA");

        CLA1.setText("INS");

        P1_jLabel.setText("P1");

        CLA3.setText("P2");

        CLA4.setText("LC");

        CLA5.setText("DATA");

        CLA6.setText("LE");

        CLA_jTextField.setEnabled(false);
        CLA_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CLA_jTextFieldKeyTyped(evt);
            }
        });

        INS_jTextField.setEnabled(false);
        INS_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                INS_jTextFieldKeyTyped(evt);
            }
        });

        P1_jTextField.setEnabled(false);
        P1_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                P1_jTextField2KeyTyped(evt);
            }
        });

        P2_jTextField.setEnabled(false);
        P2_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                P2_jTextFieldKeyTyped(evt);
            }
        });

        LC_jTextField.setEnabled(false);
        LC_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                LC_jTextFieldKeyTyped(evt);
            }
        });

        DATA_jTextField.setEnabled(false);
        DATA_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DATA_jTextFieldKeyTyped(evt);
            }
        });

        LE_jTextField.setEnabled(false);
        LE_jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                LE_jTextFieldKeyTyped(evt);
            }
        });

        Transmit_jButton.setText("Transmit");
        Transmit_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Transmit_jButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("APDU:");

        APDU_jRadioButton.setText("Enter APDU");
        APDU_jRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                APDU_jRadioButtonActionPerformed(evt);
            }
        });

        FW_jRadioButton.setSelected(true);
        FW_jRadioButton.setText("Select Single APDU");
        FW_jRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FW_jRadioButtonActionPerformed(evt);
            }
        });

        FW_jComboBox.setEditable(true);
        FW_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00A4040009A00000057420000101", "80100000", "8010000002", "80100000021122", "8010000002112203", "801000000F000102030405060708090001020304" }));
        FW_jComboBox.setMaximumSize(new java.awt.Dimension(685, 20));
        FW_jComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FW_jComboBoxKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout APDU_jPanelLayout = new javax.swing.GroupLayout(APDU_jPanel);
        APDU_jPanel.setLayout(APDU_jPanelLayout);
        APDU_jPanelLayout.setHorizontalGroup(
            APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(APDU_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(APDU_jPanelLayout.createSequentialGroup()
                        .addComponent(APDU_jRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(FW_jRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, APDU_jPanelLayout.createSequentialGroup()
                        .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(APDU_jPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FW_jComboBox, 0, 1, Short.MAX_VALUE))
                            .addGroup(APDU_jPanelLayout.createSequentialGroup()
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(CLA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CLA_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CLA1)
                                    .addComponent(INS_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(P1_jLabel)
                                    .addComponent(P1_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(CLA3)
                                    .addComponent(P2_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(CLA4)
                                    .addComponent(LC_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(CLA5)
                                    .addComponent(DATA_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(APDU_jPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(CLA6)
                                        .addGap(18, 18, 18))
                                    .addGroup(APDU_jPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(LE_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(10, 10, 10)
                        .addComponent(Transmit_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        APDU_jPanelLayout.setVerticalGroup(
            APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(APDU_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(APDU_jRadioButton)
                    .addComponent(FW_jRadioButton))
                .addGap(5, 5, 5)
                .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(APDU_jPanelLayout.createSequentialGroup()
                        .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CLA)
                            .addComponent(CLA5)
                            .addComponent(CLA6)
                            .addComponent(CLA1)
                            .addComponent(P1_jLabel)
                            .addComponent(CLA3)
                            .addComponent(CLA4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CLA_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(INS_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(P1_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(P2_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LC_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DATA_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LE_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(APDU_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(FW_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addComponent(Transmit_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        MultiPane_jTabbedPane.addTab("Transmit", APDU_jPanel);

        MultipleAPDUS_jPanel.setBackground(java.awt.SystemColor.window);
        MultipleAPDUS_jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Select APDUs file:");

        APDUsFileBrowse_jButton.setText("...");
        APDUsFileBrowse_jButton.setMaximumSize(new java.awt.Dimension(73, 23));
        APDUsFileBrowse_jButton.setMinimumSize(new java.awt.Dimension(73, 23));
        APDUsFileBrowse_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                APDUsFileBrowse_jButtonActionPerformed(evt);
            }
        });

        MultipleTransmit_jButton.setText("Transmit");
        MultipleTransmit_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MultipleTransmit_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MultipleAPDUS_jPanelLayout = new javax.swing.GroupLayout(MultipleAPDUS_jPanel);
        MultipleAPDUS_jPanel.setLayout(MultipleAPDUS_jPanelLayout);
        MultipleAPDUS_jPanelLayout.setHorizontalGroup(
            MultipleAPDUS_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MultipleAPDUS_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MultipleAPDUs_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(APDUsFileBrowse_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MultipleTransmit_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MultipleAPDUS_jPanelLayout.setVerticalGroup(
            MultipleAPDUS_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MultipleAPDUS_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MultipleAPDUS_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(MultipleAPDUs_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(APDUsFileBrowse_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MultipleTransmit_jButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MultiPane_jTabbedPane.addTab("Multiple APDUs", MultipleAPDUS_jPanel);

        HWTokenPerso_jPanel.setBackground(java.awt.SystemColor.window);
        HWTokenPerso_jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setText("Select Applet File:");

        AppletloadPath_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));

        Personalize_jButton.setText("Personalize");
        Personalize_jButton.setPreferredSize(new java.awt.Dimension(63, 23));
        Personalize_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Personalize_jButtonActionPerformed(evt);
            }
        });

        appletbrowse_jButton.setText("...");
        appletbrowse_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appletbrowse_jButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Select Pre-Perso script:");

        PrepersoPath_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));
        PrepersoPath_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrepersoPath_jTextFieldActionPerformed(evt);
            }
        });

        prepersobrowse_jButton.setText("...");
        prepersobrowse_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prepersobrowse_jButtonActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Get from U4ia");
        jCheckBox1.setActionCommand("Download \\nfrom u4ia");
        jCheckBox1.setEnabled(false);
        jCheckBox1.setName(""); // NOI18N

        jLabel14.setText("Enter SideCard TSN Value:");

        TSN_jTextField.setText("1708A00009");
        TSN_jTextField.setPreferredSize(new java.awt.Dimension(343, 20));
        TSN_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TSN_jTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HWTokenPerso_jPanelLayout = new javax.swing.GroupLayout(HWTokenPerso_jPanel);
        HWTokenPerso_jPanel.setLayout(HWTokenPerso_jPanelLayout);
        HWTokenPerso_jPanelLayout.setHorizontalGroup(
            HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HWTokenPerso_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HWTokenPerso_jPanelLayout.createSequentialGroup()
                        .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PrepersoPath_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                            .addComponent(AppletloadPath_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(appletbrowse_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(prepersobrowse_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(HWTokenPerso_jPanelLayout.createSequentialGroup()
                        .addComponent(TSN_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Personalize_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)))
                .addContainerGap())
        );
        HWTokenPerso_jPanelLayout.setVerticalGroup(
            HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HWTokenPerso_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HWTokenPerso_jPanelLayout.createSequentialGroup()
                        .addComponent(prepersobrowse_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appletbrowse_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HWTokenPerso_jPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PrepersoPath_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AppletloadPath_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HWTokenPerso_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(TSN_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Personalize_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51))
        );

        MultiPane_jTabbedPane.addTab("HW Token Perso", HWTokenPerso_jPanel);

        Log_jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Log_jTextArea.setEditable(false);
        Log_jTextArea.setColumns(20);
        Log_jTextArea.setLineWrap(true);
        Log_jTextArea.setRows(5);
        Log_jTextArea.setWrapStyleWord(true);
        Log_jScrollPane.setViewportView(Log_jTextArea);

        ExitjButton.setText("Exit");
        ExitjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitjButtonActionPerformed(evt);
            }
        });

        Reload_jButton.setText("Reload");
        Reload_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Reload_jButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Log:");

        ClearLog_jButton.setText("Clear Log");
        ClearLog_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearLog_jButtonActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/footersmall.png"))); // NOI18N

        jLabel13.setText("Card Transport Key");

        Des3_CBC_Key_jTextField.setText("404142434445464748494a4b4c4d4e4f4041424344454647");
        Des3_CBC_Key_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Des3_CBC_Key_jTextFieldActionPerformed(evt);
            }
        });

        Reset_jButton.setText("Reset");
        Reset_jButton.setEnabled(false);
        Reset_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Reset_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(ClearLog_jButton)
                .addGap(28, 28, 28)
                .addComponent(ExitjButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(Des3_CBC_Key_jTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ATR_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Reader_jComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Reload_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Reset_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Connect_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Disconnect_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Log_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MultiPane_jTabbedPane)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ClearLog_jButton, Connect_jButton, Disconnect_jButton, ExitjButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Reload_jButton, Reset_jButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(Reader_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Connect_jButton)
                    .addComponent(Reload_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Disconnect_jButton)
                    .addComponent(jLabel2)
                    .addComponent(ATR_jLabel)
                    .addComponent(Reset_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(Des3_CBC_Key_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(MultiPane_jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Log_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(ClearLog_jButton)
                    .addComponent(ExitjButton)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Connect_jButton, Disconnect_jButton, ExitjButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Connect_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Connect_jButtonActionPerformed
        cardConnect();
    }//GEN-LAST:event_Connect_jButtonActionPerformed

    private void cardConnect() {
        try {
            isConnected = false;
            isReaderConnected = false;
            if (readers != null) {
                isReaderConnected = true;
                selectedReaderIndex = Reader_jComboBox.getSelectedIndex();
                scard = scardObj.connect(readers.get(selectedReaderIndex));
                if (scard != null) {
                    byte[] atr = scardObj.getATR();

                    if (atr != null) {
                        ATR_jLabel.setText(Utilities.bytesToHexString(atr));
                        Disconnect_jButton.setEnabled(true);
                        Connect_jButton.setEnabled(false);
                        isConnected = true;
                        Reset_jButton.setEnabled(true);

                        DefaultCaret caret = (DefaultCaret) Log_jTextArea.getCaret();
                        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                        LogMessage(SMARTCARD_CONNECTED + "\r\n");
                    }
                }
            } else {
                infoBox(SELECT_READER, this.getTitle());
            }
        } catch (CardException e) {
            ATR_jLabel.setText(e.getMessage());
        }
    }


    private void Disconnect_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Disconnect_jButtonActionPerformed
        cardDisconnect();
    }//GEN-LAST:event_Disconnect_jButtonActionPerformed

    private void cardDisconnect() {
        if (isConnected == true) {
            scardObj.disconnect();
            ATR_jLabel.setText("");
            isConnected = false;
            Disconnect_jButton.setEnabled(false);
            Connect_jButton.setEnabled(true);
            Reset_jButton.setEnabled(false);
            loadReaders();
            LogMessage(SMARTCARD_DISCONNECTED);
        }
    }

    private void ExitjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitjButtonActionPerformed
        // TODO add your handling code here:
        if (scardObj != null) {
            if (isConnected) {
                scardObj.disconnect();
                isConnected = false;
            }
            scardObj = null;
        }
        System.exit(0);
    }//GEN-LAST:event_ExitjButtonActionPerformed

    private void Reload_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reload_jButtonActionPerformed
        // TODO add your handling code here:
        loadReaders();
    }//GEN-LAST:event_Reload_jButtonActionPerformed

    private void ClearLog_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearLog_jButtonActionPerformed
        // TODO add your handling code here:
        Log_jTextArea.setText("");
    }//GEN-LAST:event_ClearLog_jButtonActionPerformed

    private void Reset_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reset_jButtonActionPerformed
        // TODO add your handling code here:
        try {
            if (scardObj != null) {
                if (isConnected) {
                    Reset_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    LogMessage(CARD_RESETTING + "\r\n");

                    selectedReaderIndex = Reader_jComboBox.getSelectedIndex();
                    scardObj.disconnect();
                    TimeUnit.SECONDS.sleep(2);
                    scard = scardObj.connect(readers.get(selectedReaderIndex));
                    if (scard != null) {
                        LogMessage(CARD_RESET_SUCCESS);
                        Log_jTextArea.update(Log_jTextArea.getGraphics());
                        byte[] atr = scardObj.getATR();

                        if (atr != null) {
                            ATR_jLabel.setText(Utilities.bytesToHexString(atr));
                            Disconnect_jButton.setEnabled(true);
                            Connect_jButton.setEnabled(false);
                            isConnected = true;
                            Reset_jButton.setEnabled(true);
                            ReadCardInfo_jButton.setEnabled(true);
                            LogMessage(SMARTCARD_CONNECTED);
                        }
                    }
                    Reset_jButton.setCursor(Cursor.getDefaultCursor());
                }
            }
        } catch (Exception ex) {//
        }
    }//GEN-LAST:event_Reset_jButtonActionPerformed

    private void MultipleTransmit_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MultipleTransmit_jButtonActionPerformed
        // TODO add your handling code here:
        int cnt = 0;
        String fileName = MultipleAPDUs_jTextField.getText();
        if (fileName.equals("")) {
            infoBox(SELECT_APDUS_FILE, this.getTitle());
        } else if (isConnected == false) {
            infoBox(CONNECT_SIDECARD, this.getTitle());
        } else {
            MultipleTransmit_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                BufferedReader b = new BufferedReader(new FileReader(fileName));

                String readLine = "";

                if (b != null) {
                    LogMessage("APDU commands file: " + fileName);

                    while ((readLine = b.readLine()) != null) {
                        if (readLine.startsWith("#") || readLine.startsWith("//")) {
                            continue;
                        } else {
                            LogMessage("APDU \t:=> " + readLine);
                            byte[] apduBytes = Utilities.hexStringToBytes(readLine);
                            if (apduBytes != null) {
                                byte[] resp = scardObj.transmitAPDU(apduBytes);
                                if (resp != null) {
                                    LogMessage("Response \t:=> " + Utilities.bytesToHexString(resp));
                                }
                            } else {
                                LogMessage("Invalid APDU command.");
                            }
                            cnt++;
                        }
                    }
                    b.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            LogMessage("\r\nTotal APDU commands transmitted: " + cnt);

            MultipleTransmit_jButton.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_MultipleTransmit_jButtonActionPerformed

    private void APDUsFileBrowse_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_APDUsFileBrowse_jButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            File selectedFile = fileChooser.getSelectedFile();
            MultipleAPDUs_jTextField.setText(selectedFile.getPath());
        }
    }//GEN-LAST:event_APDUsFileBrowse_jButtonActionPerformed

    private void FW_jComboBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FW_jComboBoxKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && ((c < 'a') || (c > 'f')) && ((c < 'A') || (c > 'F')) && (c != KeyEvent.VK_BACK_SPACE)) {
            evt.consume();  // ignore event
        }
    }//GEN-LAST:event_FW_jComboBoxKeyTyped

    private void FW_jRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FW_jRadioButtonActionPerformed
        // TODO add your handling code here:
        FW_jRadioButton.setSelected(true);
        APDU_jRadioButton.setSelected(false);
        EnabbleTransmitControls(true);
    }//GEN-LAST:event_FW_jRadioButtonActionPerformed

    private void APDU_jRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_APDU_jRadioButtonActionPerformed
        // TODO add your handling code here:
        FW_jRadioButton.setSelected(false);
        APDU_jRadioButton.setSelected(true);
        EnabbleTransmitControls(false);
    }//GEN-LAST:event_APDU_jRadioButtonActionPerformed

    private void Transmit_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Transmit_jButtonActionPerformed
        // TODO add your handling code here:
        String apduStr = "";

        if (isConnected == false) {
            infoBox(CONNECT_SIDECARD, this.getTitle());
            return;
        }

        if (APDU_jRadioButton.isSelected()) {
            String cla = CLA_jTextField.getText();
            String ins = INS_jTextField.getText();
            String p1 = P1_jTextField.getText();
            String p2 = P2_jTextField.getText();
            String lc = LC_jTextField.getText();
            String data = DATA_jTextField.getText();
            String le = LE_jTextField.getText();

            if (cla == null || ins == null || p1 == null || p2 == null) {
                return;
            }

            apduStr = cla + ins + p1 + p2 + lc + data + le;
        } else {
            apduStr = FW_jComboBox.getSelectedItem().toString();
        }
        if (apduStr.equals("")) {
            return;
        }

        if (apduStr.length() % 2 != 0) {
            infoBox(ENTER_VALID_APDU, this.getTitle());
            return;
        }
        try {
            Log_jTextArea.append("APDU \t:=> " + apduStr + "\r\n");
            byte[] apduBytes = Utilities.hexStringToBytes(apduStr);
            if (apduBytes != null) {
                byte[] resp = scardObj.transmitAPDU(apduBytes);
                if (resp != null) {
                    LogMessage("Response \t:=> " + Utilities.bytesToHexString(resp));
                } else {
                    LogMessage("Response \t:=>No response.");
                }
            } else {
                LogMessage("Invalid command.\r\n");
            }
        } catch (Exception e) {
            LogMessage("Exception: " + e.getMessage() + "\r\n");
        }
    }//GEN-LAST:event_Transmit_jButtonActionPerformed

    private void LE_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LE_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, LE_jTextField, null);
    }//GEN-LAST:event_LE_jTextFieldKeyTyped

    private void DATA_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DATA_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, DATA_jTextField, null);
    }//GEN-LAST:event_DATA_jTextFieldKeyTyped

    private void LC_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LC_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateLCField(evt, LC_jTextField, DATA_jTextField);
    }//GEN-LAST:event_LC_jTextFieldKeyTyped

    private void P2_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_P2_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, P2_jTextField, LC_jTextField);
    }//GEN-LAST:event_P2_jTextFieldKeyTyped

    private void P1_jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_P1_jTextField2KeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, P1_jTextField, P2_jTextField);
    }//GEN-LAST:event_P1_jTextField2KeyTyped

    private void INS_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_INS_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, INS_jTextField, P1_jTextField);
    }//GEN-LAST:event_INS_jTextFieldKeyTyped

    private void CLA_jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CLA_jTextFieldKeyTyped
        // TODO add your handling code here:
        validateAndMoveNextField(evt, CLA_jTextField, INS_jTextField);
    }//GEN-LAST:event_CLA_jTextFieldKeyTyped

    private void ReadCardInfo_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadCardInfo_jButtonActionPerformed
        // TODO add your handling code here:
        if (isConnected == false) {
            infoBox(CONNECT_SIDECARD, this.getTitle());
        } else {
            cardTransportKey = Des3_CBC_Key_jTextField.getText().trim();
            ReadCardInfo_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ReadCardInfo_jButton.setEnabled(false);

            LogMessage("Reading card info...");
            Log_jTextArea.update(Log_jTextArea.getGraphics());

            GetCardInfo(cardTransportKey);

            Log_jTextArea.update(Log_jTextArea.getGraphics());

            ReadCardInfo_jButton.setEnabled(true);
            ReadCardInfo_jButton.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ReadCardInfo_jButtonActionPerformed

    private void DeleteApplet_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteApplet_jButtonActionPerformed
        // TODO add your handling code here:
        selectedReaderIndex = Reader_jComboBox.getSelectedIndex();
        String reader = readers.get(selectedReaderIndex).getName();
        String param = "";
        try {
            String InstId = DeleteInstanceId_jTextField.getText().trim();
            cardTransportKey = Des3_CBC_Key_jTextField.getText().trim();
            if (InstId.isEmpty()) {
                infoBox(SELECT_APPLET_FILE_OR_AID, this.getTitle());
            } else {
                if (isConnected == false) {
                    infoBox(CONNECT_SIDECARD, this.getTitle());
                } else {
                    DeleteApplet_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DeleteApplet_jButton.setEnabled(false);

                    Thread.sleep(1);

                    LogMessage("\r\nApplet path: " + appletFilePath + "\r\nApplet deleting...");

                    if (DeleteApplet(InstId, cardTransportKey)) {
                        LogMessage("\r\nSelected applet deleted successfully." + "\r\n");
                    } else {
                        LogMessage("\r\nApplet delete failed." + "\r\n");
                    }

                    DeleteApplet_jButton.setEnabled(true);
                    DeleteApplet_jButton.setCursor(Cursor.getDefaultCursor());
                }
            }
        } catch (Exception e) {
            LogMessage(APPLET_LOAD_FAIL);
            LogMessage("\r\nError: " + e.getMessage() + "\r\n");
            DeleteApplet_jButton.setEnabled(true);
            DeleteApplet_jButton.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_DeleteApplet_jButtonActionPerformed

    private void Browse_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Browse_jButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.cap", "cap");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            File selectedFile = fileChooser.getSelectedFile();
            appletFilePath = selectedFile.getPath();
            AppletPath_jTextField.setText(appletFilePath);
            fileChooser.setCurrentDirectory(selectedFile.getParentFile());
        }
    }//GEN-LAST:event_Browse_jButtonActionPerformed

    private void AppletLoad_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AppletLoad_jButtonActionPerformed
        // TODO add your handling code here:
        selectedReaderIndex = Reader_jComboBox.getSelectedIndex();
        String reader = readers.get(selectedReaderIndex).getName();
        String param = "";
        String aid = null;
        try {
            appletFilePath = AppletPath_jTextField.getText().trim();
            cardTransportKey = Des3_CBC_Key_jTextField.getText().trim();
            if (appletFilePath.equals("")) {
                infoBox(SELECT_APPLET_FILE, this.getTitle());
            } else if (isConnected == false) {
                infoBox(CONNECT_SIDECARD, this.getTitle());
            } else {
                if (!AID_jTextField.getText().isEmpty()) {
                    aid = AID_jTextField.getText().trim();

                    if (aid.length() < 10) {
                        infoBox(APPLET_AID_LEN_ERR, this.getTitle());
                        return;
                    }
                }

                AppletLoad_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                AppletLoad_jButton.setEnabled(false);

                Thread.sleep(1);

                DefaultCaret caret = (DefaultCaret) Log_jTextArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                LogMessage("Applet path: " + appletFilePath + "\r\nApplet loading...\r\n");
                //Log_jTextArea.update(Log_jTextArea.getGraphics());

                String[] ID = new String[3];
                if (LoadApplet(appletFilePath, aid, cardTransportKey, ID)) {
                    LogMessage("Selected applet: " + appletFilePath + " loaded successfully." + "\r\n");
                } else {
                    LogMessage("\r\nApplet loading failed." + "\r\n");
                }

                AppletLoad_jButton.setEnabled(true);
                AppletLoad_jButton.setCursor(Cursor.getDefaultCursor());
            }
        } catch (Exception e) {
            LogMessage(APPLET_LOAD_FAIL);
            LogMessage("\r\nError: " + e.getMessage() + "\r\n");
            AppletLoad_jButton.setEnabled(true);
            AppletLoad_jButton.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_AppletLoad_jButtonActionPerformed

    private void prepersobrowse_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prepersobrowse_jButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            File selectedFile = fileChooser.getSelectedFile();
            PrepersoPath_jTextField.setText(selectedFile.getPath());
            LogMessage("Selected Pre-Perso Script file: " + PrepersoPath_jTextField.getText() + "\r\n");
        }
    }//GEN-LAST:event_prepersobrowse_jButtonActionPerformed

    private void appletbrowse_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appletbrowse_jButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.cap", "cap");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            File selectedFile = fileChooser.getSelectedFile();
            appletFilePath = selectedFile.getPath();
            AppletloadPath_jTextField.setText(appletFilePath);
            fileChooser.setCurrentDirectory(selectedFile.getParentFile());
            LogMessage("Selected Applet file: " + appletFilePath + "\r\n");
        }
    }//GEN-LAST:event_appletbrowse_jButtonActionPerformed

    private void Personalize_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Personalize_jButtonActionPerformed
        String appletpath = "";
        String prepersopath = "";
        String tsnvalue = "1734A00093";
        String[] appletID = new String[3];
        appletpath = AppletloadPath_jTextField.getText().trim();
        prepersopath = PrepersoPath_jTextField.getText().trim();
        tsnvalue = TSN_jTextField.getText().trim();

        
        
        LogMessage("Validating inputs...\r\n");
        if(isReaderConnected == false){
            infoBox(NO_READERS_FOUND, this.getTitle());
            LogMessage(NO_READERS_FOUND + "\r\n");
            return;
        }else if (isConnected == false) {
            infoBox(CONNECT_SIDECARD, this.getTitle());
            LogMessage(CONNECT_SIDECARD + "\r\n");
            return;
        } else if (prepersopath.equals("")) {
            infoBox(SELECT_PREPERSO_FILE, this.getTitle());
            LogMessage(SELECT_PREPERSO_FILE + "\r\n");
            return;
        } else if (appletpath.equals("")) {
            infoBox(SELECT_APPLET_FILE, this.getTitle());
            LogMessage(SELECT_APPLET_FILE + "\r\n");
            return;
        } else if (tsnvalue.equals("") || tsnvalue.length() != 10) {
            infoBox(ENTER_VALID_TSN_VALUE, this.getTitle());
            LogMessage(ENTER_VALID_TSN_VALUE + "\r\n");
            return;
        } else if (appletpath.equals("")) {
            infoBox(SELECT_APPLET_FILE, this.getTitle());
            LogMessage(SELECT_APPLET_FILE + "\r\n");
            return;
        }

        TAUPPersonalization taupobj = new TAUPPersonalization();

        if (!taupobj.ValidatePrePersoFile(prepersopath)) {
            LogMessage(SELECT_VALID_PREPERSO_FILE + "\r\n");
            infoBox(SELECT_VALID_PREPERSO_FILE, this.getTitle());
            return;
        } else if (!taupobj.ValidateAppletFile(appletpath)) {
            LogMessage(SELECT_VALID_APPLET_FILE + "\r\n");
            infoBox(SELECT_VALID_APPLET_FILE, this.getTitle());
            return;
        }      
        
        HttpPostJSON httpPostObj = new HttpPostJSON(scardObj, tsnvalue, Log_jTextArea);        
        String cardStatus = httpPostObj.getStatus();
        
        if(cardStatus == null){
            LogMessage("Not able to read the card State \r\n ");
            return;
        }else if(!( 
                cardStatus.equalsIgnoreCase("DISABLED") || cardStatus.equalsIgnoreCase("DISABLED") || 
                cardStatus.equalsIgnoreCase("MANUFACTURED")|| cardStatus.equalsIgnoreCase("PROJECT_PROVISIONED")))
        {
            LogMessage("SideCard Cannot be provisioned, its in "+ cardStatus  +" state.\r\n ");
            return;
        }else{
            LogMessage("SideCard is in"+ cardStatus  +" state.\r\n ");
        }
        
        Personalize_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            BufferedReader buffReaderObj = new BufferedReader(new FileReader(prepersopath));

            String readLine = "";

            if (buffReaderObj != null) {
                LogMessage("Preperso commands file: " + prepersopath);

                while ((readLine = buffReaderObj.readLine()) != null) {
                    if (readLine.startsWith("#") || readLine.startsWith("//")) {
                        continue;
                    } else {
                        LogMessage("APDU \t:=> " + readLine);
                        byte[] apduBytes = Utilities.hexStringToBytes(readLine);
                        if (apduBytes != null) {
                            byte[] resp = scardObj.transmitAPDU(apduBytes);
                            if (resp != null) {
                                LogMessage("Response \t:=> " + Utilities.bytesToHexString(resp));
                            }
                        } else {
                            LogMessage("Invalid APDU command.");
                        }
                    }
                }
                buffReaderObj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogMessage("\r\n Preperso APDUs executed");

        //LogMessage("\r\nTotal APDU commands transmitted: "+cnt);
        Personalize_jButton.setCursor(Cursor.getDefaultCursor());
        cardDisconnect();
        cardConnect();
        //Applet Loading..                  
        String aid = null;
        try {
            cardTransportKey = Des3_CBC_Key_jTextField.getText().trim();

            if (cardTransportKey.length() != 48) {
                LogMessage("\r\n Card Manager Authentication key is not of valid length. \r\n");
            }

            if (isConnected == false) {
                infoBox(CONNECT_SIDECARD, this.getTitle());
                LogMessage(CONNECT_SIDECARD + "\r\n");
                return;
            } else {
                Personalize_jButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Personalize_jButton.setEnabled(false);

                Thread.sleep(1);

                DefaultCaret caret = (DefaultCaret) Log_jTextArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                LogMessage("Applet path: " + appletpath + "\r\nApplet loading...\r\n");
                //Log_jTextArea.update(Log_jTextArea.getGraphics());

                if (LoadApplet(appletpath, aid, cardTransportKey, appletID)) {
                    LogMessage("Selected applet: " + appletpath + " loaded successfully." + "\r\n");
                } else {
                    LogMessage("\r\nApplet loading failed." + "\r\n");
                }

                Personalize_jButton.setEnabled(true);
                Personalize_jButton.setCursor(Cursor.getDefaultCursor());
            }
        } catch (Exception e) {
            LogMessage(APPLET_LOAD_FAIL);
            LogMessage("\r\nError: " + e.getMessage() + "\r\n");
            AppletLoad_jButton.setEnabled(true);
            AppletLoad_jButton.setCursor(Cursor.getDefaultCursor());
        }

        
        httpPostObj.setAppletID(appletID);
        
        httpPostObj.personalize();
        
        AppletLoad_jButton.setEnabled(true);
        AppletLoad_jButton.setCursor(Cursor.getDefaultCursor());
        
        Personalize_jButton.setEnabled(true);
        Personalize_jButton.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_Personalize_jButtonActionPerformed

    private void PrepersoPath_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrepersoPath_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrepersoPath_jTextFieldActionPerformed

    private void Des3_CBC_Key_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Des3_CBC_Key_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Des3_CBC_Key_jTextFieldActionPerformed

    private void TSN_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TSN_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TSN_jTextFieldActionPerformed

    private String getAppVersion() {
        String version = "";
        try {
            GetPropertyValues properties = new GetPropertyValues();
            version = properties.getPropValue();
        } catch (Exception ex) {
        }
        return version;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SideCardTestTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SideCardTestTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SideCardTestTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SideCardTestTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SideCardTestTool().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AID_jTextField;
    private javax.swing.JPanel APDU_jPanel;
    private javax.swing.JRadioButton APDU_jRadioButton;
    private javax.swing.JButton APDUsFileBrowse_jButton;
    private javax.swing.JLabel ATR_jLabel;
    private javax.swing.JButton AppletLoad_jButton;
    private javax.swing.JPanel AppletLoad_jPanel;
    private javax.swing.JPanel AppletLoad_jPanel1;
    private javax.swing.JTextField AppletPath_jTextField;
    private javax.swing.JTextField AppletloadPath_jTextField;
    private javax.swing.JButton Browse_jButton;
    private javax.swing.JLabel CLA;
    private javax.swing.JLabel CLA1;
    private javax.swing.JLabel CLA3;
    private javax.swing.JLabel CLA4;
    private javax.swing.JLabel CLA5;
    private javax.swing.JLabel CLA6;
    private javax.swing.JTextField CLA_jTextField;
    private javax.swing.JButton ClearLog_jButton;
    private javax.swing.JButton Connect_jButton;
    private javax.swing.JTextField DATA_jTextField;
    private javax.swing.JButton DeleteApplet_jButton;
    private javax.swing.JTextField DeleteInstanceId_jTextField;
    private javax.swing.JTextField Des3_CBC_Key_jTextField;
    private javax.swing.JButton Disconnect_jButton;
    private javax.swing.JButton ExitjButton;
    private javax.swing.JComboBox<String> FW_jComboBox;
    private javax.swing.JRadioButton FW_jRadioButton;
    private javax.swing.JPanel HWTokenPerso_jPanel;
    private javax.swing.JTextField INS_jTextField;
    private javax.swing.JTextField LC_jTextField;
    private javax.swing.JTextField LE_jTextField;
    private javax.swing.JScrollPane Log_jScrollPane;
    private javax.swing.JTextArea Log_jTextArea;
    private javax.swing.JTabbedPane MultiPane_jTabbedPane;
    private javax.swing.JPanel MultipleAPDUS_jPanel;
    private javax.swing.JTextField MultipleAPDUs_jTextField;
    private javax.swing.JButton MultipleTransmit_jButton;
    private javax.swing.JLabel P1_jLabel;
    private javax.swing.JTextField P1_jTextField;
    private javax.swing.JTextField P2_jTextField;
    private javax.swing.JButton Personalize_jButton;
    private javax.swing.JTextField PrepersoPath_jTextField;
    private javax.swing.JButton ReadCardInfo_jButton;
    private javax.swing.JComboBox<String> Reader_jComboBox;
    private javax.swing.JButton Reload_jButton;
    private javax.swing.JButton Reset_jButton;
    private javax.swing.JTextField TSN_jTextField;
    private javax.swing.JButton Transmit_jButton;
    private javax.swing.JButton appletbrowse_jButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton prepersobrowse_jButton;
    // End of variables declaration//GEN-END:variables
}
