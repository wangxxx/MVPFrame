package com.wangxing.code;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;

import com.wangxing.code.glide.ImageLoader;
import com.wangxing.code.view.CommonLayout;

/**
 * Created by WangXing on 2018/6/11.
 */
public class App extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FrameConst.init(this);
        CommonLayout.setResources(R.drawable.audio_placeholder, 0);
        ImageLoader.init(ContextCompat.getDrawable(this, R.drawable.arrow_down));


    }
}
