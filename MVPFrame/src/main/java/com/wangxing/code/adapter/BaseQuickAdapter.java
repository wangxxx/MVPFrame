package com.wangxing.code.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.animation.Interpolator;

import com.zhouyou.recyclerview.adapter.AnimationType;
import com.zhouyou.recyclerview.adapter.AnimationUtil;
import com.zhouyou.recyclerview.adapter.BH;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewAdapter;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewAnimAdapter;

import java.util.List;

/**
 * Created by WangXing on 2018/6/5.
 */
public abstract class BaseQuickAdapter<T> extends HelperRecyclerViewAdapter<T> {


    private AnimationType mAnimationType;
    private int mAnimationDuration = 300;
    private boolean showItemAnimationEveryTime = false;
    private Interpolator mItemAnimationInterpolator;
    private HelperRecyclerViewAnimAdapter.CustomAnimator mCustomAnimator;
    private int mLastItemPosition = -1;

    public BaseQuickAdapter(List data, Context context, int... layoutId) {
        super(data, context, layoutId);
    }

    public BaseQuickAdapter(Context context, int... layoutIds) {
        super(context, layoutIds);
    }

    public BaseQuickAdapter(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecated")
    public BaseQuickAdapter(List data, Context context) {
        super(data, context);
    }


//    @Override
//    public void onBindViewHolder(BH holder, int position, List<Object> payloads) {
//        addAnimation(holder);
//        super.onBindViewHolder(holder, position, payloads);
//    }

    @Override
    public void onBindViewHolder(BH holder, int position) {
        super.onBindViewHolder(holder, position);
        addAnimation(holder);
    }

    protected final void addAnimation(final BH holder) {
        int currentPosition = holder.getAdapterPosition();
        if (null != mCustomAnimator) {
            mCustomAnimator.getAnimator(holder.itemView).setDuration(mAnimationDuration).start();
        } else if (null != mAnimationType) {
            if (showItemAnimationEveryTime || currentPosition > mLastItemPosition) {
                new AnimationUtil()
                        .setAnimationType(mAnimationType)
                        .setTargetView(holder.itemView)
                        .setDuration(mAnimationDuration)
                        .setInterpolator(mItemAnimationInterpolator)
                        .start();
                mLastItemPosition = currentPosition;
            }
        }
    }

    /**
     * Animation api
     */
    public void setItemAnimation(AnimationType animationType) {
        mAnimationType = animationType;
    }

    public void setItemAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setItemAnimationInterpolator(Interpolator animationInterpolator) {
        mItemAnimationInterpolator = animationInterpolator;
    }

    public void setShowItemAnimationEveryTime(boolean showItemAnimationEveryTime) {
        this.showItemAnimationEveryTime = showItemAnimationEveryTime;
    }

    public void setCustomItemAnimator(HelperRecyclerViewAnimAdapter.CustomAnimator customAnimator) {
        mCustomAnimator = customAnimator;
    }

    public interface CustomAnimator {
        Animator getAnimator(View itemView);
    }

    public void addData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            mList.addAll(data);
            notifyDataSetChanged();
        }
    }

}
