package com.wangxing.code.http;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wangxing.code.FrameConst;
import com.wangxing.code.http.utils.LoggerInterceptor;
import com.wangxing.code.utils.NetWorkUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WangXing on 2017/12/13.
 */

public class ApiClient {

    //        public static final String BASE_URL = "http://192.168.1.129:8081";
//    public static final String BASE_URL = "http://39.108.80.101:8081";
//    public static final String BASE_URL = "http://192.168.1.115:8080";
//    public static final String BASE_URL = "http://192.168.1.190:8080";
//    public static final String BASE_URL = "http://192.168.1.141:8080";
//    public static final String BASE_URL = "http://192.168.1.118:8080";
    public static final String BASE_URL = "http://192.168.1.164:8080";
//    public static final String BASE_URL = "http://192.168.1.168:8080";
//    public static final String BASE_URL = "http://api.yahao.ren:8080";
//    public static final String BASE_URL = "http://112.126.72.82:8080";


    private static final int READ_TIME_OUT = 45;

    private static final int WRITE_TIME_OUT = 45;

    private static final int CONNECT_TIME_OUT = 15;

    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    public static final String CACHE_CONTROL_AGE = "max-age=5";

    private static final String HTTP_LOG = "YaYaPay";

    private static volatile ApiClient sInstance;

    private Retrofit mRetrofit;

    public static ApiClient getInstance() {
        if (sInstance == null) {
            synchronized (ApiClient.class) {
                if (sInstance == null) {
                    sInstance = new ApiClient();
                }
            }
        }
        return sInstance;
    }

    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    private ApiClient() {
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(FrameConst.getContext()));


        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor(HTTP_LOG, true))
                .cookieJar(cookieJar)
                .build();


        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    //根据网络设置离线、在线缓存Header
    public static String getCacheControl() {
        return NetWorkUtil.isNetConnected(FrameConst.getContext()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }

}



