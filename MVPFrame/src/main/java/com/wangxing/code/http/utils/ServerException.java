package com.wangxing.code.http.utils;

import android.content.Context;

import com.wangxing.code.utils.ToastUtil;

/**
 * des:服务器请求异常
 */
public class ServerException extends Exception {

    public static final String ERROR_NO_DATA = "400";

    public String mErrorCode;
    public String mErrorMsg;

    public ServerException(String code, String message) {
        super(message);
        mErrorCode = code;
        mErrorMsg = message;
    }

    public void ToastError(Context context) {
        if (null != mErrorMsg) {
            ToastUtil.showShort(context, mErrorMsg);
        }
    }

}
