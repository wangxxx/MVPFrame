package com.wangxing.code.view.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxing.code.R;


public class ToolbarUtil implements View.OnClickListener {

    private View mToolbarView;
    private TextView mLeftTv;
    private ImageView mLeftIv;
    private TextView mTitleTv;
    private TextView mRightTv;
    private ImageView mRightIv;
    private FrameLayout mLeftFl;
    private FrameLayout mRightFl;
    private Activity mActivity;

    public ToolbarUtil(Activity activity) {
        mActivity = activity;
        init(activity);
    }

    public ToolbarUtil(View parentView) {
        mActivity = (Activity) parentView.getContext();
        init(parentView);
    }

    private void init(Activity activity) {
        mToolbarView = mActivity.findViewById(com.wangxing.code.R.id.frame_toolbar);
        mLeftTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_left);
        mLeftIv = (ImageView) mToolbarView.findViewById(com.wangxing.code.R.id.ib_toolbar_left);
        mLeftFl = (FrameLayout) mToolbarView.findViewById(com.wangxing.code.R.id.fl_toolbar_left);
        mTitleTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_title);
        mRightTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_right);
        mRightIv = (ImageView) mToolbarView.findViewById(com.wangxing.code.R.id.ib_toolbar_right);
        mRightFl = (FrameLayout) mToolbarView.findViewById(com.wangxing.code.R.id.fl_toolbar_right);
        mLeftFl.setOnClickListener(this);
    }

    private void init(View parentView) {
        mToolbarView = parentView.findViewById(com.wangxing.code.R.id.toolbar);
        mLeftTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_left);
        mLeftIv = (ImageView) mToolbarView.findViewById(com.wangxing.code.R.id.ib_toolbar_left);
        mLeftFl = (FrameLayout) mToolbarView.findViewById(com.wangxing.code.R.id.fl_toolbar_left);
        mTitleTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_title);
        mRightTv = (TextView) mToolbarView.findViewById(com.wangxing.code.R.id.tv_toolbar_right);
        mRightIv = (ImageView) mToolbarView.findViewById(com.wangxing.code.R.id.ib_toolbar_right);
        mRightFl = (FrameLayout) mToolbarView.findViewById(com.wangxing.code.R.id.fl_toolbar_right);
        mLeftFl.setOnClickListener(this);
    }

    public void setToolbarVisibility(int visibility) {
        mToolbarView.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fl_toolbar_left) {
            if (mActivity != null) {
                mActivity.onBackPressed();
            }
        }
    }

    public void setBackgroundResource(int resId) {
        mToolbarView.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int color) {
        mToolbarView.setBackgroundColor(color);
    }

    public void setTitle(int resId) {
        mTitleTv.setText(resId);
    }

    public void setTitleLeft() {
        mTitleTv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setLeftText(String text) {
        mLeftTv.setText(text);
    }

    public void setRightText(String text) {
        mRightTv.setText(text);
        mRightTv.setVisibility(View.VISIBLE);
        mRightIv.setVisibility(View.INVISIBLE);
    }

    public void setLeftText(int resId) {
        mLeftTv.setText(resId);
        mLeftTv.setVisibility(View.VISIBLE);
        mLeftIv.setVisibility(View.INVISIBLE);
    }

    public void setLeftImage(int resId) {
        mLeftIv.setImageResource(resId);
        mLeftIv.setVisibility(View.VISIBLE);
        mLeftTv.setVisibility(View.INVISIBLE);
    }

    public void setRightText(int resId) {
        mRightTv.setText(resId);
        mRightTv.setVisibility(View.VISIBLE);
        mRightIv.setVisibility(View.INVISIBLE);
    }

    public void setRightTextColor(int resId) {
        mRightTv.setTextColor(resId);
    }

    public void setRightImage(int resId) {
        mRightIv.setImageResource(resId);
        mRightIv.setVisibility(View.VISIBLE);
        mRightTv.setVisibility(View.INVISIBLE);
    }

    public void setOnLeftClickListener(View.OnClickListener listener) {
        mLeftFl.setOnClickListener(listener);
    }

    public void setOnRightClickListener(View.OnClickListener listener) {
        mRightFl.setOnClickListener(listener);
    }

    public void setLightBackTheme(int resId) {
        setLightBackTheme(mLeftFl.getContext().getString(resId));
    }

    public void setLightBackTheme(String title) {
        setTitle(title);
        setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white_ffffff));
        setLeftImage(R.drawable.icon_back);
    }

    public void setTitleColor(int titleColor) {
        mTitleTv.setTextColor(titleColor);
    }

    public void setLeftVisibility(int visibility) {
        mLeftIv.setVisibility(visibility);
        mLeftFl.setEnabled(false);
    }

}
