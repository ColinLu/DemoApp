package com.colin.demo.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.colin.demo.app.utils.ImageLoader;

import java.util.List;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/25
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mList;
    private SparseArray<ImageView> mViewSparseArray; //缓存控件
    private boolean isLoop = false;//是否循环

    public ImagePagerAdapter(Context context, List<String> list) {
        this(context, list, false);
    }

    public ImagePagerAdapter(Context context, List<String> list, boolean isLoop) {
        mContext = context;
        mList = list;
        mViewSparseArray = new SparseArray<>();
        this.isLoop = isLoop;
    }

    /**
     * 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
     *
     * @return
     */
    @Override
    public int getCount() {
        int number = isLoop ? 100 : 1;
        return getRealCount() * number;
    }

    /**
     * item实际个数
     *
     * @return
     */
    private int getRealCount() {
        return null == mList ? 0 : mList.size();
    }

    public int showPosition(int position) {
        if (null == mList || mList.size() == 0) {
            return 0;
        }
        if (position < 0) {
            return mList.size() - 1;
        }
        return position < mList.size() ? position : (position % getRealCount());
    }

    /**
     * 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     */

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViewSparseArray.remove(position);
    }

    /**
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
     *
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int realPosition = showPosition(position);
        ImageView imageView = mViewSparseArray.get(realPosition);
        if (null == imageView) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().loadImage(imageView, mList.get(realPosition));
            mViewSparseArray.put(realPosition, imageView);
            container.addView(imageView);
        }
        return imageView;
    }

    /**
     * 每次调用 notifyDataSetChanged() 方法时，都会激活 getItemPosition方法
     * POSITION_NONE      表示该Item 会被 destroyItem方法remove 掉，然后重新加载，
     * POSITION_UNCHANGED 表示不会重新加载，默认是 POSITION_UNCHANGED
     *
     * @param object
     * @return
     */
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
