package com.wangxing.code.adapter.load;

import com.wangxing.code.adapter.BaseQuickAdapter;
import com.wangxing.code.adapter.LoadingMoreFooter;
import com.wangxing.code.base.BasePresenter;
import com.wangxing.code.http.ApiCallBack;
import com.wangxing.code.http.utils.ServerException;
import com.wangxing.code.view.CommonLayout;
import com.zhouyou.recyclerview.XRecyclerView;
import com.zhouyou.recyclerview.refresh.ProgressStyle;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by WangXing on 2018/3/8.
 */

public abstract class LoadBeanListPresenter<T extends IListResultBean<K>, K, M, V> extends BasePresenter<M, V> implements XRecyclerView.LoadingListener {

    public int mPage = 1;
    public int mPageSize = 10;
    public Map<String, String> mParamsMap;
    public String mSecret;

    private CommonLayout mCommonLayout;
    private XRecyclerView mRecyclerView;
    private BaseQuickAdapter<K> mAdapter;

    private ApiCallBack<T> mCallBack;

    public LoadBeanListPresenter() {
    }

    public LoadBeanListPresenter(int pageSize) {
        mPageSize = pageSize;
    }

    public void initLoadParams(String secret, Map<String, String> params) {
        this.mSecret = secret;
        this.mParamsMap = params;
    }

    public void initLoadView(CommonLayout commonLayout, XRecyclerView recyclerView, BaseQuickAdapter<K> adapter) {
        LoadingMoreFooter footer = new LoadingMoreFooter(mContext);
        footer.setProgressStyle(ProgressStyle.TriangleSkewSpin);
        mCommonLayout = commonLayout;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mRecyclerView.setLoadingMoreFooter(footer);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setFootViewText("努力加载中...", "没有更多数据");
        mCommonLayout.setContentView(recyclerView);
        reload();
    }

    private void reload() {

        mAdapter.setListAll(new ArrayList<K>());
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


    public ApiCallBack<T> getCallBack() {
        mCallBack = new ApiCallBack<T>(mContext) {
            @Override
            protected void _onNext(T bean, String message) {
                if (bean == null || bean.getList() == null) {
                    if (mPage == 1) {
                        mCommonLayout.showEmpty();
                    } else {
                        mRecyclerView.setNoMore(true);//没有下一页
                    }
                    return;
                }

                if (mPage == 1) {
                    if (!bean.getList().isEmpty()) {
                        mPageSize = bean.getList().size(); // API可能只需要page, 而不需要page size
                        if (mPageSize < 10) { // 厂家分销第一页返回8条数据
                            mPageSize = 10;
                        }
                        mCommonLayout.showContent();
                    } else {
                        mCommonLayout.showEmpty();
                    }
                }

                if (bean.getList().size() <= mPageSize) {
                    mAdapter.addItemsToLast(bean.getList());
                    if (bean.getList().size() >= mPageSize) {
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
