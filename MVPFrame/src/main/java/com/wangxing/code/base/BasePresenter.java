package com.wangxing.code.base;

import android.content.Context;

import com.wangxing.code.rxevent.RxManager;

/**
 * Created by WangXing on 2017/8/31.
 */

public abstract class BasePresenter<M, V> {

    public Context mContext;

    public M mModel;

    public V mView;

    public RxManager mManager;


    public void setVM(V view, M model) {
        this.mView = view;
        this.mModel = model;
        this.onStart();
    }

    public void onStart() {

    }

}
