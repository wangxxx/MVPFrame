package com.wangxing.code.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private ImageView mEmptyImage;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private TextView mErrorTv;
    private static int mEmptyImageId = R.drawable.img_empty_data;
    private static int mLoadingStyleId = R.drawable.loading_dialog_progressbar;

    public static void setResources(int eId, int lId) {
        if (eId != 0) {
            mEmptyImageId = eId;
        }
        if (lId != 0) {
            mLoadingStyleId = lId;
        }
    }

    public static int getLoadingId() {
        return mLoadingStyleId;
    }

    public static CommonLayout create(Context context, int layoutId) {
        CommonLayout layout = (CommonLayout) LayoutInflater.from(context).inflate(com.wangxing.code.R.layout.common_layout, null);
        View view = LayoutInflater.from(context).inflate(layoutId, layout, false);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }

    public static CommonLayout create(Context context, View view) {
        CommonLayout layout = (CommonLayout) LayoutInflater.from(context).inflate(com.wangxing.code.R.layout.common_layout, null);
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
            int viewId = getContext().getResources().getIdentifier("common_content", "id", getContext().getPackageName());
            mContentView = findViewById(viewId);
        }
    }

    private void init() {
        if (mLoadingStub == null) {
            mLoadingStub = new ViewStub(getContext(), com.wangxing.code.R.layout.common_loading);
            mErrorStub = new ViewStub(getContext(), com.wangxing.code.R.layout.common_error);
            mEmptyStub = new ViewStub(getContext(), com.wangxing.code.R.layout.common_empty);
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
        mProgressText.setVisibility(GONE);
    }

    public void showLoading(int progressString) {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(VISIBLE);
        mProgressText.setVisibility(VISIBLE);
        mProgressText.setText(progressString);
    }

    public void showLoading(String progressString) {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(VISIBLE);
        mProgressText.setVisibility(VISIBLE);
        mProgressText.setText(progressString);
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

    public void showError(int errorText) {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(VISIBLE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
        mErrorTv.setText(errorText);
    }

    public void showError(String errorText) {
        mContentView.setVisibility(GONE);
        setErrorViewVisible(VISIBLE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
        mErrorTv.setText(errorText);
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
            mEmptyImage = mEmptyView.findViewById(R.id.iv_empty);
            mEmptyImage.setImageResource(mEmptyImageId);
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private void setErrorViewVisible(int visible) {
        if (mErrorView != null) {
            mErrorView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mErrorView = mErrorStub.inflate();
            mErrorView.setVisibility(VISIBLE);

            mErrorTv = (TextView) mErrorView.findViewById(com.wangxing.code.R.id.tv_common_error);
            mErrorTv.setOnClickListener(mOnErrorClickListener);
        }
    }

    private void setLoadingViewVisible(int visible) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mLoadingView = mLoadingStub.inflate();
            mProgressBar = mLoadingView.findViewById(R.id.progress_Bar);
            mProgressText = mLoadingView.findViewById(R.id.progress_text);
            mProgressBar.setIndeterminateDrawable(ContextCompat.getDrawable(getContext(), mLoadingStyleId));
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    public void setOnErrorClickListener(OnClickListener listener) {
        mOnErrorClickListener = listener;
    }


    public void setEmptyImage(int imageId) {
        mEmptyImage.setImageResource(imageId);
    }

}
