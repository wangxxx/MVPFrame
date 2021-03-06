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
    private int resId = com.wangxing.code.R.layout.dialog;

    public interface OnDialogClickListener {
        void doConfirm();

        void doCancel();
    }

    public CommonDialog(Context context) {
        this(context, null, null, 0);
    }

    public CommonDialog(Context context, int newResId) {
        this(context, null, null, newResId);
    }

    public CommonDialog(Context context, String message, int newResId) {
        this(context, context.getString(com.wangxing.code.R.string.common_dialog_title), message, newResId);
    }

    public CommonDialog(Context context, String title, String message, int newResId) {
        super(context, com.wangxing.code.R.style.CommonDialog);
        mContext = context;
        mTitle = title;
        mMessage = message;
        if (newResId != 0) {
            resId = newResId;
        }
    }

    public CommonDialog(Context context, Integer drawable, String title, String message, int newResId) {
        super(context, com.wangxing.code.R.style.CommonDialog);
        mContext = context;
        mTitle = title;
        mMessage = message;
        mDrawable = drawable;
        if (newResId != 0) {
            resId = newResId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(resId, null);
        setContentView(view);

        mTitleTv = (TextView) view.findViewById(R.id.tv_dialog_title);
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTv.setText(mTitle);
        } else if (mTitleSpan != null) {
            mTitleTv.setText(mTitleSpan);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }

        mMessageTv = (TextView) view.findViewById(R.id.tv_dialog_message);

        if (!TextUtils.isEmpty(mMessage)) {
            updateMessage(mMessage);
        } else {
            mMessageTv.setVisibility(View.GONE);
        }

        if (mDrawable != null) {
//            mTitleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_del_product, 0, 0, 0);
        }

        view.findViewById(R.id.tv_cancel_dialog).setOnClickListener(this);
        view.findViewById(R.id.tv_sure_dialog).setOnClickListener(this);
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
        if (id == R.id.tv_sure_dialog) {
            mListener.doConfirm();
        } else if (id == R.id.tv_cancel_dialog) {
            mListener.doCancel();
        }
    }

}
