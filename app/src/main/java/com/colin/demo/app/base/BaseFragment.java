package com.colin.demo.app.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colin.demo.app.R;
import com.colin.demo.app.callback.OnFragmentListener;
import com.colin.demo.app.utils.InitViewUtil;
import com.colin.demo.app.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public abstract class BaseFragment extends Fragment {
    protected static final String FRAGMENT_TITLE = "fragmentTitle";
    protected static final String FRAGMENT_ID = "fragmentID";
    protected String fragmentTitle;
    protected int fragmentID;

    protected View mFragmentView;                               //整个Fragment视图父布局
    protected SwipeRefreshLayout refresh_list;                  //常用布局刷新控件
    protected OnFragmentListener mOnFragmentListener = null;    //与activity的接口回调

    protected boolean isDestroy = false;                        //变量 表示当前fragment状态
    protected boolean isResume = false;                         //变量 表示当前fragment状态

    protected boolean isPrepared;
    protected boolean isFirstVisible = true;
    protected boolean isFirstInvisible = true;
    protected boolean isFirstResume = true;
    protected int page = 1;
    protected int pageSize = 20;

    /**
     * 当Activity与Fragment发生关联时调用
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mOnFragmentListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "界面必须实现OnFragmentListener接口");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * //创建该Fragment的视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResID() != 0) {
            return mFragmentView = inflater.inflate(getLayoutResID(), container, false);
        } else {
            return mFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * 当Activity的onCreate()；方法返回时调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        isResume = true;
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        isResume = false;
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onStop() {
        isResume = false;
        super.onStop();
    }

    /**
     * 当改Fragment被移除时调用
     */
    @Override
    public void onDestroyView() {
        isDestroy = true;
        mFragmentView = null;
        mOnFragmentListener = null;

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 当Fragment与Activity的关联被取消时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
            mOnFragmentListener = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            initBaseView();
            initView();
            initData();
            initListener();
            initAsync();
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    private void initBaseView() {
        if (null == mFragmentView) {
            return;
        }
        refresh_list = mFragmentView.findViewById(R.id.refresh_list);
        if (null != refresh_list) {
            InitViewUtil.initSwipeRefreshLayout(refresh_list);
        }
    }


    /**
     * 第一次fragment可见（进行初始化工作）
     */
    protected void onFirstUserVisible() {
        LogUtil.e("第一次fragment可见（进行初始化工作）");
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    protected void onUserVisible() {
        LogUtil.e("fragment可见（切换回来或者onResume）");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    protected void onFirstUserInvisible() {
        LogUtil.e("第一次fragment不可见（不建议在此处理事件）");
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    protected void onUserInvisible() {
        LogUtil.e("fragment不可见（切换掉或者onPause）");
    }


    public void startActivity(@NonNull Class<? extends Activity> target) {
        startActivity(target, null, false);
    }

    public void startActivity(@NonNull Class<? extends Activity> target, @Nullable Bundle bundle) {
        startActivity(target, bundle, false);
    }

    @SuppressLint("NewApi")
    public void startActivity(@NonNull Class<? extends Activity> target, @Nullable Bundle bundle, boolean closeSelf) {
        if (null == getActivity()) {
            return;
        }
        Intent intent = new Intent(getActivity(), target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        if (closeSelf) {
            this.getActivity().finish();
            getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    public void startActivityForResult(@NonNull Class<? extends Activity> target, int requestCode) {
        startActivityForResult(target, requestCode, null);
    }

    public void startActivityForResult(@NonNull Class<? extends Activity> target, int requestCode, @Nullable Bundle bundle) {
        if (null == getActivity()) {
            return;
        }
        Intent intent = new Intent(getActivity(), target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        this.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }


    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void initAsync();

}
