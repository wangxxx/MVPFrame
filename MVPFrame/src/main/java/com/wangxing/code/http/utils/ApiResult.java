package com.wangxing.code.http.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WangXing on 2018/3/6.
 */

public class ApiResult<T> {

    @SerializedName("code")
    public String code;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;

    public boolean isOk() {
        return code.equals("200");
    }

}
