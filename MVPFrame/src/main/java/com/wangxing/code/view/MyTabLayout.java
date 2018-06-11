package com.wangxing.code.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangxing.code.R;

import java.lang.reflect.Field;

/**
 * Created by WangXing on 2018/3/23.
 */

public class MyTabLayout extends TabLayout {
    private static final int NO_VALID = -1;
    private int mTextSize;
    private boolean mTextSizeBaseWidth = false;

    public MyTabLayout(Context context) {
        this(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, R.style.Widget_Design_TabLayout);
        int tabTextAppearance = a.getResourceId(R.styleable.TabLayout_tabTextAppearance,
                R.style.TextAppearance_Design_Tab);

        a.recycle();
    }


    public  void setIndicator( int leftDip, int rightDip) {
        Class<?> tabLayout = getClass().getSuperclass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
