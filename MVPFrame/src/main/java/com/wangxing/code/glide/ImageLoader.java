package com.wangxing.code.glide;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wangxing.code.R;
import com.wangxing.code.utils.ImageMeasureUtil;
import com.wangxing.code.utils.ValueUtil;

public class ImageLoader {

    private static ImageLoader sInstance;
    private static Drawable mPlaceholder;

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public static void init(Drawable placeholder) {

        mPlaceholder = placeholder;
        if (mPlaceholder == null) {
            throw new NullPointerException("ImageLoader需要初始化");
        }

    }


    private ImageLoader() {
//        this.mContext = context;
    }

    // view不能设置tag
    public void loadWithRound(ImageView view, String uri, int round) {
        if (uri == null) return;
        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .transform(new CenterCropRoundCornerTransform(ValueUtil.dpToPx(view.getContext(), round)))
                )
                .into(view);
    }

    // view不能设置tag
    public void load(ImageView view, String uri) {
        if (uri == null) return;
//        L.e(uri);
        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(view);
    }

    // view不能设置tag
    public void loadWithOutCenterCrop(ImageView view, String uri) {
        if (uri == null) return;
//        L.e(uri);
        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(view);
    }


    public void loadUserBigAvatar(ImageView view, String uri) {
        if (uri == null) return;
        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                )
                .into(view);

    }

    public void loadShopBanner(ImageView view, String uri) {
        if (uri == null) return;

        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                )
                .into(view);

    }

    public void loadFitCenter(ImageView view, String uri) {
        if (uri == null) return;


        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(mPlaceholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .into(view);

    }

    public void loadWithoutSize(final ImageView view, final String uri) {
        if (uri == null) return;

//        L.e(uri);

        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.getLayoutParams().height = ValueUtil.dpToPx(view.getContext(), 100);
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.grey_ededed));
        view.requestLayout();


        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull final Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                if (view.getMeasuredWidth() <= 0) return;

                                float radio = 1.0f * resource.getIntrinsicWidth() / resource.getIntrinsicHeight();
                                int height = ImageMeasureUtil.getHeight(view.getMeasuredWidth(), radio);
                                view.getLayoutParams().height = height;
                                view.requestLayout();


                                view.setBackground(null);

                                view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        load(view, uri);
                                    }
                                }, 100);
                            }
                        });

                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                if (view.getMeasuredWidth() <= 0) return;

                                float radio = 1.0f * resource.getIntrinsicWidth() / resource.getIntrinsicHeight();
                                int height = ImageMeasureUtil.getHeight(view.getMeasuredWidth(), radio);
                                view.getLayoutParams().height = height;
                                view.requestLayout();


                                view.setBackground(null);

                                view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        load(view, uri);
                                    }
                                }, 100);
                            }
                        });
                    }
                });
    }

    public void loadWithoutPlaceholder(ImageView view, String uri) {

        if (uri == null) return;

        Glide.with(view.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                )
                .into(view);
    }

    public void clearMemoryCache(Context ctx) {
        // 必须在主线程上调用此方法
        Glide.get(ctx).clearMemory();
    }

}
