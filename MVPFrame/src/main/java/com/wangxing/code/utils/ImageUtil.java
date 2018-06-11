package com.wangxing.code.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    public static String getFullPath(String path) {
        if (path != null) {
            if (path.startsWith("file://")) { // 由于软连接的问题, 带file://的可能会找不到文件
                return path.substring(7);
            } else if (path.startsWith("http://")) {
                return path;
            }
        }
        return path;
    }

//    public static String getSystemFullPath(String path) {
//        return getImageUrlPrefix() + path;
//    }

    private static String getImageUrlPrefix() {
        return "";
//        return ConfigUtils.sConfig != null ? ConfigUtils.sConfig.imgurl : "http://www.yahao.ren";
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    public static Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 在图片右下角添加水印
     *
     * @param srcBMP  原图
     * @param markBMP 水印图片
     * @return 合成水印后的图片
     */
    public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
        if (srcBMP == null) {
            return null;
        }

        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(srcBMP.getWidth(), srcBMP.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入原图
        cv.drawBitmap(srcBMP, 0, 0, null);
        // 在原图的右下角画入水印
        cv.drawBitmap(markBMP, srcBMP.getWidth() - markBMP.getWidth() + 5, srcBMP.getHeight() - markBMP.getHeight() + 5, null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();

        return newb;
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

}
