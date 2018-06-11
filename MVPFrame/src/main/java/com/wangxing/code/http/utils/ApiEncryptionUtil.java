package com.wangxing.code.http.utils;


import com.wangxing.code.utils.DESPlus;

/**
 * Created by WangXing on 2017/4/18.
 * DES加密
 */

public class ApiEncryptionUtil {

    public static final String SECRET = "secret";
    public static final String SIGN = "sign";

    public String secret;
    public String sign;


    public ApiEncryptionUtil(Object... value) {
        this.secret = DESPlus.getNonce();
        encrypt(value);
    }


    private void encrypt(Object[] params) {

        StringBuffer stringBuffer = new StringBuffer();
        try {
            DESPlus desPlus = new DESPlus(secret);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stringBuffer.append(params[i]);
                }
            }
            this.sign = desPlus.encryptDES(stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
