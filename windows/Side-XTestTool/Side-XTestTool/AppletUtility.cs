using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.IO.Compression;
using System.Globalization;
using System.Security.Cryptography.X509Certificates;
using System.ComponentModel;
using System.Runtime.InteropServices;

namespace SideXTestTool
{
    class AppletUtility
    {
        public enum ProviderType : uint
        {
            PROV_RSA_FULL = 1,
            PROV_RSA_SIG = 2,
            PROV_DSS = 3,
            PROV_FORTEZZA = 4,
            PROV_MS_EXCHANGE = 5,
            PROV_SSL = 6,
            PROV_RSA_SCHANNEL = 12,
            PROV_DSS_DH = 13,
            PROV_EC_ECDSA_SIG = 14,
            PROV_EC_ECNRA_SIG = 15,
            PROV_EC_ECDSA_FULL = 16,
            PROV_EC_ECNRA_FULL = 17,
            PROV_DH_SCHANNEL = 18,
            PROV_SPYRUS_LYNKS = 20,
            PROV_RNG = 21,
            PROV_INTEL_SEC = 22,
            PROV_REPLACE_OWF = 23,
            PROV_RSA_AES = 24,
        }

        [Flags]
        public enum ContextFlags : uint
        {
            CRYPT_FIRST = 0x00000001,
            CRYPT_NEXT = 0x00000002,
            CRYPT_VERIFYCONTEXT = 0xF0000000,
            CRYPT_NEWKEYSET = 0x00000008,
            CRYPT_DELETEKEYSET = 0x00000010,
            CRYPT_MACHINE_KEYSET = 0x00000020,
            CRYPT_SILENT = 0x00000040,
            CRYPT_DEFAULT_CONTAINER_OPTIONAL = 0x00000080,
        }

        public enum ProviderParamType : uint
        {
            PP_CLIENT_HWND = 1,
            PP_ENUMALGS = 1,
            PP_ENUMCONTAINERS = 2,
            PP_IMPTYPE = 3,
            PP_NAME = 4,
            PP_VERSION = 5,
            PP_CONTAINER = 6,
            PP_CHANGE_PASSWORD = 7,
            PP_KEYSET_SEC_DESCR = 8,
            PP_CERTCHAIN = 9,
            PP_KEY_TYPE_SUBTYPE = 10,
            PP_CONTEXT_INFO = 11,
            PP_KEYEXCHANGE_KEYSIZE = 12,
            PP_SIGNATURE_KEYSIZE = 13,
            PP_KEYEXCHANGE_ALG = 14,
            PP_SIGNATURE_ALG = 15,
            PP_PROVTYPE = 16,
            PP_KEYSTORAGE = 17,
            PP_APPLI_CERT = 18,
            PP_SYM_KEYSIZE = 19,
            PP_SESSION_KEYSIZE = 20,
            PP_UI_PROMPT = 21,
            PP_ENUMALGS_EX = 22,
            PP_DELETEKEY = 24,
            PP_ENUMMANDROOTS = 25,
            PP_ENUMELECTROOTS = 26,
            PP_KEYSET_TYPE = 27,
            PP_ADMIN_PIN = 31,
            PP_KEYEXCHANGE_PIN = 32,
            PP_SIGNATURE_PIN = 33,
            PP_SIG_KEYSIZE_INC = 34,
            PP_KEYX_KEYSIZE_INC = 35,
            PP_UNIQUE_CONTAINER = 36,
            PP_SGC_INFO = 37,
            PP_USE_HARDWARE_RNG = 38,
            PP_KEYSPEC = 39,
            PP_ENUMEX_SIGNING_PROT = 40,
            PP_CRYPT_COUNT_KEY_USE = 41,
        }

        public enum KeyParameter : uint
        {
            KP_IV = 1,
            KP_SALT = 2,
            KP_PADDING = 3,
            KP_MODE = 4,
            KP_MODE_BITS = 5,
            KP_PERMISSIONS = 6,
            KP_ALGID = 7,
            KP_BLOCKLEN = 8,
            KP_KEYLEN = 9,
            KP_SALT_EX = 10,
            KP_P = 11,
            KP_G = 12,
            KP_Q = 13,
            KP_X = 14,
            KP_Y = 15,
            KP_RA = 16,
            KP_RB = 17,
            KP_INFO = 18,
            KP_EFFECTIVE_KEYLEN = 19,
            KP_SCHANNEL_ALG = 20,
            KP_CLIENT_RANDOM = 21,
            KP_SERVER_RANDOM = 22,
            KP_RP = 23,
            KP_PRECOMP_MD5 = 24,
            KP_PRECOMP_SHA = 25,
            KP_CERTIFICATE = 26,
            KP_CLEAR_KEY = 27,
            KP_PUB_EX_LEN = 28,
            KP_PUB_EX_VAL = 29,
            KP_KEYVAL = 30,
            KP_ADMIN_PIN = 31,
            KP_KEYEXCHANGE_PIN = 32,
            KP_SIGNATURE_PIN = 33,
            KP_PREHASH = 34,
            KP_ROUNDS = 35,
            KP_OAEP_PARAMS = 36,
            KP_CMS_KEY_INFO = 37,
            KP_CMS_DH_KEY_INFO = 38,
            KP_PUB_PARAMS = 39,
            KP_VERIFY_PARAMS = 40,
            KP_HIGHEST_VERSION = 41,
            KP_GET_USE_COUNT = 42,
        }

        public const uint ERROR_NO_MORE_ITEMS = 0x00000103;

        [DllImport("advapi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern Boolean CryptAcquireContext(
             ref IntPtr hProv,
             string pszContainer,
             string pszProvider,
             ProviderType dwProvType,
             ContextFlags dwFlags);

        [DllImport("advapi32.dll", CharSet = CharSet.Ansi, SetLastError = true)]
        public static extern Boolean CryptGetProvParam(
             IntPtr hProv,
             ProviderParamType dwParam,
             StringBuilder pbData,
             ref UInt32 pdwDataLen,
             UInt32 dwFlags);

        [DllImport("advapi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern Boolean CryptImportKey(
             IntPtr hProv,
             byte[] pbKeyData,
             UInt32 dwDataLen,
             IntPtr hPubKey,
             UInt32 dwFlags,
             ref IntPtr hKey);

        [DllImport("advapi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern Boolean CryptSetKeyParam(
             IntPtr hKey,
             KeyParameter dwParam,
             byte[] pbData,
             UInt32 dwFlags);

        [DllImport("advapi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern Boolean CryptDestroyKey(
             IntPtr phKey);

        [DllImport("advapi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern Boolean CryptReleaseContext(
             IntPtr hProv,
             UInt32 dwFlags);

        public static int CAP_HEADER_FILE_DATA_OFFSET_PACKAGE_AID_LENGTH = 12;
        public static int CAP_HEADER_FILE_DATA_OFFSET_PACKAGE_AID = 13;
        public static int CAP_APPLET_FILE_DATA_OFFSET_APPLET_COUNT = 3;

        public static int BLOCK_SIZE = 400;
        public static int PKGID_LENGTH_OFFEST_STR = 24;
        public static int PKGID_LENGTH_END_STR = 26;
        public static int AID_APPLET_COUNT_OFFSET_STR = 6;
        public static int AID_APPLET_COUNT_END_STR = 8;

        // ExtAuth Constants
        public static int APDU_HEADER_LENGTH = 5;
        public static int CMAC_LENGTH = 8;
        public static int OFFSET_CDATA = 10;
        public static int OFFSET_CLS = 2;
        public static int OFFSET_P2 = 8;

        // Different types of Security levels
        public static int PLAIN = 0;
        public static int C_MAC = 1;
        public static int C_ENC = 3;
        public static int CMAC_ENC = 3;

        public static String ZERO_STR = "0";
        public static String HEX_ZERO = "00";
        public static String DELETE_APDU_CONST = "4F";
        public static String END_BLOCK = "80";
        public static String NOT_LAST_BLOCK = "00";

        public static String SECURITY_AID = "A000000151000000";
        public static String SECURITY_AID_PART = "000000";
        public static String LOAD_TAG_VALUE = "C482";
        public static String DEL_HEADER = "80E40000";
        public static String DEL_APDU_HEADER = "80E40080";
        public static String LOAD_CLS_INS = "80E8";
        public static String INSTALL_CLS_INS = "80E6";
        public static String INSTALL_HEADER = "80E60C00";
        public static String APP_PREV_VALUES = "00";
        public static String INSTALL_PARAM = "C900";
        public static String SELECT_JCOP_MANAGER = "00A4040008A000000151000000";
        public static String SELECT_JCOP_MANAGER_HEADER = "00A40400";
        public static String GET_CARD_INFO_APDU = "80F21000024F0000";
        public static String GET_INSTANCEs_ID_APDU = "80F24000024F0000";

        public static string szMSBaseCsp = "Microsoft Base Smart Card Crypto Provider";

        //CAP file - sub files
        public static String[] capList = { "Header.cap", "Directory.cap", "Import.cap", "Applet.cap", "Class.cap", "Method.cap", "StaticField.cap", "ConstantPool.cap", "RefLocation.cap" };

        public static String TEMP_DIR = "\\tempDir";


        public class Packages
        {
            public String packageId = null;
            public String[] aid = null;
        }

        public List<Packages> pakegesList = null;

        /********************************************************************************
        * This method appends '0' to input string if the length of the input string is odd length
        * @strData string with even length
        ********************************************************************************/
        private String zeroFill(String strData)
        {
            String length = strData;
            if (strData != null)
            {
                if (strData.Length == 1)
                {
                    length = "0" + strData;
                    return length;
                }
                else if (strData.Length % 2 != 0)
                {
                    length = "0" + strData;
                }

            }
            return length;
        }

        /********************************************************************************
        * This method calculates input string and returns the length in hex string format
        * @data - input hex string
        * @returns input hex strings length in hex string format
        ********************************************************************************/
        private String lengthInHex(String data)
        {
            String length = null;
            int len = data.Length / 2;
            length = len.ToString("X2");
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
        public String getCardAuthenticateAPDU(int securityLevel, String randomnumber, String cardresponses, String des3_cbc_key)
        {
            String AUTH_APDU_HEADER = "8050000008";
            String CRYPTOGRAM_APDU_HEADER = "8482000010";
            //Eight byte padding values as required by the DES algorithm
            byte[] pad = { (byte)0x80, 0, 0, 0, 0, 0, 0, 0 };
            int STRLEN_8BYTES = 16;

            //16 bytes
            byte[] derivationData = { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
            byte[] ivAllZeros = { 0, 0, 0, 0, 0, 0, 0, 0 };

            byte[] hostCrypto;
            byte[] cardCrypto;

            byte[] encSessionKey;
            byte[] macSessionKey;
            byte[] skuDESKey;

            byte[] staticEncKeyBytes;
            byte[] CMac = null;
            byte[] macFromPrevCommand;

            String host_rand_no = "";
            String temp = "";
            String host_cryptogram = "";
            String final_enc_str = "";
            String ex_auth_apdu = null;

            if (randomnumber.Length < 1)
                return "";
            if (cardresponses == "" || cardresponses.Length == 4)
                return "";
            else
            {
                try
                {
                    encSessionKey = new byte[24];
                    macSessionKey = new byte[24];
                    skuDESKey = new byte[24];
                    cardCrypto = new byte[8];
                    hostCrypto = new byte[8];
                    macFromPrevCommand = new byte[8];

                    byte[] cardRandom = new byte[8];
                    byte[] sequenceNumber = new byte[2];

                    byte[] cardResponse = Utilities.hexStringToByteArray(cardresponses);
                    byte[] hostRandom = Utilities.hexStringToByteArray(randomnumber);

                    host_rand_no = randomnumber.Substring(0, STRLEN_8BYTES);
                    temp = AUTH_APDU_HEADER + host_rand_no;

                    // get the card random number from 28bytes of response
                    Array.Copy(cardResponse, 12, cardRandom, 0, 8);
                    // get the sequence number 2 byte from 28bytes of response - 26:26+2
                    Array.Copy(cardResponse, 12, sequenceNumber, 0, 2);

                    derivationData[2] = (byte)sequenceNumber[0];
                    derivationData[3] = (byte)sequenceNumber[1];

                    staticEncKeyBytes = Utilities.hexStringToByteArray(des3_cbc_key);
                    // Initialize to generate session keys

                    TripleDESCryptoServiceProvider tdes = new TripleDESCryptoServiceProvider();
                    //set the secret key for the tripleDES algorithm
                    tdes.Key = staticEncKeyBytes;
                    //mode of operation. there are other 4 modes.            
                    tdes.Mode = CipherMode.CBC;
                    //padding mode(if any extra byte added)
                    tdes.Padding = PaddingMode.None;

                    ICryptoTransform cTransform = tdes.CreateEncryptor(staticEncKeyBytes, ivAllZeros);
                    derivationData[1] = (byte)0x82;
                    byte[] resultbytes = cTransform.TransformFinalBlock(derivationData, 0, derivationData.Length);
                    //Release resources held by TripleDes Encryptor

                    // Generate ENC session key
                    Array.Copy(resultbytes, 0, encSessionKey, 0, resultbytes.Length);
                    Array.Copy(resultbytes, 0, encSessionKey, 16, 8); // Make a
                    // 192 bit
                    // key out
                    // of a 128
                    // bit key

                    // Generate C-MAC session key
                    derivationData[1] = (byte)0x01;
                    resultbytes = cTransform.TransformFinalBlock(derivationData, 0, derivationData.Length);
                    Array.Copy(resultbytes, 0, macSessionKey, 0, resultbytes.Length);
                    Array.Copy(resultbytes, 0, macSessionKey, 16, 8); // Make a
                    // 192 bit
                    // key out
                    // of a 128
                    // bit key

                    // Initialize key and cipher for Card Cryptogram creation
                    cTransform = tdes.CreateEncryptor(encSessionKey, ivAllZeros);

                    // Gather data card cryptogram
                    byte[] data = new byte[24];
                    Array.Copy(hostRandom, 0, data, 0, 8);
                    Array.Copy(cardRandom, 0, data, 8, 8);
                    Array.Copy(pad, 0, data, 16, 8);

                    // Generate Card Cryptogram. Use 8-LSB
                    byte[] cryptogram = cTransform.TransformFinalBlock(data, 0, data.Length);

                    byte[] cardCryptogram = new byte[8];
                    Array.Copy(cryptogram, 16, cardCryptogram, 0, cardCryptogram.Length);
                    Array.Copy(cardCryptogram, 0, cardCrypto, 0, cardCryptogram.Length);

                    // Gather data for host cryptogram
                    Array.Copy(cardRandom, 0, data, 0, 8);
                    Array.Copy(hostRandom, 0, data, 8, 8);
                    Array.Copy(pad, 0, data, 16, 8);

                    // Generate Host Cryptogram. Use 8-LSB
                    cryptogram = cTransform.TransformFinalBlock(data, 0, data.Length);
                    byte[] hostCryptogram = new byte[8];
                    Array.Copy(cryptogram, 16, hostCryptogram, 0, hostCryptogram.Length);
                    Array.Copy(hostCryptogram, 0, hostCrypto, 0, hostCryptogram.Length);

                    host_cryptogram = Utilities.bytesToHexString(hostCrypto, hostCrypto.Length);

                    // Gather data for C-MAC
                    data[0] = (byte)0x84;
                    data[1] = (byte)0x82;
                    data[2] = (byte)securityLevel;
                    data[3] = 0;
                    data[4] = 0x10;
                    Array.Copy(hostCryptogram, 0, data, 5, 8);
                    Array.Copy(pad, 0, data, 13, 3);

                    // Initialize key and cipher for C-MAC
                    // For SCP 02 you must encrypt the first n-1 block by single des.
                    // Use the last 8 bytes of macSessionKey as the DES key.
                    DESCryptoServiceProvider des = new DESCryptoServiceProvider();
                    //set the secret key for the tripleDES algorithm
                    tdes.Key = staticEncKeyBytes;
                    //mode of operation. there are other 4 modes.            
                    tdes.Mode = CipherMode.CBC;
                    //padding mode(if any extra byte added)
                    tdes.Padding = PaddingMode.None;

                    byte[] deskey = new byte[8];
                    Array.Copy(macSessionKey, 0, deskey, 0, 8);

                    ICryptoTransform cdesTransform = des.CreateEncryptor(deskey, ivAllZeros);

                    // Calculate the first n - 1 block. For this case, n = 1
                    byte[] ivForLastBlock = cdesTransform.TransformFinalBlock(data, 0, 8);

                    cTransform = tdes.CreateEncryptor(macSessionKey, ivForLastBlock);

                    byte[] tmpcMac = cTransform.TransformFinalBlock(data, 8, 8);
                    CMac = new byte[8];
                    Array.Copy(tmpcMac, 0, CMac, 0, tmpcMac.Length);

                    tdes.Clear();
                    des.Clear();
                }
                catch (Exception ex)
                {
                    ex_auth_apdu = "";
                }

                if (CMac != null)
                {
                    final_enc_str = Utilities.bytesToHexString(CMac, CMac.Length);

                    ex_auth_apdu = CRYPTOGRAM_APDU_HEADER + host_cryptogram + final_enc_str.Substring(0, STRLEN_8BYTES);
                }
            }

            return ex_auth_apdu;
        }
        #region cap file parsing

        /*******************************************************************************/
        /**
         * This method Reads the file and returns the binary data
         * 
         * @param capfile - selected cap file
         * @return the binary data of passed cap file binary data
         */
        public byte[] getBytesFromFile(String capfile)
        {
            int readBytes = 0;
            try
            {
                var fs = new FileStream(capfile, FileMode.Open);
                var len = (int)fs.Length;
                var bytes = new byte[len];
                readBytes = fs.Read(bytes, 0, len);
                fs.Close();
                if (readBytes != len)
                    return null;
                return bytes;
            }
            catch (FileNotFoundException ex)
            {
                //ex.printStackTrace();
            }
            catch (IOException e)
            {
                //e.printStackTrace();
            }
            return null;
        }

        /*******************************************************************************/
        /**
         *  This method is to extract the selected cap file into sub cap files
         * 
         * @param filePath - selected cap  file path
         * @param destDir - cap file extract destination folder path...application path
         * @return cap file extracted directory path
         */
        public String extractCapFile(String filePath, String destDir)
        {
            String currentDir = "";

            if (filePath != null)
            {
                try
                {
                    ZipFile.ExtractToDirectory(filePath, destDir);

                    ZipArchive zip = ZipFile.OpenRead(filePath);
                    foreach (ZipArchiveEntry entry in zip.Entries)
                    {
                        if (currentDir == "" && entry.FullName.Contains("Header.cap"))
                        {
                            currentDir = destDir + "\\" + Path.GetDirectoryName(entry.FullName);
                            break;
                        }
                    }
                }
                catch (Exception ex)
                {

                }
            }

            return currentDir;
        }

        /**
        * This method reads  the files listed in the capList[] one by one as a sequence of bytes and
        * stores them in the String variable finalOutput.
        * @param tempDir - capfile extracted path
        * @param capList - list of capfiles to be read
        * @return cap file binary data as hex string
        */
        public String getcapFilesBinaryData(String tempDir, String[] capList)
        {
            byte[] output = null;
            StringBuilder finalOutput = new StringBuilder();
            foreach (String filename in capList)
            {
                String filepath = tempDir + '/' + filename;
                //if(jarFiles.contains(file)){            
                output = getBytesFromFile(filepath);
                String temp = Utilities.bytesToHexString(output, output.Length);
                finalOutput.Append(temp);
                //}
            }
            return finalOutput.ToString();
        }

        /**
        * This method retrieves the Package ID from the selected cap file's  file 'Applet.cap'
        * @param fileName - selected cap file's  file 'Applet.cap'
        * @return the selected applet's package ID
        */
        public String[] getPackageID(String fileName)
        {
            String[] pkgid = null;
            byte[] arrayBytes = getBytesFromFile(fileName);
            String inputString = Utilities.bytesToHexString(arrayBytes, arrayBytes.Length);

            String tag_str = inputString.Substring(0, 2);

            int tag = Convert.ToInt16(tag_str, 16);
            long size = Convert.ToInt32(inputString.Substring(2, 4), 16);

            int i = Convert.ToInt16(inputString.Substring(24, 2), 16);
            i *= 2;
            pkgid = new String[2];
            pkgid[0] = inputString.Substring(26, i);
            if (inputString.Length > (26 + i))
            {
                int j = Convert.ToInt16(inputString.Substring((26 + i), 2), 16);
                if (j >= 5)
                {
                    j *= 2;
                    pkgid[1] = inputString.Substring((28 + i), j);
                }
                else
                {
                    String temp = pkgid[0];
                    pkgid = new String[1];
                    pkgid[0] = temp;
                }
            }
            return pkgid;
        }

        /**
         * This function will retrieves the Applet ID from the input string
         * @param fileName - 'Applet.cap' file path
         * @return the selected cap file's AppletID (AID)
         */
        public String[] getAppletInstanceId(String fileName)
        {
            String[] aid=null;
            byte[] arrayBytes = getBytesFromFile(fileName);
            String inputString = Utilities.bytesToHexString(arrayBytes,arrayBytes.Length);
            String tagStr = inputString.Substring(0,2);
            int tag = Convert.ToInt16(tagStr,16);
            long sz = Convert.ToInt32(inputString.Substring(2, 4), 16);
            int noOfApplets = Convert.ToInt16(inputString.Substring(6, 2), 16);
            int indexJ = 8 ,indexK =10;
            aid = new String[noOfApplets];
            for(int i = 0;i<noOfApplets;i++){
                int length = Convert.ToInt16(inputString.Substring(indexJ, (indexK - indexJ)), 16);
                length *= 2;
                aid[i]= inputString.Substring(indexK,((length+indexK)-indexK));
                length += 4;      
                indexJ = length+indexK;
                indexK = indexJ + 2;            
            }
    
            return aid;
        }

        /**
        * This method prepares chunks of data of cap file binary data with specified block size
        * 
        * @param output - capfile binary data as input
        * @param blockSize - each block size e.g 400 chars...200bytes
        * @return array of blocks of capfile data
        */
        public String[] frameBlockData(String output, int blockSize)
        {
            if (output.Length <= 1)
                return null;
            String[] blockData = null;

            int totalLength = output.Length;
            totalLength /= 2;

            //Total length of the whole data block
            String hexadecimalValue = totalLength.ToString("X4");
            while (hexadecimalValue.Length < 4)
            {
                hexadecimalValue = ZERO_STR + hexadecimalValue;
            }

            String partOutput = LOAD_TAG_VALUE + hexadecimalValue;
            partOutput = partOutput + output;

            int startPos = 0;
            int endPos = blockSize;
            int len = partOutput.Length;
            int numBlock = (partOutput.Length / blockSize);
            int rem = partOutput.Length % blockSize;
            if (rem > 0)
                blockData = new String[numBlock + 1];

            if (numBlock == 0 && totalLength > 10)
            {
                blockData = new String[numBlock + 1];
                blockData[numBlock] = partOutput;
            }
            for (int index = 0; index < numBlock; index++)
            {
                blockData[index] = partOutput.Substring(startPos, blockSize);
                startPos += blockSize;                
            }/* End of the for loop */

            if (startPos < (partOutput.Length))
            {
                blockData[numBlock] = partOutput.Substring(startPos);
            }


            return blockData;
        }

        /**
        * This method parses the input data to set the privilege parameters
        * @param data - input data to parse the privilege
        * @return the privilege values as string
        */
        private String calculatPrivilege(String data)
        {
            int index = 0;
            int finalOutput = 0;
            String res = "";
            String[] priv = { "S", "V", "E", "L", "T", "D", "P", "M" };
            int[] value = { 0x80, 0x40, 0x20, 0x10, 0x8, 0x4, 0x02, 0x1 };

            while (index <= 7)
            {
                Boolean val = data.Contains(priv[index]);
                if (val == true)
                {
                    finalOutput += value[index];
                }
                index++;
            }
            res = finalOutput.ToString("X2");
            if (res.Length % 2 != 0)
            {
                res = "0" + res;
            }
            return res;
        }

        /**
        * This method Constructs the Install Command APDU using Package Id and applet Id
        * @param appletId - selected Applet ID
        * @param instanceId - applet instance ID
        * @param appSpecificParam - application specific params
        * @param appPrivileges - application previlages
        * @param packageId - selected applet Package ID
        * @return the Install APDU header with packageID and applet Id
        */
        public String createInstallAPDU(String appletId, String instanceId, String appSpecificParam, String appPrivileges, String packageId)
        {
            String apdu = "";
            String length = "";

            apdu = lengthInHex(packageId);
            apdu = apdu + packageId;
            length = lengthInHex(appletId);
            length = length + appletId;

            if (appPrivileges == null && instanceId != null)
            {
                String instId = "";
                instId = lengthInHex(instanceId);
                instId = instId+instanceId;
                length = length + instId;
                apdu = apdu + length;
                length = lengthInHex(APP_PREV_VALUES);
                length = length + calculatPrivilege(APP_PREV_VALUES);//appPrivileges));            
            }
            if (appPrivileges != null)
            {
                String instId = "";
                if (instanceId != null)
                {
                    instId = lengthInHex(instanceId);
                    instId = instId +instanceId;
                    length = length + instId;
                    apdu = apdu + length;
                    length = lengthInHex(APP_PREV_VALUES);
                    length = length + calculatPrivilege(appPrivileges);
                }
                else
                {
                    //Attaching once again the length + AID
                    length = length+length;
                    apdu = apdu + length;
                    length = lengthInHex(APP_PREV_VALUES);
                    length = length+calculatPrivilege(appPrivileges);
                }
            }
            else if (instanceId == null && appPrivileges == null)
            {
                //Attaching once again the length + AID
                length = length + length;
                apdu = apdu + length;
                length = lengthInHex(APP_PREV_VALUES);
                length = length+calculatPrivilege(APP_PREV_VALUES);//appPrivileges));                 
            }
            apdu = apdu + length;
            if (appSpecificParam != null)
            {
                String dataPart = lengthInHex(appSpecificParam);
                dataPart = "C9" + dataPart;
                length = lengthInHex(dataPart + appSpecificParam);
                length = length + dataPart;
                length = length + appSpecificParam;
                apdu = apdu + length;
            }
            else
            {
                length = lengthInHex(INSTALL_PARAM);
                length = length + INSTALL_PARAM;
                apdu = apdu + length;
            }

            //TODO: Find out the reason for this extra 00 byte in the INSTALL APDU
            apdu = apdu + HEX_ZERO;
            length = lengthInHex(apdu);
            String installApdu = INSTALL_HEADER + length;
            installApdu = installApdu + apdu;
            //        System.out.println(""+installApdu);
            return installApdu;
        }

        /**
         * This method Constructs the Upload Command APDU using Package Id
         * 
         * @param packageId - selected applet Package ID
         * @param numBlock - applet cap file binary data divided into no.of blocks
         * @return the applet install APDU  as string
         */
        public String getInstallAPDU(String packageId, int numBlock)
        {
            String apdu = INSTALL_CLS_INS;
            String len = zeroFill((numBlock - 1).ToString("X2"));
            len = len + (HEX_ZERO);
            //TODO: len has not going to be  + niated .
            apdu = apdu + ("0200");
            String length = lengthInHex(packageId);
            length = length + (packageId);
            String temp = lengthInHex(SECURITY_AID);
            length = length + (temp);
            length = length + (SECURITY_AID);
            length = length + (SECURITY_AID_PART);
            String tmp = lengthInHex(length);
            tmp = tmp + (length);
            apdu = apdu + (tmp);
            return apdu;
        }

        /**
         * This method Constructs the Delete APDU for the given package Id
         * 
         * @param aId- selected applet Instance ID
         * @param packageId- selected applet Package ID
         * @return the APDU to delete the package as string
         */
        public String getDeleteAPDU(String aId, String packageId)
        {           
            if (!String.IsNullOrEmpty(aId))
                packageId = aId;
            String length = lengthInHex(packageId);
            packageId = length + (packageId);
            packageId = DELETE_APDU_CONST + (packageId);
            length = lengthInHex(packageId);
            packageId = length + (packageId);
            if (!String.IsNullOrEmpty(aId))
                packageId = DEL_HEADER + (packageId);
            else
                packageId = DEL_APDU_HEADER + (packageId);
            return packageId;
        }


        /**
         * This method will create APDU header for each of the Upload data block.
         * 
         * @param blockData - input of capfile data as array of blocks
         * @return array of blocks, each block with APDU header
         */
        private String[] blockDataWithHeader(String[] blockData)
        {
            String apduHeader = null;
            String[] blocks = null;
            int numBlock = blockData.Length;
            blocks = new String[numBlock];
            for (int blockNumber = 0; blockNumber < numBlock; blockNumber++)
            {
                //Initialize tem with CLS and INS as 80E8
                apduHeader = LOAD_CLS_INS;

                if (blockNumber == (numBlock - 1))
                {
                    apduHeader = apduHeader + END_BLOCK;
                }
                else
                {
                    apduHeader = apduHeader + NOT_LAST_BLOCK;
                }

                String valueOfP2 = blockNumber.ToString("X2");
                valueOfP2 = zeroFill(valueOfP2);
                apduHeader = apduHeader + valueOfP2;
                String lengthOfDataVal = lengthInHex(blockData[blockNumber]);
                apduHeader = apduHeader + lengthOfDataVal;
                blocks[blockNumber] = apduHeader + blockData[blockNumber];
            }

            return blocks;
        }

        /**
        * 
        * Creates the list of Delete APDUs by reading the CAP file specified in the input
        * 
        * @param secLevel - communication security level plan or encrypt. default will be plain
        * @param delAID - list of package ID/Instance ID
        * @return arraylist of apdu strings
        */
        public List<String> frameDeleteAppletAPDUs(int secLevel, String[] delAID)
        {
            int index = 0;
            List<String> apdusList = null;
            String resStr = "";
            String data = "";
            int cnt = delAID.Length;

            apdusList = new List<String>();
            /*-----------------------Delete APDU------------------*/
            for (int i = 0; i < cnt; i++)
                data = delAID[i];

            data = DEL_APDU_HEADER + data.Substring(8);

            if (secLevel == CMAC_ENC || secLevel == C_MAC)
            {
                index = 0;
                //resStr =  data; //encrypted data
            }
            else
            {
                resStr = data;
            }

            apdusList.Add(resStr);

            return apdusList;
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
        public List<String> frameLoadAppletAPDUs(int secLevel, String[] delAID, String uploadAPDU, String[] blockData, String[] installAPDU)
        {
            int index = 0;
            List<String> apdusList = new List<String>();//String[] apdusList=null;
            String resStr = "";
            String deleteData = "";

            int cnt = delAID.Length;

            //apdusList = new String[cnt];
            /*------------------Delete APDU------------------*/
            for (int i = 0; i < cnt; i++)
                deleteData = delAID[i];

            deleteData = DEL_APDU_HEADER + deleteData.Substring(8);
            if (secLevel == CMAC_ENC || secLevel == C_MAC)
            {
                index = 0;
                // TBD
                //deleteData = getEncrypedData(deleteData
                //resStr = deleteData
            }
            else
            {
                resStr = deleteData;
                apdusList.Add(resStr);
                index += 1;
            }
            /* ----------------Upload APDU-------------------*/
            if (secLevel == CMAC_ENC || secLevel == C_MAC)
            {
                index = 0;
                // dataEnc = getEncrypedData(uploadAPDU)
                // resStr =  dataEnc
            }
            else
            {
                resStr = uploadAPDU;
                apdusList.Add(resStr);
                index += 1;
            }

            /* ----------------Load APDU----------------------*/
            for (int i = 0; i < blockData.Length; i++)
            {
                if (secLevel == CMAC_ENC || secLevel == C_MAC)
                {
                    index = 0;
                    // dataEnc = getEncrypedData(blockData[i])
                    // resStr =  dataEnc
                }
                else
                {
                    resStr = blockData[i];
                    apdusList.Add(resStr);
                    index += 1;
                }
            }
            /* ----------------Install APDU---------------------*/
            if (installAPDU.Length > 0)
            {
                for (int j = 0; j < installAPDU.Length; j++)
                {
                    if (secLevel == CMAC_ENC || secLevel == C_MAC)
                    {
                        index = 0;
                        //dataEnc = getEncrypedData(installAPDU[i])
                        //resStr = dataEnc
                    }
                    else
                    {
                        resStr = installAPDU[j];
                        apdusList.Add(resStr);
                        index += 1;
                    }
                }
            }

            return apdusList;
        }


        /**
         * This method processes selected cap file and returns the list of apdus to load applet.
         * 
         * @param capFilepath - selected cap file path
         * @param instaid - applet instance AID, hex string
         * @param appSpecificParam - application specific parameters
         * @param appPrivileges- applet specific privilege parameters
         * @return string list of apdus on success or null on failure
         */
        public List<String> getLoadAppletAPDUs(String capFilepath, String instaid, String appSpecificParam, String appPrivileges)
        {
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
            secLevel = 0;
            blockSize = BLOCK_SIZE;
            List<String> capFileAPDUs=null;
            String tempDir="";
    
            //extract the cap file

            String capExtractDir = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + "\\Side-XTestTool" + TEMP_DIR;

            if (!String.IsNullOrEmpty(capExtractDir))
            {
                if(Directory.Exists(capExtractDir))
                    Directory.Delete(capExtractDir, true);
            }

            tempDir = extractCapFile(capFilepath, capExtractDir);

            if(tempDir == "")
                return capFileAPDUs;

            //read all cap files binary data
            finalLoadDatas = getcapFilesBinaryData(tempDir, capList);

            //set header for each block
            blockData = frameBlockData(finalLoadDatas, blockSize);

            numBlock = blockData.Length;

            //get the PACKAGE ID INFO
            PKGID = getPackageID(tempDir + "\\Header.cap");

            //get the Applet ID info
            AID = getAppletInstanceId(tempDir + "\\Applet.cap");

            if(AID != null)
                delAID = new String[AID.Length + 1];
            else 
                delAID = new String[PKGID.Length];

            if (AID.Length > 0){
                //delAID will have the full APDU for deleting an Applet, after calling getDeleteAPDU method.
                for (int i=0; i<AID.Length;i++){
                    if (i == 0 && instaid != null)
                        delAID[i] = getDeleteAPDU(instaid,null);
                    else
                        delAID[i] = getDeleteAPDU(AID[i], null);
                }

                // Each cap file consists of only one package, thats why PKGID index is always zero.
                // Last delAID element will have the full APDU for deleting the Package.
                delAID[AID.Length] = getDeleteAPDU(null, PKGID[0]);
            }
            else{
                delAID[0] = getDeleteAPDU(null, PKGID[0]);
            }

            // uploadAPDU will have the APDU for installing an applet.
            uploadAPDU = getInstallAPDU(PKGID[0], numBlock);
            //Sub Upload APDU's will be created
            blockData = blockDataWithHeader(blockData);
            //Install APDU block values            
            if (AID.Length > 0)
            {
                int i = 0;
                installAPDU = new String[AID.Length];
                foreach(String aId in AID){
                    if(i == 0 && instaid != null)
                        installAPDU[i] = createInstallAPDU(aId, instaid, appSpecificParam, appPrivileges, PKGID[0]);
                    else
                        installAPDU[i] = createInstallAPDU(aId, null, appSpecificParam, appPrivileges, PKGID[0]);
                    i++;
                }
            }  
            //load applet APDUs
            capFileAPDUs = frameLoadAppletAPDUs(secLevel, delAID, uploadAPDU,  blockData, installAPDU);
            //APDUs generated for selected applet load"


            //Delete tempXML folder and its content
            if (!String.IsNullOrEmpty(capExtractDir))
            {
                if (Directory.Exists(capExtractDir))
                    Directory.Delete(capExtractDir, true);
            }


            return capFileAPDUs;
        }

        /**
         * This method processes selected cap file and returns the list of apdus to delete applet.
         * 
         * @param capFilepath - selected cap file path
         * @param aid - applet Instance ID, hex string
         * @param pkg_id - applet package ID, hex string
         * @return string list of apdus on success or null on failure
         */
        public List<String> getDeleteAppletAPDUs(String capFilepath, String aid, String pkg_id)
        {
            String[] PKGID;
            String[] AID;
            String[] delAID;
            int blockSize;
            int secLevel;

            secLevel = 0;
            blockSize = BLOCK_SIZE;
            List<String> capFileAPDUs = null;
            String tempDir = "";

            if (!String.IsNullOrEmpty(aid))
            {
                delAID = new String[1];
                delAID[0] = getDeleteAPDU(aid,null);
            }
            else if (!String.IsNullOrEmpty(pkg_id))
            {
                delAID = new String[1];
                delAID[0] = getDeleteAPDU(null,pkg_id);
            }
            else
            {            //extract the cap file
                String capExtractDir = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + "\\Side-XTestTool" + TEMP_DIR;

                tempDir = extractCapFile(capFilepath, capExtractDir);

                if (tempDir == "")
                    return capFileAPDUs;

                //get the PACKAGE ID INFO
                PKGID = getPackageID(tempDir + "\\Header.cap");

                //get the Applet ID info
                AID = getAppletInstanceId(tempDir + "\\Applet.cap");

                if (AID != null)
                    delAID = new String[AID.Length + 1];
                else
                    delAID = new String[PKGID.Length];

                if (AID.Length > 0)
                {
                    //delAID will have the full APDU for deleting an Applet, after calling getDeleteAPDU method.
                    for (int i = 0; i < AID.Length; i++)
                    {
                        delAID[i] = getDeleteAPDU(AID[i], null);
                    }

                    // Each cap file consists of only one package, thats why PKGID index is always zero.
                    // Last delAID element will have the full APDU for deleting the Package.
                    delAID[AID.Length] = getDeleteAPDU(null,PKGID[0]);
                }
                else
                {
                    delAID[0] = getDeleteAPDU(null, PKGID[0]);
                }

                //Delete tempXML folder and its content
                if (!String.IsNullOrEmpty(capExtractDir))
                    Directory.Delete(capExtractDir, true);
            }

            //delete applet
            //applet delete APDUs
            capFileAPDUs = frameDeleteAppletAPDUs(secLevel, delAID);


            return capFileAPDUs;
        }

        /**
         * This method parses the passed string and extracts the package ID and applet Ids
         * 
         * @param data - card response which contains card info
         * @return list of Packages structures
         */
        public List<Packages> getPckages(String data)
        {
            int index = 0;
            int len = 0;
            int cnt = 0;
            String sub = "";
            String lenStr = "";
            List<Packages> packagesList = null;

            if (!String.IsNullOrEmpty(data))
            {
                packagesList = new List<Packages>();
                do
                {
                    Packages pkg = new Packages();
                    index = 0;
                    lenStr = data.Substring(index, 2);
                    len = Convert.ToInt16(lenStr, 16);
                    len = len * 2;
                    index += 2;
                    pkg.packageId = data.Substring(index, len);
                    index += len;
                    sub = data.Substring(index, 6);
                    index += 6;
                    cnt = Convert.ToInt16(sub.Substring(4, 2), 16);
                    data = data.Substring(index);
                    pkg.aid = new String[cnt];
                    index = 0;
                    for (int i = 0; i < cnt; i++)
                    {
                        len = Convert.ToInt16(data.Substring(index, 2), 16);
                        len = len * 2;
                        index += 2;
                        pkg.aid[i] = data.Substring(index, len);
                        index += len;
                    }
                    packagesList.Add(pkg);
                    data = data.Substring(index);
                } while (!data.Equals("9000"));
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
        public List<String> getInstaceIDs(String data)
        {
            int index = 0;
            int len = 0;
            int cnt = 0;
            String sub = "";
            String lenStr = "";
            List<String> instaceIdList = null;

            try
            {
                if (!String.IsNullOrEmpty(data))
                {
                    instaceIdList = new List<String>();

                    if (data.Length <= 4)
                        return instaceIdList;

                    do
                    {
                        String instanceID = "";
                        index = 0;
                        lenStr = data.Substring(index, 2);
                        len = Convert.ToInt16(lenStr, 16);
                        len = len * 2;
                        index += 2;
                        instanceID = data.Substring(index, len);
                        instaceIdList.Add(instanceID);
                        index += len;
                        index += 4; //0700 
                        data = data.Substring(index);
                    } while (!data.Equals("9000"));
                }
            }
            catch (Exception ex)
            {

            }

            return instaceIdList;
        }

        /**
         * This method processes selected cap file and returns the list of applet IDs (package id, instance Id).
         * 
         * @param capFilepath - selected cap file path
         * @return string list of applet Ids on success or null on failure
         */
        public string[] getAppletIDs(String capFilepath)
        {
            String[] PKGID;
            String[] AID;
            String[] ID = new string[3];
            String tempDir = "";

            //extract the cap file

            String capExtractDir = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + "\\Side-XTestTool" + TEMP_DIR;

            if (!String.IsNullOrEmpty(capExtractDir))
            {
                if (Directory.Exists(capExtractDir))
                    Directory.Delete(capExtractDir, true);
            }

            tempDir = extractCapFile(capFilepath, capExtractDir);

            if (tempDir == "")
                return ID;

            //get the PACKAGE ID INFO
            PKGID = getPackageID(tempDir + "\\Header.cap");

            //get the Applet ID info
            AID = getAppletInstanceId(tempDir + "\\Applet.cap");

            ID[0] = PKGID[0];
            ID[1] = AID[0];

            //Delete tempXML folder and its content
            if (!String.IsNullOrEmpty(capExtractDir))
            {
                if (Directory.Exists(capExtractDir))
                    Directory.Delete(capExtractDir, true);
            }

            return ID;
        }

        /**
         * This method loads the passed pfx/p12 certificate into the smartcard
         * 
         * @param certPath - certificate in .pfx/p12 format file path
         * @param certPwd - certificate passphrase
         * @return true on certificate load success, false on failure
         */
        public Boolean LoadPFXCertificate(string certPath, string certPwd)
        {
            bool isLoaded = false;
            X509Certificate2 x509 = null;

            if (String.IsNullOrEmpty(certPath))
            {
                Console.WriteLine("Please pass proper pfx file path.");
                return isLoaded;
            }


            try
            {
                Console.WriteLine("For the MS Base Smart Card CSP, two registry values must first be set to 1 : ");
                Console.WriteLine("\tAllowPrivateExchangeKeyImport.\n\tAllowPrivateSignatureKeyImport.\n");
                Utilities.SetRegKeyValues();
                x509 = new X509Certificate2(certPath, certPwd, X509KeyStorageFlags.Exportable);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Failed to open the PFX file. Error : {0}", ex);
                return isLoaded;
            }

            if (x509.HasPrivateKey)
            {
                if (x509.PublicKey.Oid.FriendlyName.Contains("RSA"))
                {
                    RSACryptoServiceProvider key = (RSACryptoServiceProvider)x509.PrivateKey;
                    byte[] pBlob = key.ExportCspBlob(true);
                    byte[] pbCert = x509.RawData;

                    Console.WriteLine("Found {0} bits RSA private key. Loading into the smart card.\n", key.KeySize);

                    IntPtr hProv = IntPtr.Zero;

                    bool bStatus = CryptAcquireContext(ref hProv, "", szMSBaseCsp, ProviderType.PROV_RSA_FULL, ContextFlags.CRYPT_NEWKEYSET);
                    if (bStatus)
                    {
                        // Get the container name
                        string szContainerName = "";
                        StringBuilder szVal = new StringBuilder(1024);
                        uint dwDataLen = 1024;

                        bStatus = CryptGetProvParam(hProv, ProviderParamType.PP_CONTAINER, szVal, ref dwDataLen, 0);
                        if (bStatus)
                            szContainerName = szVal.ToString();

                        IntPtr hKey = IntPtr.Zero;
                        bStatus = CryptImportKey(hProv, pBlob, (UInt32)pBlob.Length, IntPtr.Zero, 0, ref hKey);
                        if (bStatus)
                        {
                            bStatus = CryptSetKeyParam(hKey, KeyParameter.KP_CERTIFICATE, pbCert, 0);
                            if (bStatus)
                            {
                                Console.WriteLine("Key and certificate loaded successfully into the card");
                                if (szContainerName.Length > 0)
                                {
                                    Console.WriteLine("\tUsed container = \"{0}\"", szContainerName);
                                }
                                isLoaded = true;
                            }
                            else
                            {
                                Win32Exception ex = new Win32Exception();
                                Console.WriteLine("Private key imported into the card but certificate loading failed with error 0x" + ex.NativeErrorCode.ToString("X8") + "\nDescription : " + ex.Message);
                            }
                            CryptDestroyKey(hKey);
                        }
                        else
                        {
                            Win32Exception ex = new Win32Exception();
                            Console.WriteLine("CryptImportKey failed with error 0x" + ex.NativeErrorCode.ToString("X8") + "\n\tDescription : " + ex.Message);
                        }

                        CryptReleaseContext(hProv, 0);

                        if (!bStatus && szContainerName.Length > 0)
                        {
                            // delete the container because there was an error
                            CryptAcquireContext(ref hProv, szContainerName, szMSBaseCsp, ProviderType.PROV_RSA_FULL, ContextFlags.CRYPT_DELETEKEYSET);
                        }
                    }
                    else
                    {
                        Win32Exception ex = new Win32Exception();
                        Console.WriteLine("CryptAcquireContext failed with error 0x" + ex.NativeErrorCode.ToString("X8") + "\n\tDescription : " + ex.Message);
                    }
                }
                else
                {
                    Console.WriteLine("PFX file contains an unsupported key type ({0})", x509.PublicKey.Oid.FriendlyName);
                }
            }
            else
            {
                Console.WriteLine("No private key found in the PFX file");
            }

            return isLoaded;
        }

        /**
         * This method deletes the certificate with container name
         * 
         * @param containerName - certificate container name
         * @return true on certificate delete , false on failure
         */
        public Boolean deleteCertificate(string containerName)
        {
            bool isDone = false;
            IntPtr hProv = IntPtr.Zero;
            try
            {
                if (!String.IsNullOrEmpty(containerName))
                {
                    isDone = CryptAcquireContext(ref hProv, containerName, szMSBaseCsp, ProviderType.PROV_RSA_FULL, ContextFlags.CRYPT_DELETEKEYSET);
                }
            }
            catch (Win32Exception ex)
            {
                Console.WriteLine("CryptAcquireContext failed with error 0x" + ex.NativeErrorCode.ToString("X8") + "\n\tDescription : " + ex.Message);
            }

            return isDone;
        }

        public List<X509Certificate2> GetCertificates(string reader)
        {
            List<X509Certificate2> certs = new List<X509Certificate2>();
            X509Store x509Store = null;

            //string reader = "Generic Smart Card Reader Interface 0";
            string readercontainer = String.Format("\\\\.\\{0}\\", reader);

            try
            {
                List<string> containers = GetKeyContainers(Globals.SelectedReader);

                if (containers==null)
                {
                    return null;
                }

                x509Store = new X509Store(StoreName.My, StoreLocation.CurrentUser);

                x509Store.Open(OpenFlags.ReadOnly | OpenFlags.OpenExistingOnly);

                foreach (string container in containers)
                {
                    CspParameters cspParameters = new CspParameters((int)ProviderType.PROV_RSA_FULL, szMSBaseCsp, readercontainer+container);
                    cspParameters.KeyContainerName = container;
                    cspParameters.Flags = CspProviderFlags.UseExistingKey;
                    string pubKeyXml = null;

                    try
                    {
                        using (RSACryptoServiceProvider rsaProvider = new RSACryptoServiceProvider(cspParameters))
                            if (rsaProvider.CspKeyContainerInfo.HardwareDevice) // sure - smartcard
                            {
                                pubKeyXml = rsaProvider.ToXmlString(false);
                            }
                    }
                    catch
                    {
                    }
                    foreach (X509Certificate2 cert in x509Store.Certificates)
                    {
                        if ((cert.PublicKey.Key.ToXmlString(false) == pubKeyXml) && cert.HasPrivateKey)
                        {
                            certs.Add(cert);
                            Console.WriteLine("Key Container: " + container);
                        }
                    }
                }
            }
            finally
            {
                if (x509Store != null)
                {
                    x509Store.Close();
                    x509Store = null;
                }
            }

            return certs;
        }

        public List<string> GetKeyContainers(string reader)
        {
            List<string> containers = new List<string>();
            //string reader = "Generic Smart Card Reader Interface 0";
            string readercontainer = String.Format("\\\\.\\{0}\\", reader);
            IntPtr hProv = IntPtr.Zero;

            try
            {
                if (!CryptAcquireContext(ref hProv, readercontainer, szMSBaseCsp, ProviderType.PROV_RSA_FULL, ContextFlags.CRYPT_VERIFYCONTEXT))
                    throw new Win32Exception(Marshal.GetLastWin32Error());

                uint pcbData = 0;
                uint dwFlags = (uint)ContextFlags.CRYPT_FIRST;
                if (!CryptGetProvParam(hProv, ProviderParamType.PP_ENUMCONTAINERS, null, ref pcbData, dwFlags))
                    throw new Win32Exception(Marshal.GetLastWin32Error());

                StringBuilder sb = new StringBuilder((int)pcbData + 1);
                while (CryptGetProvParam(hProv, ProviderParamType.PP_ENUMCONTAINERS, sb, ref pcbData, dwFlags))
                {
                    containers.Add(sb.ToString());
                    dwFlags = (uint)ContextFlags.CRYPT_NEXT;
                }

                int err = Marshal.GetLastWin32Error();
                if (err != ERROR_NO_MORE_ITEMS)
                    throw new Win32Exception(err);

                if (hProv != IntPtr.Zero)
                {
                    if (!CryptReleaseContext(hProv, 0))
                        throw new Win32Exception(Marshal.GetLastWin32Error());
                    hProv = IntPtr.Zero;
                }
            }
            catch
            {
                if (hProv != IntPtr.Zero)
                {
                    if (!CryptReleaseContext(hProv, 0))
                        throw new Win32Exception(Marshal.GetLastWin32Error());
                    hProv = IntPtr.Zero;
                }

                containers = null;
            }

            return containers;
        }

        #endregion

    }
}
