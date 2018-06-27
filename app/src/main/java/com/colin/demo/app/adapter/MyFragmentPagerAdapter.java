package com.colin.demo.app.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;


public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Integer> fragmentIDList;
    private List<String> fragmentTitleList;
    private List<? extends Fragment> fragmentList;
    private FragmentManager fragmentManager;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<? extends Fragment> fragmentList, List<String> fragmentTitleList) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragmentList;
        this.fragmentTitleList = fragmentTitleList;
    }

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<? extends Fragment> fragmentList, List<Integer> fragmentIDList, List<String> fragmentTitleList) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragmentList;
        this.fragmentIDList = fragmentIDList;
        this.fragmentTitleList = fragmentTitleList;
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position % fragmentList.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(fragmentTitleList != null ? fragmentTitleList.get(position) : fragmentIDList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
