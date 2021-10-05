using SmartCardNotifications;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SideXTestTool
{
    public class Globals
    {        
        public static string AppletFileType	= "All files (*.cap)|*.cap|All files (*.cap)|*.cap";
        public static string SelectApplet = "Select Applet";
        public static string AppletName = "Applet.cap";

        public static string SelectCertificate = "Select Certificate";
        public static string CertFileType = "All files (*.pfx)|*.pfx|All files (*.pfx)|*.pfx";

        public static string ScriptFileType = "All files (*.txt)|*.txt|All files (*.txt)|*.txt";
        public static string SelectScript = "Select Pre-Perso Script";

        public static string MSG_CHECKING_SC_READER = "Checking smart card reader...";
        public static string MSG_CHECKING_SC = "SideCard™ inserted";
        public static string MSG_CONNECT_SC = "Connect a smart card reader";
        public static string MSG_INSERT_SC = "Insert SideCard™";
        public static string MSG_REMOVED_SC = "SideCard™ removed";
        public static string MSG_NETWORK_NOT_AVAILABLE = "Check network connectivity";
        public static string MSG_INSERT_SS = "Insert SideSafe™";
        public static string MSG_SS_CONNECTED = "SideSafe™ connected";
        public static string MSG_SC_CONNECTED = "SideCard™ connected";
        public static string MSG_SC_DISCONNECTED = "SideCard™ disconnected";
        public static string MSG_SS_DISCONNECTED = "SideSafe™ disconnected";
        public static string MSG_SC_READER_REMOVED = "Smartcard reader removed";

        public static string MSG_SC_SELECTED = "SideCard™ selected";
        public static string MSG_SS_SELECTED = "SideSafe™ selected";

        public static string MSG_SS_PREPERSOING = "SideSafe™ pre-personalization started...";
        public static string MSG_SS_RECONNECTING = "Resetting SideSafe™ connection ...";
        public static string MSG_SS_APPLET_LOADING = "SideSafe™ applet loading...";
        public static string MSG_SS_APPLET_LOAD_SUCCESS = "SideSafe™ applet load successful.";
        public static string MSG_SS_APPLET_LOAD_FAIL = "SideSafe™ applet load failed.";
        public static string MSG_SS_PRE_PERSO_FAIL = "SideSafe™ pre-personalization failed.";

        public static string MSG_SC_PREPERSOING = "SideCard™ pre-personalization started...";
        public static string MSG_SC_RECONNECTING = "Resetting SideCard™ connection ...";
        public static string MSG_SC_APPLET_LOADING = "SideCard™ applet loading...";
        public static string MSG_SC_APPLET_LOAD_SUCCESS = "SideCard™ applet load successful.";
        public static string MSG_SC_APPLET_LOAD_FAIL = "SideCard™ applet load failed.";
        public static string MSG_PERSO_SUCCESS_CONTINUE = "SideSafe™ and SideCard™ personalization successful. \r\nDo you want to personalize another set of cards?";

        public static string MSG_SELECT_APPLET = "Selecting applet...";
        public static string ERR_CONNECT_ERROR = "Error in smartcard connection.";                
        public static string APP_CLOSE_CONFIRMATION = "Are you sure that you would like to close the tool?";

        public static string HOST_NAME = "int-u4ia.tyfone.com";
        public static string BASE_URL = "https://int-u4ia.tyfone.com";
        public static string PREPERSO_FILE = "SideCard_JC2_4_2R2_145_TYFONEGIDS.txt";
        public static string APPLET_FILE = "hwtoken.cap";
        public static string DES_TRANSPORT_KEY = "404142434445464748494a4b4c4d4e4f4041424344454647";
        public static string ORG_ID = "0";
        public static string PROJ_ID = "1";
        
                
        public static string SelectedDrive = "";
        public static string SelectedReader = "";

        public static CardBase m_iCard = null;
        public static bool isSideSafe = false;
        public static string ErrorMessage = "";
    }
}
