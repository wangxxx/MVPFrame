package com.wangxing.code.adapter.load;


import com.wangxing.code.adapter.BaseQuickAdapter;
import com.wangxing.code.adapter.LoadingMoreFooter;
import com.wangxing.code.base.BasePresenter;
import com.wangxing.code.http.ApiCallBack;
import com.wangxing.code.http.utils.ServerException;
import com.wangxing.code.view.CommonLayout;
import com.zhouyou.recyclerview.XRecyclerView;
import com.zhouyou.recyclerview.refresh.BaseRefreshHeader;
import com.zhouyou.recyclerview.refresh.ProgressStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WangXing on 2018/3/7.
 */

public abstract class LoadListPresenter<T, M, V> extends BasePresenter<M, V> implements XRecyclerView.LoadingListener {


    public int mPage = 1;
    public int mPageSize = 10;
    public Map<String, String> mParamsMap;
    public String mSecret;

    private CommonLayout mCommonLayout;
    private XRecyclerView mRecyclerView;
    private BaseQuickAdapter<T> mAdapter;

    private ApiCallBack<List<T>> mCallBack;

    public LoadListPresenter() {
    }

    public LoadListPresenter(int pageSize) {
        mPageSize = pageSize;
    }

    public void initLoadParams(String secret, Map<String, String> params) {
        this.mSecret = secret;
        this.mParamsMap = params;
    }

    public void initLoadView(CommonLayout commonLayout, XRecyclerView recyclerView, BaseQuickAdapter<T> adapter) {
        initLoadView(commonLayout, recyclerView, adapter, null);
    }

    public void initLoadView(CommonLayout commonLayout, XRecyclerView recyclerView, BaseQuickAdapter<T> adapter, BaseRefreshHeader refreshHeader) {
        LoadingMoreFooter footer = new LoadingMoreFooter(mContext);
        footer.setProgressStyle(ProgressStyle.TriangleSkewSpin);
        mCommonLayout = commonLayout;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mRecyclerView.setLoadingMoreFooter(footer);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        if (refreshHeader != null) {
            mRecyclerView.setRefreshHeader(refreshHeader);
        } else {
            mRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        }
        mRecyclerView.setFootViewText("努力加载中...", "没有更多数据");
        mCommonLayout.setContentView(recyclerView);
        reload();
    }

    private void reload() {

        mAdapter.setListAll(new ArrayList<T>());
        mPage = 1;
        mPageSize = 10;
        mCommonLayout.showLoading();
        if (mParamsMap != null) {
            mParamsMap.put("pageNum", String.valueOf(mPage));
        }
        requestNextPage();
    }

    private void loadMore() {
        if (mParamsMap != null) {
            mParamsMap.put("pageNum", String.valueOf(mPage));
        }
        requestNextPage();
    }

    public abstract void requestNextPage();


    public ApiCallBack<List<T>> getCallBack() {
        mCallBack = new ApiCallBack<List<T>>(mContext) {
            @Override
            protected void _onNext(List<T> bean, String message) {
                if (bean == null || bean == null) {
                    if (mPage == 1) {
                        mCommonLayout.showEmpty();
                    } else {
                        mRecyclerView.setNoMore(true);//没有下一页
                    }
                    return;
                }

                if (mPage == 1) {
                    if (!bean.isEmpty()) {
                        mPageSize = bean.size(); // API可能只需要page, 而不需要page size
                        if (mPageSize < 10) { // 厂家分销第一页返回8条数据
                            mPageSize = 10;
                        }
                        mCommonLayout.showContent();
                    } else {
                        mCommonLayout.showEmpty();
                    }
                }

                if (bean.size() <= mPageSize) {
                    mAdapter.addItemsToLast(bean);
                    if (bean.size() >= mPageSize) {
                        mRecyclerView.loadMoreComplete();
                    } else {
                        mRecyclerView.setNoMore(true);//没有下一页
                    }
                }

                mPage++;
                if (mRecyclerView.isRefreshing()) {
                    mRecyclerView.refreshComplete();
                }
            }


            @Override
            protected void _onError(ServerException exception) {
                if (mPage == 1) {
                    if (!exception.mErrorCode.equals(ServerException.ERROR_NO_DATA)) {
                        mCommonLayout.showError();
                    } else {
                        mCommonLayout.showEmpty();
                    }
                } else {
                    if (exception.mErrorCode.equals(ServerException.ERROR_NO_DATA)) {
                        mRecyclerView.setNoMore(true);//没有下一页
                    } else {
                        mRecyclerView.setFootViewText("努力加载中...", "加载失败");
                        mRecyclerView.setNoMore(true);
                    }
                }
                if (mRecyclerView.isRefreshing()) {
                    mRecyclerView.refreshComplete();
                }
            }
        };

        return mCallBack;
    }


    //刷新
    @Override
    public void onRefresh() {
        reload();
    }

    //加载更多
    @Override
    public void onLoadMore() {
        loadMore();
    }
}
