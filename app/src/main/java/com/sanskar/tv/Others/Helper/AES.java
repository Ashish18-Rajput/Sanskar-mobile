package com.sanskar.tv.Others.Helper;




import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES
{
    private static String CIPHER_NAME = "AES/CBC/NoPadding";
    public static String strArrayKey = "!*@#)($^%1fgv&C=";
    public static String strArrayvector = "?\\:><{}@#Vjekl/4";
    private static int CIPHER_KEY_LEN = 16; //128 bits



    public static String encryptPassword(String key) {
        String password = "";
        String[] base = key.split("1090##", 2);
        String sub1 = base[0];
        String sub2 = base[1];
        String[] subBase = sub2.split("==", 2);
        String s1 = subBase[0];
        String finalEncodeStart = s1 + "==";
        String endEncode = subBase[1];

        byte[] decodeStart = Base64.decode(finalEncodeStart, Base64.DEFAULT);
        String finalDecodeStart = new String(decodeStart, StandardCharsets.UTF_8);

        byte[] decodeEnd = Base64.decode(endEncode, Base64.DEFAULT);
        String finalDecodeEnd = new String(decodeEnd, StandardCharsets.UTF_8);
//password = finalDecodeStart + finalDecodeEnd;
        password = finalDecodeStart;

        return password;
    }




    public static String encrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal((data.getBytes("UTF-8")));
            String encryptedDataInBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT );
            return encryptedDataInBase64;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
     /*
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(AES.CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedData = cipher.doFinal((data.getBytes("utf-8")));

         //   byte[] encryptedData = cipher.doFinal((data.getBytes(StandardCharsets.UTF_8)));
            String encryptedDataInBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT);
            String ivInBase64 = Base64.encodeToString(iv.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

            return encryptedDataInBase64 + ":" + ivInBase64;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }*/
    }

    private static String fixKey(String key) {

        if (key.length() < AES.CIPHER_KEY_LEN) {
            int numPad = AES.CIPHER_KEY_LEN - key.length();

            for (int i = 0; i < numPad; i++) {
                key += "0"; //0 pad to len 16 bytes
            }

            return key;

        }

        if (key.length() > AES.CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        return key;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key  - key to use should be 16 bytes long (128 bits)
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */

    public static String decrypt(String data, String key, String ivParameter) {

        try {
            if (data.contains(":"))
            {
                String[] parts = data.split(":");

                IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

                Cipher cipher = Cipher.getInstance(AES.CIPHER_NAME);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

                byte[] decodedEncryptedData = Base64.decode(parts[0], Base64.NO_PADDING);

                byte[] original = cipher.doFinal(decodedEncryptedData);

                return new String(original);

            }else {

                IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

                Cipher cipher = Cipher.getInstance(AES.CIPHER_NAME);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

                byte[] decodedEncryptedData = Base64.decode(data, Base64.NO_PADDING);

                byte[] original = cipher.doFinal(decodedEncryptedData);

                return new String(original);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String generatekey(String token) {
        String finalKey = "";
        String parts = token.substring(0,16);
        Log.e("token",parts);
        for (char c : parts.toCharArray()) {
            finalKey = finalKey + strArrayKey.toCharArray()[Integer.parseInt(String.valueOf(c))];
        }
        return finalKey;
    }


    public static String generateVector(String token) {
        String finalKey = "";
        String parts = token.substring(0,16);
        Log.e("token",parts);
        for (char c : parts.toCharArray()) {
            finalKey = finalKey + strArrayvector.toCharArray()[Integer.parseInt(String.valueOf(c))];
        }
        return finalKey;
    }


    public static String generateKeyNew(String token) {
        String finalKey = "";
        String[] parts = token.split("_");
      //  Log.e("token",parts);
        for (char c : parts[2].toCharArray()) {
            finalKey = finalKey + strArrayKey.toCharArray()[Integer.parseInt(String.valueOf(c))];
        }
        return finalKey;
    }


    public static String generateVectorNew(String token) {
        String finalKey = "";
        String[] parts = token.split("_");
       // Log.e("token",parts);
        for (char c : parts[2].toCharArray()) {
            finalKey = finalKey + strArrayvector.toCharArray()[Integer.parseInt(String.valueOf(c))];
        }
        return finalKey;
    }

}

