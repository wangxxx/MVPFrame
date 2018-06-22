package com.wangxing.code;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.wangxing.code.base.BaseActivity;
import com.wangxing.code.glide.ImageLoader;
import com.wangxing.code.view.utils.ToolbarUtil;

public class MainActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initToolBar() {
        ToolbarUtil toolbarUtil = new ToolbarUtil(this);
        toolbarUtil.setLineBackgroundColor(R.color.blue_2c80ba);
        toolbarUtil.setRightText("哈哈哈");
        toolbarUtil.setRightTextCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.arrow_down), ToolbarUtil.LEFT);
    }

    @Override
    protected void initView() {
        ImageView viewById = findViewById(R.id.image);
        ImageLoader.getInstance().load(viewById, "");
    }

    public void click(View view) {


    }


}
