package com.wangxing.code.http;

import android.app.Activity;
import android.content.Context;

import com.wangxing.code.FrameConst;
import com.wangxing.code.http.utils.ApiResult;
import com.wangxing.code.http.utils.ServerException;
import com.wangxing.code.utils.NetWorkUtil;
import com.wangxing.code.utils.ToastUtil;
import com.wangxing.code.view.LoadingDialog;

import rx.Subscriber;

/**
 * des:订阅封装
 */
public abstract class ApiCallBack<T> extends Subscriber<ApiResult<T>> {

    private Context mContext;
    private String msg;
    private boolean mShowDialog;

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    public ApiCallBack(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.mShowDialog = showDialog;
    }

    public ApiCallBack(Context context) {
        this(context, context.getString(com.wangxing.code.R.string.call_back_loading), false);
    }

    public ApiCallBack(Context context, boolean showDialog) {
        this(context, context.getString(com.wangxing.code.R.string.call_back_loading), showDialog);
    }

    @Override
    public void onCompleted() {
        LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!NetWorkUtil.isNetConnected(FrameConst.getContext())) {
            ToastUtil.showShort(mContext, mContext.getString(com.wangxing.code.R.string.call_back_no_network));
            _onError(new ServerException(ServerException.ERROR_EXCEPTION, mContext.getString(com.wangxing.code.R.string.call_back_no_network_1)));
            onCompleted();
            return;
        }

        if (mShowDialog) {
            try {
                LoadingDialog.showDialogForLoading((Activity) mContext, msg, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onNext(ApiResult<T> t) {

        if (t.isOk()) {
            _onNext(t.data, t.message);
        } else {
            _onError(new ServerException(t.code, t.message));
        }

    }

    @Override
    public void onError(Throwable e) {

        if (mShowDialog)
            LoadingDialog.cancelDialogForLoading();
        e.printStackTrace();
        //网络
        if (!NetWorkUtil.isNetConnected(FrameConst.getContext())) {
            ToastUtil.showShort(mContext, mContext.getString(com.wangxing.code.R.string.call_back_no_network_1));
            _onError(new ServerException(ServerException.ERROR_EXCEPTION, mContext.getString(com.wangxing.code.R.string.call_back_no_network_1)));
        } else if (e instanceof ServerException) {
            ToastUtil.showShort(mContext, ((ServerException) e).mErrorMsg);
            _onError((ServerException) e);
        } else {
            ToastUtil.showShort(mContext, e.toString());
            _onError(new ServerException(ServerException.ERROR_EXCEPTION, e.toString()));
        }
    }

    protected abstract void _onNext(T t, String message);

    protected abstract void _onError(ServerException exception);

}
