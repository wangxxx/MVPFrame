package com.wangxing.code.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangxing.code.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LabelView extends ViewGroup implements View.OnClickListener {

    private List<LabelBean> mLabels = new ArrayList<>();

    private final List<List<View>> mLines = new ArrayList<>();
    private List<View> mLineViews = new ArrayList<>();

    private ArrayList<View> mSelectedViews = new ArrayList<>();
    private ArrayList<LabelBean> mSelectedLabels = new ArrayList<>();

    private int mLabelSpacing;
    private int mLineSpacing;
    private Drawable mBackgroundRes;

    private OnLabelSelectListener mOnLabelSelectListener;

    public LabelView(Context context) {
        super(context);
        init(null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
            mLabelSpacing = ta.getDimensionPixelSize(R.styleable.LabelView_label_spacing, 0);
            mLineSpacing = ta.getDimensionPixelSize(R.styleable.LabelView_line_spacing, 0);
            ta.recycle();
        }
        setLabels(null);
    }

    public void setLabels(List<LabelBean> labels) {
        setLabels(labels, null);
    }

    public void setLabels(List<LabelBean> labels, Set<String> selectedIds) {

        if (labels != null) {
            mLabels.clear();
            mLabels.addAll(labels);
        }

        if (!mLabels.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < mLabels.size(); i++) {
                LabelBean bean = mLabels.get(i);

                TextView labelView = (TextView) inflater.inflate(R.layout.item_label, this, false);
                if (null != mBackgroundRes) {
                    labelView.setBackground(mBackgroundRes);
                }
                labelView.setTag(i);
                labelView.setText(bean.getContent());
                labelView.setOnClickListener(this);
                if (selectedIds != null && selectedIds.contains(String.valueOf(bean.getId()))) {
                    labelView.setSelected(true);
                    mSelectedViews.add(labelView);
                    mSelectedLabels.add(bean);
                }
                addView(labelView);
            }
        } else {
            removeAllViews();
        }
    }

    public void setLabelBackground(Drawable backgroundDrawable) {
        this.mBackgroundRes = backgroundDrawable;
    }

    public ArrayList<View> getSelectedViews() {
        return mSelectedViews;
    }

    public ArrayList<LabelBean> getSelectedLabels() {
        return mSelectedLabels;
    }

    @Override
    public void onClick(View v) {
        if (mOnLabelSelectListener != null) {
            int position = (Integer) v.getTag();
            mOnLabelSelectListener.onLabelSelected(v, position);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mLines.clear();
        mLineViews.clear();

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int sizeHeight = MeasureSpec.getSize(widthMeasureSpec) - getPaddingTop() - getPaddingBottom();

        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int height = getPaddingTop() + getPaddingBottom();

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, sizeWidth, heightMeasureSpec, height);

            // 计算child start
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMode = MeasureSpec.AT_MOST;
            int childWidthSize = sizeWidth;

            int childHeightMode = MeasureSpec.AT_MOST;
            int childHeightSize = sizeHeight;

            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthMode = MeasureSpec.EXACTLY;
                childWidthSize -= lp.leftMargin + lp.rightMargin;
            } else if (lp.width >= 0) {
                childWidthMode = MeasureSpec.EXACTLY;
                childWidthSize = lp.width;
            }

            if (lp.height >= 0) {
                childHeightMode = MeasureSpec.EXACTLY;
                childHeightSize = lp.height;
            } else if (modeHeight == MeasureSpec.UNSPECIFIED) {
                childHeightMode = MeasureSpec.UNSPECIFIED;
                childHeightSize = 0;
            }

            child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                    MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
            // 计算child end

            int childWidth = child.getMeasuredWidth();
            lineHeight = child.getMeasuredHeight();

//            L.e(this + ">>> " + i);

            if (lineWidth + childWidth + mLabelSpacing < sizeWidth) { // 一行

                lineWidth += childWidth + mLabelSpacing;
                mLineViews.add(child);
            } else if (lineWidth + childWidth < sizeWidth) { // 一行最后一个

                lineWidth += childWidth;
                mLineViews.add(child);
            } else { // 换行

                lineWidth = 0;
                mLines.add(new ArrayList<>(mLineViews));
                mLineViews.clear();

                lineWidth += childWidth + mLabelSpacing;
                mLineViews.add(child);

//                L.e(this + ">>> line");
            }
        }
        if (!mLineViews.isEmpty()) {
            mLines.add(new ArrayList<>(mLineViews));
        }

        height += lineHeight * (mLines.size());
        height += mLineSpacing * (mLines.size());

        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int lineHeight = 0;
        for (int n = 0; n < mLines.size(); n++) {
            List<View> lineView = mLines.get(n);

            if (lineHeight <= 0) {
                lineHeight = lineView.get(0).getMeasuredHeight();
            }

            if (n == 0) {
                top = 0;
            } else {
                top += lineHeight + mLineSpacing;
            }
            bottom = top + lineHeight;

            for (int i = 0; i < lineView.size(); i++) {
                View view = lineView.get(i);

                if (i == 0) {
                    left = 0;
                } else {
                    left += lineView.get(i - 1).getMeasuredWidth() + mLabelSpacing;
                }
                right = left + view.getMeasuredWidth();

                view.layout(left, top, right, bottom);
            }
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setOnLabelSelectListener(OnLabelSelectListener listener) {
        mOnLabelSelectListener = listener;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }

    public interface OnLabelSelectListener {
        void onLabelSelected(View view, int position);
    }

    public static class LabelBean implements Parcelable {
        private String content;
        private int createBy;
        private String createDate;
        private String delFlg;
        private int id;
        private int modifyBy;
        private String modifyDate;
        private int proId;
        private String typeStatus;
        public LabelBean(){}

        public LabelBean(Parcel in) {
            content = in.readString();
            createBy = in.readInt();
            createDate = in.readString();
            delFlg = in.readString();
            id = in.readInt();
            modifyBy = in.readInt();
            modifyDate = in.readString();
            proId = in.readInt();
            typeStatus = in.readString();
        }

        public static final Creator<LabelBean> CREATOR = new Creator<LabelBean>() {
            @Override
            public LabelBean createFromParcel(Parcel in) {
                return new LabelBean(in);
            }

            @Override
            public LabelBean[] newArray(int size) {
                return new LabelBean[size];
            }
        };

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDelFlg() {
            return delFlg;
        }

        public void setDelFlg(String delFlg) {
            this.delFlg = delFlg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getModifyBy() {
            return modifyBy;
        }

        public void setModifyBy(int modifyBy) {
            this.modifyBy = modifyBy;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getProId() {
            return proId;
        }

        public void setProId(int proId) {
            this.proId = proId;
        }

        public String getTypeStatus() {
            return typeStatus;
        }

        public void setTypeStatus(String typeStatus) {
            this.typeStatus = typeStatus;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(content);
            dest.writeInt(createBy);
            dest.writeString(createDate);
            dest.writeString(delFlg);
            dest.writeInt(id);
            dest.writeInt(modifyBy);
            dest.writeString(modifyDate);
            dest.writeInt(proId);
            dest.writeString(typeStatus);
        }
    }
}
