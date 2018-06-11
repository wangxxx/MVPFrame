package com.wangxing.code.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxing.code.R;


public class ToastUtil {
    private static Toast sToast;
    private static TextView sMessageTv;

    private static void initToast(Context context) {
        if (sToast == null) {
            sToast = new Toast(context);

            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//            sMessageTv = V.f(view, android.R.id.message);
            sMessageTv = (TextView) view.findViewById(android.R.id.message);

            sToast.setView(view);
            sToast.setGravity(Gravity.CENTER, 0, 0);
        }
    }

    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int messageResId) {
        show(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int messageResId) {
        show(context, context.getString(messageResId), Toast.LENGTH_LONG);
    }

    private static void show(Context context, CharSequence message, int duration) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        initToast(context);
        sMessageTv.setText(message);
        sToast.setDuration(duration);
        sToast.show();
    }

//    public static void showStyle(Context context, int resid) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
//        view.findViewById(R.id.ll_toast).setBackgroundColor(Color.WHITE);
//        sMessageTv = (TextView) view.findViewById(android.R.id.message);
//        sMessageTv.setBackgroundResource(resid);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_LONG);
//        sToast.show();
//    }
//
//    public static void showImgStyle(Context context, String message) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        sMessageTv = (TextView) view.findViewById(R.id.tv_toast_msg);
//        sMessageTv.setText(message);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_LONG);
//        sToast.show();
//    }
//
//    public static void showPostSelectLabel(Context context) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_post_select_label, null);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_SHORT);
//        sToast.show();
//    }
//
//    public static void showNoShopPrivilege(Context context) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_post_select_label, null);
//        TextView content1 = (TextView) view.findViewById(R.id.tv_content1);
//        content1.setText(R.string.mine_shop_privilege_toast1);
////        TextView content2 = V.f(view, R.id.tv_content2);
//        TextView content2 = (TextView) view.findViewById(R.id.tv_content2);
//
//        content2.setText(R.string.mine_shop_privilege_toast2);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_SHORT);
//        sToast.show();
//    }
//
//    public static void showNotEnoughMoney(Context context) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_post_select_label, null);
//        TextView content1 = (TextView) view.findViewById(R.id.tv_content1);
//        content1.setText(R.string.mine_shop_privilege_toast1);
////        TextView content2 = V.f(view, R.id.tv_content2);
//        TextView content2 = (TextView) view.findViewById(R.id.tv_content2);
//        content2.setText(R.string.ecoer_detail_shop_not_enough_money);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_SHORT);
//        sToast.show();
//    }
//
//    public static void showIntegralStyle(Context context, String integral) {
//        if (sToast != null) {
//            sToast.cancel();
//            sToast = null;
//        }
//        sToast = new Toast(context);
//
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_integral, null);
//        view.findViewById(R.id.ll_toast).setBackgroundColor(ContextCompat.getColor(context, R.color.white_00ffffff));
//        sMessageTv = (TextView) view.findViewById(R.id.textView);
//        sMessageTv.setText(integral);
//        sToast.setView(view);
//        sToast.setGravity(Gravity.CENTER, 0, 0);
//        sToast.setDuration(Toast.LENGTH_SHORT);
//        sToast.show();
//    }
}
