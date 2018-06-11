package com.wangxing.code.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wangxing.code.FrameConst;
import com.wangxing.code.R;


// 内容布局ID必须为common_content
public class CommonLayout extends FrameLayout {

    private ViewStub mLoadingStub;
    private View mLoadingView;
    private ViewStub mErrorStub;
    private View mErrorView;
    private ViewStub mEmptyStub;
    private View mEmptyView;
    private View mContentView;
    private OnClickListener mOnErrorClickListener;

    public static CommonLayout create(Context context, int layoutId) {
        CommonLayout layout = (CommonLayout) LayoutInflater.from(context).inflate(R.layout.common_layout, null);
        View view = LayoutInflater.from(context).inflate(layoutId, layout, false);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }

    public static CommonLayout create(Context context, View view) {
        CommonLayout layout = (CommonLayout) LayoutInflater.from(context).inflate(R.layout.common_layout, null);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }

    public CommonLayout(Context context) {
        super(context);
        init();
    }

    public CommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mContentView == null) {
            int viewId = FrameConst.getContext().getResources().getIdentifier("common_content", "id", this.getContext().getPackageName());
            mContentView = findViewById(viewId);
        }
    }

    private void init() {
        if (mLoadingStub == null) {
            mLoadingStub = new ViewStub(getContext(), R.layout.common_loading);
            mErrorStub = new ViewStub(getContext(), R.layout.common_error);
            mEmptyStub = new ViewStub(getContext(), R.layout.common_empty);

            addView(mLoadingStub);
            addView(mErrorStub);
            addView(mEmptyStub);
        }
    }

    public void showLoading() {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(VISIBLE);
    }

    public void showEmpty() {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(VISIBLE);
        setLoadingViewVisible(GONE);
    }

    public void showError() {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(VISIBLE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
    }

    public void showContent() {
        mContentView.setVisibility(VISIBLE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
    }

    private void setEmptyViewVisible(int visible) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mEmptyView = mEmptyStub.inflate();
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private void setErrorViewVisible(int visible) {
        if (mErrorView != null) {
            mErrorView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mErrorView = mErrorStub.inflate();
            mErrorView.setVisibility(VISIBLE);

            TextView errorTv = (TextView) mErrorView.findViewById(R.id.tv_common_error);
            errorTv.setOnClickListener(mOnErrorClickListener);
        }
    }

    private void setLoadingViewVisible(int visible) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mLoadingView = mLoadingStub.inflate();
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    public void setOnErrorClickListener(OnClickListener listener) {
        mOnErrorClickListener = listener;
    }
}
