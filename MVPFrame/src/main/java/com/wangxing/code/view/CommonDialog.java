package com.wangxing.code.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wangxing.code.R;


public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnDialogClickListener mListener;
    private String mTitle;
    private String mMessage;
    private SpannableString mTitleSpan;
    private TextView mTitleTv;
    private TextView mMessageTv;
    private Integer mDrawable;

    public interface OnDialogClickListener {
        void doConfirm();

        void doCancel();
    }

    public CommonDialog(Context context) {
        this(context, null);
    }

    public CommonDialog(Context context, String message) {
        this(context, context.getString(com.wangxing.code.R.string.common_dialog_title), message);
    }

    public CommonDialog(Context context, String title, String message) {
        super(context, com.wangxing.code.R.style.CommonDialog);
        mContext = context;
        mTitle = title;
        mMessage = message;
    }

    public CommonDialog(Context context, Integer drawable, String title, String message) {
        super(context, com.wangxing.code.R.style.CommonDialog);
        mContext = context;
        mTitle = title;
        mMessage = message;
        mDrawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(com.wangxing.code.R.layout.dialog, null);
        setContentView(view);

        mTitleTv = (TextView) view.findViewById(com.wangxing.code.R.id.tv_dialog_title);
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTv.setText(mTitle);
        } else if (mTitleSpan != null) {
            mTitleTv.setText(mTitleSpan);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }

        mMessageTv = (TextView) view.findViewById(com.wangxing.code.R.id.tv_dialog_message);

        if (!TextUtils.isEmpty(mMessage)) {
            updateMessage(mMessage);
        } else {
            mMessageTv.setVisibility(View.GONE);
        }

        if (mDrawable != null) {
//            mTitleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_del_product, 0, 0, 0);
        }

        view.findViewById(com.wangxing.code.R.id.tv_cancel_dialog).setOnClickListener(this);
        view.findViewById(com.wangxing.code.R.id.tv_sure_dialog).setOnClickListener(this);
    }

    public void updateMessage(String message) {
        mMessageTv.setVisibility(View.VISIBLE);
        mMessageTv.setText(message);
    }

    public void setTitle(SpannableString title) {
        mTitleSpan = title;
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    public void setOnClickListener(OnDialogClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.wangxing.code.R.id.tv_sure_dialog) {
            mListener.doConfirm();
        } else if (id == com.wangxing.code.R.id.tv_cancel_dialog) {
            mListener.doCancel();
        }
    }

}
