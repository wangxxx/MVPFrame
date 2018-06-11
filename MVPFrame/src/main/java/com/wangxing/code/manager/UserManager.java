package com.wangxing.code.manager;

import android.content.Context;
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

    public void saveUserId(Context context, String userId) {
        ACache.get(context).put(FrameConst.USER_ID, userId);
    }

    public void saveUserStatus(Context context, String userStatus) {
        ACache.get(context).put(FrameConst.USER_STATUS, userStatus);
    }


    public void saveUserLevel(Context context, String userLevel) {
        ACache.get(context).put(FrameConst.USER_LEVEL, userLevel);
    }


    public void saveUserToken(Context context, String userToken) {
        ACache.get(context).put(FrameConst.USER_TOKEN, userToken);
    }


    public void saveUserName(Context context, String userName) {
        ACache.get(context).put(FrameConst.USER_NAME, userName);
    }

    public void saveLongitude(Context context, String longitude) {
        ACache.get(context).put(FrameConst.LONGITUDE, longitude);
    }

    public void saveCityName(Context context, String cityName) {
        ACache.get(context).put(FrameConst.CITY_NAME, cityName);
    }

    public String getCityName(Context context) {
        return ACache.get(context).getAsString(FrameConst.CITY_NAME);
    }

    public String getUserStatus(Context context) {
        return ACache.get(context).getAsString(FrameConst.USER_STATUS);
    }

    public String getLongitude(Context context) {
        return GsonUtil.format(ACache.get(context).getAsString(FrameConst.LONGITUDE));
    }

    public void saveLatitude(Context context, String latitude) {
        ACache.get(context).put(FrameConst.LATITUDE, latitude);
    }

    public String getLatitude(Context context) {
        return GsonUtil.format(ACache.get(context).getAsString(FrameConst.LATITUDE));
    }


    public static boolean checkToken(Context context) {
        return !TextUtils.isEmpty(ACache.get(context).getAsString(FrameConst.USER_TOKEN));
    }

    public static boolean checkUser(Context context) {
        return !GsonUtil.format0(ACache.get(context).getAsString(FrameConst.USER_ID)).equals("0");
    }

    public String getUserId(Context context) {

        return GsonUtil.isEmpty(ACache.get(context).getAsString(FrameConst.USER_ID)) ? "0" : ACache.get(context).getAsString(FrameConst.USER_ID);
    }

    public String getUserLevel(Context context) {

        return ACache.get(context).getAsString(FrameConst.USER_LEVEL);
    }


    public String getUserName(Context context) {
        return ACache.get(context).getAsString(FrameConst.USER_NAME);
    }

    public String getUserToken(Context context) {
        return ACache.get(context).getAsString(FrameConst.USER_TOKEN);
    }


    public static String getUUid() {

        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    public void logout(Context context) {
        ACache.get(context).clear();
    }

    public boolean isLogin(Context context) {
        String userToken = ACache.get(context).getAsString(FrameConst.USER_TOKEN);
        return !TextUtils.isEmpty(userToken);
    }
}
