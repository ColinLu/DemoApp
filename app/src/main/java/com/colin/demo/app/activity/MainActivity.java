package com.colin.demo.app.activity;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colin.demo.app.R;
import com.colin.demo.app.activity.configuration.ConfigurationActivity;
import com.colin.demo.app.activity.downloadmanager.DownloadManagerActivity;
import com.colin.demo.app.adapter.MyFragmentPagerAdapter;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.callback.MyOnPageChangeListener;
import com.colin.demo.app.callback.OnFragmentListener;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.effect.DepthPageTransformer;
import com.colin.demo.app.fragment.MethodFragment;
import com.colin.demo.app.fragment.OtherFragment;
import com.colin.demo.app.fragment.ViewFragment;
import com.colin.device.api.library.DeviceApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentListener {
    private DrawerLayout drawer_main;
    private BottomNavigationView navigation_main_tab;
    private ViewPager pager_main_content;
    private List<Fragment> mList;
    private MyFragmentPagerAdapter mAdapter;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        if (null != mList) {
            mList.clear();
        }
        mList = null;
        mAdapter = null;
        super.onDestroy();
    }

    @Override
    protected void initView() {
        drawer_main = findViewById(R.id.drawer_main);
        navigation_main_tab = findViewById(R.id.navigation_main_tab);
        pager_main_content = findViewById(R.id.pager_main_content);
        initDrawerLayout();
        initViewPager();

    }


    private void initDrawerLayout() {
        if (null == drawer_main || null == mToolbar) {
            return;
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_main, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_main.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initViewPager() {
        if (null == mList) {
            mList = new ArrayList<>();
            mList.add(ViewFragment.newInstance(Constants.FRAGMENT_MAIN_VIEW, Constants.FRAGMENT_MIAN_TITLE_ARRAY[Constants.FRAGMENT_MAIN_VIEW]));
            mList.add(MethodFragment.newInstance(Constants.FRAGMENT_MAIN_METHOD, Constants.FRAGMENT_MIAN_TITLE_ARRAY[Constants.FRAGMENT_MAIN_METHOD]));
            mList.add(OtherFragment.newInstance(Constants.FRAGMENT_MAIN_OTHER, Constants.FRAGMENT_MIAN_TITLE_ARRAY[Constants.FRAGMENT_MAIN_OTHER]));
        }

        if (null == mAdapter) {
            mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mList, Arrays.asList(Constants.FRAGMENT_MIAN_TITLE_ARRAY));
        }
        pager_main_content.setAdapter(mAdapter);
        pager_main_content.setOffscreenPageLimit(mList.size());
        pager_main_content.setPageTransformer(true, new DepthPageTransformer());
        selectPager(Constants.FRAGMENT_MAIN_VIEW);
        navigation_main_tab.setSelectedItemId(navigation_main_tab.getMenu().getItem(position).getItemId());
    }

    private void selectPager(int position) {
        if (this.position == position) {
            return;
        }

        this.position = position;
        pager_main_content.setCurrentItem(position);
        navigation_main_tab.setSelectedItemId(navigation_main_tab.getMenu().getItem(position).getItemId());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        navigation_main_tab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_main_view:
                        selectPager(Constants.FRAGMENT_MAIN_VIEW);
                        return true;
                    case R.id.tab_main_method:
                        selectPager(Constants.FRAGMENT_MAIN_METHOD);
                        return true;
                    case R.id.tab_main_other:
                        selectPager(Constants.FRAGMENT_MAIN_OTHER);
                        return true;
                    default:
                        return false;
                }
            }
        });
        pager_main_content.addOnPageChangeListener(new MyOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectPager(position);
            }
        });
    }

    @Override
    protected void initAsync() {

    }

    @Override
    public void onBackPressed() {
        if (null != drawer_main && drawer_main.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                DeviceApi.getInstance().showToast(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_download:
                startActivity(DownloadManagerActivity.class);
                break;
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_configuration:
                startActivity(ConfigurationActivity.class);
                break;
            default:
                break;
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        if (null != drawer_main && drawer_main.isDrawerOpen(GravityCompat.START)) {
            drawer_main.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onFragmentClick(View view, int fragment_id, int position, Object object) {

    }
}
