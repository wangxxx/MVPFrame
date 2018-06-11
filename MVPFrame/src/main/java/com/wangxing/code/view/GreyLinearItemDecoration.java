package com.wangxing.code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wangxing.code.R;
import com.wangxing.code.utils.ValueUtil;


public class GreyLinearItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mDividerWidth = 1;
    private int mMargin;

    public GreyLinearItemDecoration(Context context) {
        this(context, 1);
    }

    public GreyLinearItemDecoration(Context context, int dividerWidth) {
        this(context, dividerWidth, 0);
    }

    public GreyLinearItemDecoration(Context context, int dividerWidth, int marigin) {
        mDividerWidth = ValueUtil.dpToPx(context, dividerWidth);
        mMargin = ValueUtil.dpToPx(context, marigin);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, com.wangxing.code.R.color.grey_ededed));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mDividerWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerWidth;
            c.drawRect(left + mMargin, top, right - mMargin, bottom, mPaint);
        }
    }
}
