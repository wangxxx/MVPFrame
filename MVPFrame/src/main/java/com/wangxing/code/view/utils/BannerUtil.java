package com.wangxing.code.view.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wangxing.code.R;
import com.wangxing.code.utils.ImageMeasureUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.CubeOutTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangXing on 2017/9/9.
 */

public class BannerUtil {

    private static final int DELAY_TIME = 3000; // 3s
    private View mContentView;
    private Banner banner;

    private List<DataBean> mBanners;
    private OnLoadFinish mOnLoadFinish;

    public BannerUtil(View view) {
        initViews(view);
        initPlayAnimations();
        initBannerHeight();
    }

    public BannerUtil(View view, int[] measure) {
        initViews(view);
        initPlayAnimations();
        initBannerHeight(measure);
    }

    public BannerUtil(Activity activity) {
        initViews(activity);
        initPlayAnimations();
        initBannerHeight();
    }

    public BannerUtil(Activity activity, int[] measure) {
        initViews(activity);
        initPlayAnimations();
        initBannerHeight(measure);
    }

    // 初始化banner播放动画
    private void initPlayAnimations() {

        banner.setBannerAnimation(CubeOutTransformer.class);

    }

    private void initBannerHeight() {
        int[] measure = getBannerSize();
        //mFlipper.getLayoutParams().height = measure[1];
        mContentView.getLayoutParams().height = measure[1];
//        int height = f.getHeight();
        mContentView.requestLayout();
    }

    private void initBannerHeight(int[] measure) {
        //mFlipper.getLayoutParams().height = measure[1];
        mContentView.getLayoutParams().height = measure[1];
//        int height = f.getHeight();
        mContentView.requestLayout();
    }

    protected int[] getBannerSize() {
        return ImageMeasureUtil.getBannerMeasure();
    }


    // 初始化banner views
    private void initViews(View view) {
        mContentView = view.findViewById(R.id.fl_home_banner);
        banner = (Banner) view.findViewById(R.id.banner);
    }

    // 初始化banner views
    private void initViews(Activity activity) {
        mContentView = activity.findViewById(R.id.fl_home_banner);
        banner = (Banner) activity.findViewById(R.id.banner);
    }

    public void setBanner(List<DataBean> banners) {
        mBanners = banners;
        List<String> images = new ArrayList<>();

        if (mBanners != null && !mBanners.isEmpty()) {

            for (int i = 0; i < mBanners.size(); i++) {

                images.add(mBanners.get(i).getImg_url());
            }
            banner.setImages(images)
                    .setDelayTime(DELAY_TIME)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {

            mContentView.setVisibility(View.GONE);

        }

        if (mOnLoadFinish != null) {
            mOnLoadFinish.onLoadFinish();
        }

    }

    public void setOnLoadFinish(OnLoadFinish finish) {
        mOnLoadFinish = finish;
    }

    public void setBannerHeight(int[] bannerSize) {
        mContentView.getLayoutParams().height = bannerSize[1];
//        int height = f.getHeight();
        mContentView.requestLayout();
    }

    public void setBannerClickable(OnBannerListener listener) {
        banner.setOnBannerListener(listener);
    }

    public interface OnLoadFinish {

        void onLoadFinish();

    }


    public static class DataBean {
        private int createBy;
        private String createDate;
        private String delFlg;
        private String del_flg;
        private int id;
        private String img_url;
        private int modifyBy;
        private String modifyDate;
        private int object_id;
        private String object_url;
        private String title;
        private String type;

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDelFlg() {
            return delFlg;
        }

        public void setDelFlg(String delFlg) {
            this.delFlg = delFlg;
        }

        public String getDel_flg() {
            return del_flg;
        }

        public void setDel_flg(String del_flg) {
            this.del_flg = del_flg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getModifyBy() {
            return modifyBy;
        }

        public void setModifyBy(int modifyBy) {
            this.modifyBy = modifyBy;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getObject_id() {
            return object_id;
        }

        public void setObject_id(int object_id) {
            this.object_id = object_id;
        }

        public String getObject_url() {
            return object_url;
        }

        public void setObject_url(String object_url) {
            this.object_url = object_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(imageView);
        }


    }

}
