package com.wangxing.code.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wangxing.code.R;


/**
 * description:弹窗浮动加载进度条
 * Created by xsf
 * on 2016.07.17:22
 */
public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Context context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(com.wangxing.code.R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(com.wangxing.code.R.id.id_tv_loading_dialog_text);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.callback_progress);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context, CommonLayout.getLoadingId()));
        loadingText.setText(msg);
        mLoadingDialog = new Dialog(context, com.wangxing.code.R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }


    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading() {
        if (mLoadingDialog != null) {
//            mLoadingDialog.cancel();
            mLoadingDialog.dismiss();
        }
    }
}
