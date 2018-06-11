package com.wangxing.code.view;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wangxing.code.R;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;


public class BottomBar extends TabLayout {

    public BottomBar(Context context) {
        super(context);
        init();
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSelectedTabIndicatorColor(Color.TRANSPARENT);
        setSelectedTabIndicatorHeight(0);
    }

    public void setBadgeViewCount(String badgeCount) {
        TabLayout.Tab tabAt = getTabAt(1);
        View customView = tabAt.getCustomView();
//        BGABadgeRadioButton badgeView = (BGABadgeRadioButton) customView.findViewById(R.id.bottom_bar_view);
//        BGABadgeRelativeLayout badgeView = (BGABadgeRelativeLayout) customView.findViewById(R.id.badge_layout);
        BGABadgeImageView icon = (BGABadgeImageView) customView.findViewById(R.id.bottom_bar_icon);
//        badgeView.setVisibility(VISIBLE);
        icon.showTextBadge(badgeCount);

    }


    public void setDragDismissDelegage(BGADragDismissDelegate delegate) {
        TabLayout.Tab tabAt = getTabAt(1);
        View customView = tabAt.getCustomView();
        BGABadgeImageView icon = (BGABadgeImageView) customView.findViewById(R.id.bottom_bar_icon);
        icon.setDragDismissDelegage(delegate);
    }

    public void hiddenBadge() {

        TabLayout.Tab tabAt = getTabAt(1);
        View customView = tabAt.getCustomView();
//        BGABadgeRelativeLayout badgeView = (BGABadgeRelativeLayout) customView.findViewById(R.id.badge_layout);
        BGABadgeImageView icon = (BGABadgeImageView) customView.findViewById(R.id.bottom_bar_icon);
        icon.hiddenBadge();
    }


    public void addItem(int resId, int textResId) {
        TabLayout.Tab tab = newTab();
        tab.setText(textResId);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_main_bottom_tab, this, false);
//        mTestBv.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#0000FF"));
//        mTestBv.getBadgeViewHelper().setBadgeBgColorInt(Color.parseColor("#00FF00"));
//        mTestBv.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF0000"));
        TextView title = (TextView) view.findViewById(R.id.bottom_bar_titles);
//        BGABadgeRadioButton itemView = (BGABadgeRadioButton) view.findViewById(R.id.bottom_bar_view);
        BGABadgeImageView icon = (BGABadgeImageView) view.findViewById(R.id.bottom_bar_icon);
//        itemView.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
        icon.setImageResource(resId);
        title.setText(textResId);
        tab.setCustomView(view);
        addTab(tab);
    }
}
