package com.wangxing.code.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wangxing.code.R;
import com.wangxing.code.manager.AppForegroundStateManager;
import com.wangxing.code.rxevent.RxManager;
import com.wangxing.code.utils.ToastUtil;
import com.wangxing.code.utils.TypeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    private static boolean isMiUi = false;

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
        this.setStatusBarDarkMode();

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

    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private void setMIUIStatusBarDarkMode() {
        if (isMiUi) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 静态域，获取系统版本是否基于MIUI
     */

    static {
        try {
            @SuppressLint("PrivateApi") Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            isMiUi = version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMeiZuDarkMode(Window window, boolean dark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 24) {
            return false;
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @SuppressLint("InlinedApi")
    private int getStatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUi) {
                result = 1;
            } else if (setMeiZuDarkMode(getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    @SuppressLint("InlinedApi")
    protected void setStatusBarDarkMode() {
        int type = getStatusBarLightMode();
        if (type == 1) {
            setMIUIStatusBarDarkMode();
        } else if (type == 2) {
            setMeiZuDarkMode(getWindow(), true);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
