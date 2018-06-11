package com.wangxing.code;

import android.annotation.SuppressLint;
import android.app.Application;

import com.wangxing.code.http.ApiClient;
import com.wangxing.code.utils.ImageMeasureUtil;

import wxc.android.logwriter.L;

import static com.wangxing.code.http.ApiClient.BASE_URL;

/**
 * Created by WangXing on 2017/8/31.
 */

public class FrameConst {
    @SuppressLint("StaticFieldLeak")
    private static Application sApp;
    private static ApiClient instance;


    public static Application getContext() {
        if (sApp == null) {
            throw new NullPointerException("Const需要初始化");
        }
        return sApp;
    }

    public static void init(Application application) {
        sApp = application;
        ImageMeasureUtil.init(application);
        instance = ApiClient.getInstance();
        L.Builder builder = new L.Builder();
        L.set(builder.addLogCat().addLocalLog(application.getApplicationContext()).logCrash(application.getApplicationContext()).create());

    }

    public static ApiClient getInstance() {
        return instance;
    }

    public static <T> T apiService(Class<T> clz) {
        return getInstance().createApi(clz);
    }

    public static final int PAGE_SIZE = 10;

    /**
     * OSS
     */
    public static final String OSS_TST = BASE_URL + "QianGuizhou/foreground/getTst";

    public static final String OSS_END_POINT = "http://oss-cn-shenzhen.aliyuncs.com";

    public static final String OSS_BUCKET = "qianshijie";

    public static final String OSS_SHOP_IMAGE_CALLBACK = "http://neufmer.iok.la/QianGuizhou/foreground/rShopImgCallback";
//    public static final String OSS_SHOP_IMAGE_CALLBACK = "http://qsje.tv/QianGuizhou/foreground/rShopImgCallback";

    public static final String OSS_ROOM_IMAGE_CALLBACK = "http://neufmer.iok.la/QianGuizhou/foreground/rRoomCategoryImgCallback";
//    public static final String OSS_ROOM_IMAGE_CALLBACK = "http://qsje.tv/QianGuizhou/foreground/rRoomCategoryImgCallback";

    /**
     * 用户信息
     */
    public static final String USER_ID = "userId";

    public static final String USER_NAME = "userName";

    public static final String USER_LEVEL = "userLevel";

    public static final String USER_TOKEN = "userToken";

    public static final String USER_STATUS = "userStatus";

    /**
     * 经纬度
     */
    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String CITY_NAME = "city";

    //微信支付
    public static final String WX_APP_ID = "wx68dd2359ac382326";


}
