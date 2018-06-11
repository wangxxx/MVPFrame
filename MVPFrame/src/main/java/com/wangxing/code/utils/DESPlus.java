package com.wangxing.code.utils;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * DES加密
 *
 * @author Zhao.JQ
 */
public class DESPlus {
    private String encryptKey;

    public DESPlus(String strKey) throws Exception {
        encryptKey = strKey;
    }

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public String encryptDES(String encryptString) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encode(encryptedData);
    }

    //取八位随机数
    public static String Rands() {
        Random rd = new Random();

        return String.valueOf(rd.nextInt(900000) + 100000);
    }

    public static String getNonce() {
        String str = "";
        for (int i = 0; i < 8; i++) {
            char temp = (char) (Math.random() * 10 + 48);//产生随机数字
            str = str + temp;
        }
        return str;
    }

    public static String signRet(String str) {
        if (str == null)
            return null;
        // 7 和 11 互换
        char[] chArr = str.toCharArray();
        char temp = chArr[6];
        chArr[6] = chArr[10];
        chArr[10] = temp;
        String str1 = String.valueOf(chArr);
        // 前5位放到后面
        String str2 = str1.substring(5);
        String str3 = str1.substring(0, 5);
        String str4 = str2 + str3;
        return str4;
    }
}
