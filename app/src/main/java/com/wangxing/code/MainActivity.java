package com.wangxing.code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.luck.picture.lib.config.PictureConfig;
import com.wangxing.code.base.BaseActivity;
import com.wangxing.code.utils.SelectPictureUtil;
import com.wangxing.code.utils.ToastUtil;
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
    }

    @Override
    protected void initView() {

    }

    public void click(View view) {



    }


}
