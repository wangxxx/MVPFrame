package com.wangxing.code.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangxing.code.rxevent.RxManager;
import com.wangxing.code.utils.TypeUtil;


/**
 * Created by WangXing on 2017/8/31.
 */

public abstract class BaseFragment<M extends BaseModelInterface, P extends BasePresenter> extends Fragment {
    protected View layout;

    public M mModel;

    public P mPresenter;

    public RxManager mRxManager;

    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layout == null) {
            layout = inflater.inflate(getLayoutResId(), container, false);
        }
        mRxManager = new RxManager();
        isFirstLoad = true;
        isPrepared = true;
        mModel = TypeUtil.getTypeObject(this, 0);
        mPresenter = TypeUtil.getTypeObject(this, 1);
        // 实例化Presenter中的Context
        if (mPresenter != null) {
            mPresenter.mContext = getActivity();
            mPresenter.mManager = this.mRxManager;
        }
        this.initPresenter();
        this.initArgumentsData();
        this.initToolBar();
        this.initView();
        lazyLoad();
        return layout;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    public void setDataInitiated(boolean init) {
        isFirstLoad = init;
    }

    public abstract int getLayoutResId();

    protected abstract void initPresenter();

    protected abstract void initArgumentsData();

    protected abstract void initToolBar();

    protected abstract void initView();

    protected abstract void initData();

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
