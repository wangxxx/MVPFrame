package com.wangxing.code.manager;

import android.text.TextUtils;

import com.wangxing.code.FrameConst;
import com.wangxing.code.utils.ACache;
import com.wangxing.code.utils.GsonUtil;

import java.util.UUID;


public class UserManager {
    public static final int TYPE_ACCOUNT_QQ = 1;
    public static final int TYPE_ACCOUNT_SINA = 2;
    public static final int TYPE_ACCOUNT_WECHAT = 3;
    public static final int TYPE_ACCOUNT_PHONE = 4;
    public static String TYPE_LOGIN = "type_login";

    private static volatile UserManager sInstance;


    private UserManager() {

    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    public void saveUserId(String userId) {
        ACache.get(FrameConst.getContext()).put(FrameConst.USER_ID, userId);
    }

    public void saveUserStatus(String userStatus) {
        ACache.get(FrameConst.getContext()).put(FrameConst.USER_STATUS, userStatus);
    }


    public void saveUserLevel(String userLevel) {
        ACache.get(FrameConst.getContext()).put(FrameConst.USER_LEVEL, userLevel);
    }


    public void saveUserToken(String userToken) {
        ACache.get(FrameConst.getContext()).put(FrameConst.USER_TOKEN, userToken);
    }


    public void saveUserName(String userName) {
        ACache.get(FrameConst.getContext()).put(FrameConst.USER_NAME, userName);
    }

    public void saveLongitude(String longitude) {
        ACache.get(FrameConst.getContext()).put(FrameConst.LONGITUDE, longitude);
    }

    public void saveCityName(String cityName) {
        ACache.get(FrameConst.getContext()).put(FrameConst.CITY_NAME, cityName);
    }

    public String getCityName() {
        return ACache.get(FrameConst.getContext()).getAsString(FrameConst.CITY_NAME);
    }

    public String getUserStatus() {
        return ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_STATUS);
    }

    public String getLongitude() {
        return GsonUtil.format(ACache.get(FrameConst.getContext()).getAsString(FrameConst.LONGITUDE));
    }

    public void saveLatitude(String latitude) {
        ACache.get(FrameConst.getContext()).put(FrameConst.LATITUDE, latitude);
    }

    public String getLatitude() {
        return GsonUtil.format(ACache.get(FrameConst.getContext()).getAsString(FrameConst.LATITUDE));
    }


    public static boolean checkToken() {
        return !TextUtils.isEmpty(ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_TOKEN));
    }

    public static boolean checkUser() {
        return !GsonUtil.format0(ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_ID)).equals("0");
    }

    public String getUserId() {

        return GsonUtil.isEmpty(ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_ID)) ? "0" : ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_ID);
    }

    public String getUserLevel() {

        return ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_LEVEL);
    }


    public String getUserName() {
        return ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_NAME);
    }

    public String getUserToken() {
        return ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_TOKEN);
    }


    public static String getUUid() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    public void logout() {
        ACache.get(FrameConst.getContext()).clear();
    }

    public boolean isLogin() {
        String userToken = ACache.get(FrameConst.getContext()).getAsString(FrameConst.USER_TOKEN);
        return !GsonUtil.isEmpty(userToken);
    }

    public void saveKeyValue(String key, String value) {
        ACache.get(FrameConst.getContext()).put(key, value);
    }

    public String getValue(String key) {
        return ACache.get(FrameConst.getContext()).getAsString(key);
    }

}
