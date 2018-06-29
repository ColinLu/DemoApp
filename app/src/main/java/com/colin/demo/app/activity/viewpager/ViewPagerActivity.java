package com.colin.demo.app.activity.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.colin.demo.app.R;
import com.colin.demo.app.adapter.ImagePagerAdapter;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.data.Constants;
import com.colin.demo.app.transformer.AlphaPageTransformer;
import com.colin.demo.app.transformer.NonPageTransformer;
import com.colin.demo.app.transformer.RotateDownPageTransformer;
import com.colin.demo.app.transformer.RotateUpPageTransformer;
import com.colin.demo.app.transformer.RotateYTransformer;
import com.colin.demo.app.transformer.ScaleAlphaTransformer;
import com.colin.demo.app.transformer.ScaleInTransformer;
import com.colin.demo.app.transformer.ScalePageTransformer;

import java.util.Arrays;

public class ViewPagerActivity extends BaseActivity {
    private ViewPager pager_show_gallery;
    private ViewPager pager_show_standard;
    private ViewPager pager_show_alpha;
    private ImagePagerAdapter mAdapter;
    private String title = "RotateDown";
    private ItemBean mItemBean;

    @Override
    protected void onDestroy() {
        mItemBean = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
    }

    @Override
    protected void initView() {
        this.pager_show_gallery = this.findViewById(R.id.pager_show_gallery);
        this.pager_show_standard = this.findViewById(R.id.pager_show_standard);
        this.pager_show_alpha = this.findViewById(R.id.pager_show_alpha);
        initViewPager();
    }

    private void initViewPager() {
        pager_show_standard.setPageMargin(this.getResources().getDimensionPixelSize(R.dimen.fab_margin));
        pager_show_standard.setOffscreenPageLimit(3);
        pager_show_standard.setAdapter(new ImagePagerAdapter(this, Arrays.asList(Constants.IMAGE_URL)));
        pager_show_standard.setPageTransformer(true, NonPageTransformer.INSTANCE);

        pager_show_gallery.setPageMargin(this.getResources().getDimensionPixelSize(R.dimen.fab_margin));
        pager_show_gallery.setOffscreenPageLimit(3);
        mAdapter = new ImagePagerAdapter(this, Arrays.asList(Constants.IMAGE_URL));
        pager_show_gallery.setAdapter(mAdapter);
        pager_show_gallery.setPageTransformer(true, getTransformer());


        pager_show_alpha.setPageMargin(this.getResources().getDimensionPixelSize(R.dimen.fab_margin));
        pager_show_alpha.setOffscreenPageLimit(3);
        pager_show_alpha.setAdapter(new ImagePagerAdapter(this, Arrays.asList(Constants.IMAGE_URL),true));
        pager_show_alpha.setPageTransformer(true, new ScaleAlphaTransformer());
        pager_show_alpha.setCurrentItem(5);
    }


    private ViewPager.PageTransformer getTransformer() {
        if ("RotateDown".equals(title)) {
            return new RotateDownPageTransformer();
        } else if ("RotateUp".equals(title)) {
            return new RotateUpPageTransformer();
        } else if ("RotateY".equals(title)) {
            return new RotateYTransformer(45);
        } else if ("Standard".equals(title)) {
            return NonPageTransformer.INSTANCE;
        } else if ("Alpha".equals(title)) {
            return new AlphaPageTransformer();
        } else if ("Scale".equals(title)) {
            return new ScalePageTransformer();
        } else if ("Scale and Alpha".equals(title)) {
            return new ScalePageTransformer(new AlphaPageTransformer());
        } else if ("ScaleIn".equals(title)) {
            return new ScaleInTransformer();
        } else if ("RotateDown and Alpha".equals(title)) {
            return new RotateDownPageTransformer(new AlphaPageTransformer());
        } else if ("RotateDown and Alpha And ScaleIn".equals(title)) {
            return new RotateDownPageTransformer(new AlphaPageTransformer(new ScaleInTransformer()));
        } else {
            return NonPageTransformer.INSTANCE;
        }
    }

    @Override
    protected void initData() {
        setTitle(title);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAsync() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_pager, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        title = item.getTitle().toString();
        setTitle(title);
        pager_show_gallery.setAdapter(mAdapter);
        pager_show_gallery.setPageTransformer(true, getTransformer());
        return true;
    }

}
