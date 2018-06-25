package com.wangxing.code.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.wangxing.code.R;
import com.wangxing.code.manager.AppForegroundStateManager;
import com.wangxing.code.rxevent.RxManager;
import com.wangxing.code.utils.ToastUtil;
import com.wangxing.code.utils.TypeUtil;
import com.wangxing.code.view.CommonLayout;

import java.util.ArrayList;
import java.util.List;

import wxc.android.logwriter.L;

import static com.wangxing.code.utils.KeyboardUtil.hiddenKeyboard;


/**
 * Created by WangXing on 2017/8/31.
 */

public abstract class BaseActivity<M extends BaseModelInterface,
        P extends BasePresenter>
        extends AppCompatActivity {


    public M mModel;

    public P mPresenter;

    public Context mContext;

    public RxManager mRxManager;

    private static List<BaseActivity> sActivities = new ArrayList<>();

    private boolean mDestroyed;

    private long mExitTimestamp;

    public static void finishActivities() {
        for (BaseActivity activity : sActivities) {
            activity.finish();
        }
        sActivities.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sActivities.add(this);
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
        // 在设置内容View之前调用
        doBeforeSetContentView();
        // 设置布局
        setContentView(getLayoutResId());
        mContext = this;
        // 实例化Model
        mModel = TypeUtil.getTypeObject(this, 0);
        // 实例化Presenter
        mPresenter = TypeUtil.getTypeObject(this, 1);
        // 实例化Presenter中的Context
        if (mPresenter != null) {
            mPresenter.mContext = this;
            mPresenter.mManager = this.mRxManager;
        }
        this.initIntentData();
        this.initPresenter();
        this.initView();
        this.initToolBar();
        L.e("当前Activity为------------->" + getClass().getCanonicalName());

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppForegroundStateManager.getInstance().onActivityVisible(this);
    }

    @Override
    protected void onStop() {
        AppForegroundStateManager.getInstance().onActivityNotVisible(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        mDestroyed = true;
        super.onDestroy();
        mRxManager.clear();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            hiddenKeyboard(event, this);

        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isDestroyed() {
        return mDestroyed || isFinishing();
    }

    public static boolean checkActivity(Activity activity) {
        if (activity == null || activity.isFinishing()) return false;

        if (activity instanceof BaseActivity) {
            return !((BaseActivity) activity).isDestroyed();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (sActivities.size() == 1) {
            if (System.currentTimeMillis() - mExitTimestamp > 3000) {
                mExitTimestamp = System.currentTimeMillis();

                ToastUtil.showShort(this, R.string.common_exit_app);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public abstract int getLayoutResId();

    protected abstract void initPresenter();

    protected abstract void initIntentData();

    protected abstract void initToolBar();

    protected abstract void initView();

    protected void doBeforeSetContentView() {
    }

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
