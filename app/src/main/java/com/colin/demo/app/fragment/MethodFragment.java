package com.colin.demo.app.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.activity.configuration.ConfigurationActivity;
import com.colin.demo.app.activity.configuration.SystemActivity;
import com.colin.demo.app.activity.downloadmanager.DownloadManagerActivity;
import com.colin.demo.app.activity.handler.HandlerActivity;
import com.colin.demo.app.activity.touch.TouchActivity;
import com.colin.demo.app.adapter.ItemAdapter;
import com.colin.demo.app.base.BaseAdapter;
import com.colin.demo.app.base.BaseFragment;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.callback.OnRecyclerItemClickListener;
import com.colin.demo.app.utils.InitViewUtil;

import java.util.ArrayList;
import java.util.List;

public class MethodFragment extends BaseFragment {
    private RecyclerView recycler_list;
    private List<ItemBean> mList;
    private ItemAdapter mAdapter;

    @Override
    public void onDestroyView() {
        if (null != mList) {
            mList.clear();
        }
        mList = null;
        mAdapter = null;
        super.onDestroyView();
    }

    public MethodFragment() {

    }

    public static MethodFragment newInstance(int fragment_id, String fragmentTitle) {
        MethodFragment fragment = new MethodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_ID, fragment_id);
        bundle.putString(FRAGMENT_TITLE, fragmentTitle);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        fragmentID = bundle.getInt(FRAGMENT_ID);
        fragmentTitle = bundle.getString(FRAGMENT_TITLE);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_item;
    }

    @Override
    protected void initView() {
        recycler_list = mFragmentView.findViewById(R.id.recycler_list);
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (null == mList) {
            mList = new ArrayList<>();
        }
        if (null == mAdapter) {
            mAdapter = new ItemAdapter(getActivity(), mList);
        }
        mAdapter.setEmptyState(BaseAdapter.EMPTY_STATE_LOADING);
        mAdapter.notifyDataSetChanged();
        InitViewUtil.initRecyclerView(recycler_list, mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        refresh_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAsync();
            }
        });

        //加载更多
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (!isDestroy && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mList.size() > 0
                        && mList.size() % 10 == 0
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    refresh_list.setRefreshing(true);
                    mAdapter.setFootState(BaseAdapter.FOOT_STATE_LOADING);
                    mAdapter.notifyDataSetChanged();
                    recycler_list.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(false);
                        }
                    }, 3000);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClick(View view, Object object, int position) {
                switch (view.getId()) {
                    case R.id.layout_item_content:
                        if (null == object || !(object instanceof ItemBean)) {
                            return;
                        }
                        if (null == ((ItemBean) object).clazz) {
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("clazz", ((ItemBean) object));
                        startActivity(((ItemBean) object).clazz, bundle);
                        break;
                }
            }
        });
    }

    @Override
    protected void initAsync() {
        if (null != refresh_list && !refresh_list.isRefreshing()) {
            refresh_list.setRefreshing(true);
        }
        recycler_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(true);
            }
        }, 3000);
    }

    private void loadData(boolean refresh) {
        page = refresh ? 1 : page + 1;
        mList.clear();

        mList.add(new ItemBean(1, "事件分发", TouchActivity.class));
        mList.add(new ItemBean(3, "APP更新升级", DownloadManagerActivity.class));
        mList.add(new ItemBean(4, "获取手机配置信息", ConfigurationActivity.class));
        mList.add(new ItemBean(6, "手机系统配置信息", SystemActivity.class));
        mList.add(new ItemBean(7, "Handler", HandlerActivity.class));

        mAdapter.setEmptyState(BaseAdapter.EMPTY_STATE_NO);
        mAdapter.setFootState(BaseAdapter.FOOT_STATE_FINISH);
        mAdapter.notifyDataSetChanged();
        refresh_list.setRefreshing(false);
    }

    private int getIndex(int page, int size, int i) {
        return (page - 1) * size + i + 1;
    }
}
