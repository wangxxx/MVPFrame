package com.wangxing.code.manager;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by WangXing on 2017/9/19.
 * 判断前台后台
 */

public class AppForegroundStateManager {
    private static final String TAG = AppForegroundStateManager.class.getSimpleName();
    private static final int MESSAGE_NOTIFY_LISTENERS = 1;
    public static final long APP_CLOSED_VALIDATION_TIME_IN_MS = 3 * DateUtils.SECOND_IN_MILLIS; // 30 Seconds
    private Reference<Activity> mForegroundActivity;
    private Set<OnAppForegroundStateChangeListener> mListeners = new HashSet<>();
    private AppForegroundState mAppForegroundState = AppForegroundState.NOT_IN_FOREGROUND;
    private NotifyListenersHandler mHandler;

    // 获得一个线程安全的类实例
    private static class SingletonHolder {
        public static final AppForegroundStateManager INSTANCE = new AppForegroundStateManager();
    }

    public static AppForegroundStateManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AppForegroundStateManager() {
        // 在主线程创建一个 handler
        mHandler = new NotifyListenersHandler(Looper.getMainLooper());
    }

    public enum AppForegroundState {
        IN_FOREGROUND,
        NOT_IN_FOREGROUND
    }

    public interface OnAppForegroundStateChangeListener {
        /**
         * 当应用状态发生改变时这个方法被调用（隐藏到后台或显示到前台）
         */
        public void onAppForegroundStateChange(AppForegroundState newState);
    }

    /**
     * 当 Activity 可见时应该调用这个方法
     */
    public void onActivityVisible(Activity activity) {
        if (mForegroundActivity != null) mForegroundActivity.clear();
        mForegroundActivity = new WeakReference<>(activity);
        determineAppForegroundState();
    }

    /**
     * 当 Activity 不再可见时应该调用这个方法
     */
    public void onActivityNotVisible(Activity activity) {
        /*
         * 前台 Activity 可能会被一个新的 Activity 替换。
         * 如果新 Activity 与前台 Activity 匹配，仅仅清除前台 Activity
         */
        if (mForegroundActivity != null) {
            Activity ref = mForegroundActivity.get();

            if (activity == ref) {
                // This is the activity that is going away, clear the reference
                mForegroundActivity.clear();
                mForegroundActivity = null;
            }
        }

        determineAppForegroundState();
    }

    /**
     * 用于判断应用是否处于前台
     */
    public Boolean isAppInForeground() {
        return mAppForegroundState == AppForegroundState.IN_FOREGROUND;
    }

    /**
     * 用于判断当前状态，更新追踪的目标，并通知所有观察者状态是否发生了改变
     */
    private void determineAppForegroundState() {
        /* 获取当前状态 */
        AppForegroundState oldState = mAppForegroundState;

        /* 决定当前状态 */
        final boolean isInForeground = mForegroundActivity != null && mForegroundActivity.get() != null;
        mAppForegroundState = isInForeground ? AppForegroundState.IN_FOREGROUND : AppForegroundState.NOT_IN_FOREGROUND;

        /* 如果新的状态与之前的状态不一样，则之前的状态需要通知所有观察者状态发生了改变 */
        if (mAppForegroundState != oldState) {
            validateThenNotifyListeners();
        }
    }

    /**
     * 添加一个用于监听前台应用状态的监听器
     *
     * @param listener
     */
    public void addListener(@NonNull OnAppForegroundStateChangeListener listener) {
        mListeners.add(listener);
    }

    /**
     * 移除用于监听前台应用状态的监听器
     *
     * @param listener
     */
    public void removeListener(OnAppForegroundStateChangeListener listener) {
        mListeners.remove(listener);
    }

    /**
     * 通知所有监听器前台应用状态发生了改变
     */
    private void notifyListeners(AppForegroundState newState) {
        android.util.Log.i(TAG, "Notifying subscribers that app just entered state: " + newState);

        for (OnAppForegroundStateChangeListener listener : mListeners) {
            listener.onAppForegroundStateChange(newState);
        }
    }

    /**
     * 这个方法会通知所有观察者：前台应用的状态发生了改变
     * <br><br>
     * 我们只在应用进入/离开前台时立刻监听器。当打开/关闭/方向切换这些操作频繁发生时，我们
     * 简要的传递一个一定会被无视的 NOT_IN_FOREGROUND 值。为了实现它，当我们注意到状态发
     * 生改变，一个延迟的消息会被发出。在这个消息被接收之前，我们不会注意前台应用的状态是否
     * 发生了改变。如果在消息被延迟的那段时间内应用的状态发生了改变，那么该通知将会被取消。
     */
    private void validateThenNotifyListeners() {
        // If the app has any pending notifications then throw out the event as the state change has failed validation
        if (mHandler.hasMessages(MESSAGE_NOTIFY_LISTENERS)) {
            android.util.Log.v(TAG, "Validation Failed: Throwing out app foreground state change notification");
            mHandler.removeMessages(MESSAGE_NOTIFY_LISTENERS);
        } else {
            if (mAppForegroundState == AppForegroundState.IN_FOREGROUND) {
                // If the app entered the foreground then notify listeners right away; there is no validation time for this
                mHandler.sendEmptyMessage(MESSAGE_NOTIFY_LISTENERS);
            } else {
                // We need to validate that the app entered the background. A delay is used to allow for time when the application went into the
                // background but we do not want to consider the app being backgrounded such as for in app purchasing flow and full screen ads.
                mHandler.sendEmptyMessageDelayed(MESSAGE_NOTIFY_LISTENERS, APP_CLOSED_VALIDATION_TIME_IN_MS);
            }
        }
    }

    private class NotifyListenersHandler extends Handler {
        private NotifyListenersHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                // 解码完成
                case MESSAGE_NOTIFY_LISTENERS:
                    /* 通知所有观察者状态发生了改变 */
                    android.util.Log.v(TAG, "App just changed foreground state to: " + mAppForegroundState);
                    notifyListeners(mAppForegroundState);
                    break;
                default:
                    super.handleMessage(inputMessage);
            }
        }
    }
}
