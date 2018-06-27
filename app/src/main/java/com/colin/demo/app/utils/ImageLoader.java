package com.colin.demo.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestOptions;
import com.colin.demo.app.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;

/**
 * author:chosen
 * date:2016/11/2 14:40
 * email:812219713@qq.com
 * 图片加载管理类
 */

public class ImageLoader {
    /**
     * 下载图片接口回调
     */
    public interface OnLoadImageCallBack {
        void loadImage(boolean success, Bitmap bitmap, Object object);
    }

    /**
     * 清理图片缓存接口回调
     */
    public interface OnClearImageCacheListener {
        void clearCache(long size);
    }

    private ImageLoader() {
    }


    public static class Holder {
        static ImageLoader instance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return Holder.instance;
    }


    /**
     * 暂停加载
     */
    public void pause(Context context) {
        if (null == context) {
            return;
        }
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复加载
     */
    public void onResume(Context context) {
        if (null == context) {
            return;
        }
        Glide.with(context).resumeRequests();
    }

    /**
     * 清楚缓存
     */
    public void clearMemory(Context context, OnClearImageCacheListener onClearImageCacheListener) {
        if (null == context) {
            return;
        }
        clearImageDiskCache(context);
        new ClearDiskCache(context, onClearImageCacheListener).execute();
    }

    public void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, null, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round);
    }

    public void loadImage(ImageView imageView, File file) {
        loadImage(imageView, null, file, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round);
    }

    public void loadImage(ImageView imageView, String url, int errorRes) {
        loadImage(imageView, url, null, errorRes, errorRes);
    }

    public void loadImage(ImageView imageView, String url, File file, int placeholderRes, int errorRes) {
        if (null == imageView || null == imageView.getContext()) {
            return;
        }
        if (StringUtil.isEmpty(url) && errorRes < 0) {
            return;
        }
        if (StringUtil.isEmpty(url) && null == file) {
            imageView.setImageResource(errorRes);
            return;
        }
        if (StringUtil.isEmpty(url)) {
            Glide.with(imageView).load(file).apply(getRequest(errorRes, placeholderRes)).into(imageView);
        } else {
            Glide.with(imageView).load(url).apply(getRequest(errorRes, placeholderRes)).into(imageView);
        }

    }


    public void loadGitImage(ImageView imageView, int imageRes) {
        if (null == imageView || null == imageView.getContext() || imageRes < 0) {
            return;
        }
        Glide.with(imageView.getContext())
                .asGif()
                .load(imageRes)
                .thumbnail(0.5F)
                .apply(getGifRequest())
                .into(imageView);
    }

    private RequestOptions getGifRequest() {
        return new RequestOptions()
                .fitCenter()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    private RequestOptions getRequest(int errorRes, int placeholderRes) {
        return new RequestOptions()
                .skipMemoryCache(true)
                .error(errorRes)
                .priority(Priority.HIGH)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    /**
     * 清除图片磁盘缓存
     * 只能在子线程执行
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     * 只能在主线程执行
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClearDiskCache extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> mWeakReference;
        private OnClearImageCacheListener mCacheListener;

        public ClearDiskCache(Context context) {
            this(context, null);
        }

        public ClearDiskCache(Context context, OnClearImageCacheListener cacheListener) {
            mWeakReference = new WeakReference<>(context);
            mCacheListener = cacheListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (null == mWeakReference) {
                return null;
            }
            Context context = mWeakReference.get();
            if (null == context) {
                return null;
            }
            Glide.get(context.getApplicationContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mCacheListener = null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Context context = mWeakReference.get();
            if (null == context || null == mCacheListener) {
                return;
            }
            mCacheListener.clearCache(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        }
    }


    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 格式化单位
     *
     * @param byteNum size
     * @return size
     */
    @SuppressLint("DefaultLocale")
    public String getFormatSize(long byteNum) {
        if (byteNum < 1073741824) {
            return String.format("%.2fMB", new BigDecimal(byteNum / 1048576).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        } else {
            return String.format("%.2fGB", new BigDecimal(byteNum / 1073741824).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }
}
