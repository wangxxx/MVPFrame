package com.wangxing.code;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.wangxing.code.base.BaseActivity;
import com.wangxing.code.glide.ImageLoader;
import com.wangxing.code.view.CommonLayout;
import com.wangxing.code.view.LoadingDialog;
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

//    @Override
//    protected void doBeforeSetContentView() {
//        super.doBeforeSetContentView();
//        setStatusBarDarkMode();
//    }

    @Override
    protected void initToolBar() {
        ToolbarUtil toolbarUtil = new ToolbarUtil(this);
        toolbarUtil.setLineBackgroundColor(R.color.blue_2c80ba);
        toolbarUtil.setRightText("哈哈哈");
        toolbarUtil.setLightBackTheme("测试");
        toolbarUtil.setRightTextCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.arrow_down), ToolbarUtil.LEFT);
        toolbarUtil.setTitleStyle(Typeface.DEFAULT_BOLD, Typeface.BOLD);

    }

    @Override
    protected void initView() {
        ImageView viewById = findViewById(R.id.image);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showDialogForLoading(MainActivity.this, "测试", true);
            }
        });
        ImageLoader.getInstance().load(viewById, "");
        CommonLayout commonLayout = findViewById(R.id.common_layout);
//        commonLayout.showEmpty();
        commonLayout.showLoading(R.string.picture_error);
//        commonLayout.showError(R.string.picture_error);
//        commonLayout.showError(getString(R.string.picture_error));

    }

    public void click(View view) {


    }


}
