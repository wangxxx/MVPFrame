package com.wangxing.code.utils;

import android.content.Context;
import android.content.res.Resources;

public class ImageMeasureUtil {

    // banner
    public static final float BANNER_RATIO = 72 / 35f;
    //首页  banner
    public static final float MAIN_BANNER_SIZE = 750 / 468f;
    //商品详情 banner
    public static final float PRODUCT_DETAIL_BANNER_SIZE = 750/700f;


    private static int sScreenWidth;

    private static int dp24;

    // 视频宽高缩小倍数
    public static final int VIDEO_NARROW = 3;

    public static void init(Context ctx) {
        Resources resources = ctx.getResources();
        sScreenWidth = resources.getDisplayMetrics().widthPixels;
        dp24 = ValueUtil.dpToPx(ctx, 24);
    }

    public static int[] getBannerMeasure() {
        int[] measure = new int[2];
        measure[0] = sScreenWidth;
        measure[1] = Math.round(sScreenWidth / BANNER_RATIO);
        return measure;
    }

    public static int[] getMainHomeBannerMeasure() {
        int[] measure = new int[2];
        measure[0] = sScreenWidth;
        measure[1] = Math.round(sScreenWidth / MAIN_BANNER_SIZE);
        return measure;
    }

    public static int[] getProductDetailBannerMeasure() {
        int[] measure = new int[2];
        measure[0] = sScreenWidth;
        measure[1] = Math.round(sScreenWidth / PRODUCT_DETAIL_BANNER_SIZE);
        return measure;
    }

    public static int[] getVideoMeasure(float ratio) {
        int[] measure = new int[2];
        measure[0] = sScreenWidth;
        measure[1] = Math.round((sScreenWidth - dp24) / ratio);
        return measure;
    }


    /**
     * 获取视频宽高缩放
     */
    public static int getVideosWHRatio(int video) {
        return Math.round(video / VIDEO_NARROW);
    }

    public static int getHeight(int width, float radio) {
        return Math.round(width / radio);
    }
}
