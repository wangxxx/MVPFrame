package com.wangxing.code.http;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.wangxing.code.FrameConst;
import com.wangxing.code.http.utils.ApiResult;
import com.wangxing.code.http.utils.ServerException;
import com.wangxing.code.utils.NetWorkUtil;
import com.wangxing.code.utils.ToastUtil;
import com.wangxing.code.view.LoadingDialog;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
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
        this(context, "Loading...", false);
    }

    public ApiCallBack(Context context, boolean showDialog) {
        this(context, "Loading...", showDialog);
    }

    @Override
    public void onCompleted() {
        LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!NetWorkUtil.isNetConnected(FrameConst.getContext())) {
            ToastUtil.showShort(mContext, "当前网络不可用，请检查网络情况");
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
            ToastUtil.showShort(mContext, "网络开小差了~~~");
        }
        //服务器
        else if (e instanceof ServerException) {
            _onError((ServerException) e);
        }
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    _onError(new ServerException(String.valueOf(httpException.code()), "网络请求错误"));
                    ToastUtil.showShort(mContext, "网络请求错误");
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            _onError(new ServerException(ServerException.ERROR_NO_DATA, "数据解析异常"));
            ToastUtil.showShort(mContext, "数据解析异常");
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException) {
            _onError(new ServerException(ServerException.ERROR_NO_DATA, "网络连接错误"));
            ToastUtil.showShort(mContext, "网络连接错误");
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
//            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
//            ex.message = "证书验证失败";
        }//其它
        else {
            _onError(new ServerException(ServerException.ERROR_NO_DATA, "状态异常"));
            ToastUtil.showShort(mContext, "状态异常");
        }
    }

    protected abstract void _onNext(T t, String message);

    protected abstract void _onError(ServerException exception);

}
