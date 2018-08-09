package com.colin.demo.app.image;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 描述：图片加载框架
 * <p>
 * 作者：Colin
 * 时间：2018/8/7
 */
public class ImageLoader {
    private static final int TYPE_FIFO = 0;    //先进先出
    private static final int TYPE_FILO = 1;    //先进后出
    private static final int TYPE_LIFO = 2;    //后进先出

    @IntDef({TYPE_FIFO, TYPE_FILO})
    @Retention(RetentionPolicy.SOURCE)
    private @interface LoadType {
    }


    private ImageLoader(int threadCount, int type) {
        init(threadCount, type);
    }


    private volatile static ImageLoader mInstance;


    public static ImageLoader getInstance(int threadCount, int type) {
        if (null == mInstance) {
            synchronized (ImageLoader.class) {
                if (null == mInstance) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    ///////////////////////////////////////变量/////////////////////////////////////
    //图片缓存的核心对象  管理图片缓存
    private LruCache<String, Bitmap> mLruCache;
    //线程池           加载图片
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;
    //队列的调度模式
    private int mType = TYPE_LIFO;
    //LinkedList 方便调度模式的
    private LinkedList<Runnable> mTaskQueue;
    //后台轮询线程
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    //UI线程中的Handler
    private Handler mUIHandler;
    //同步线程
    private Semaphore mSemaphoremPoolThreadHandler = new Semaphore(0);

    /**
     * 初始化操作
     *
     * @param threadCount
     * @param type
     */
    private void init(int threadCount, int type) {
        //后台轮询线程
        mPoolThread = new Thread() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取出一个任务执行
                        Runnable runnable = getTask();
                        if (null != runnable) {
                            mThreadPool.execute(runnable);
                        }
                    }
                };
                //释放一个信号量
                mSemaphoremPoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //应用缓存大小设置
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;
    }

    /**
     * 从任务队列取出一个方法
     *
     * @return
     */
    private Runnable getTask() {
        if (mType == TYPE_FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == TYPE_LIFO) {
            return mTaskQueue.removeLast();
        }
        return mTaskQueue.removeFirst();
    }

    @SuppressLint("HandlerLeak")
    public void loadImage(final ImageView imageView, final String path) {
        //设置标记
        imageView.setTag(path);
        if (null == mUIHandler) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //得到图片，为ImageView 回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    ImageView imageview = holder.mImageView;
                    Bitmap bitmap = holder.mBitmap;
                    //path getTag 路径比较
                    if (imageview.getTag().toString().trim().equals(holder.path)) {
                        imageview.setImageBitmap(bitmap);
                    }
                }
            };
        }

        Bitmap bitmap = getBitmapFromLruCache(path);
        if (null != bitmap) {
            sendImageMessage(imageView, path, bitmap);
        } else {
            addTasks(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    //加载图片 -> 图片压缩 ->1图片需要显示大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2压缩图片
                    Bitmap bitmap = decodeSampleBitmapFromPath(path, imageSize.width, imageSize.height);
                    //3加入到缓存
                    addBitmapToLruCache(path, bitmap);
                    //4 显示到控件中
                    sendImageMessage(imageView, path, bitmap);
                }


            });
        }
    }

    /**
     * 将图片加入缓存
     *
     * @param path
     * @param bitmap
     */
    private void addBitmapToLruCache(String path, Bitmap bitmap) {
        if (null == getBitmapFromLruCache(path)) {
            mLruCache.put(path, bitmap);
        }
    }

    /**
     * 压缩图片
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //获取图片的宽和高度 ，并不把图片加载内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateSampleSize(options, width, height);
        //使用获取到的inSampleSize
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据需求的宽度 以及图片的宽度高度 计算图片的options.inSampleSize
     *
     * @param options
     * @param width
     * @param height
     * @return
     */
    private int calculateSampleSize(BitmapFactory.Options options, int width, int height) {
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        int inSampleSize = 1;
        if (outWidth > width || outHeight > height) {
            long widthRadio = Math.round(outWidth * 1.0 / width);
            long heightRadio = Math.round(outHeight * 1.0 / height);
            inSampleSize = (int) Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    /**
     * 根据ImageView 获取适当的压缩宽和高
     *
     * @param imageView
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        //获取屏幕宽度
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        //首先获取控件的宽度
        int width = imageView.getWidth();

        if (width <= 0) {
            width = layoutParams.width;//获取ImageView 在layout 声明的宽度
        }
        if (width <= 0) {
            width = imageView.getMaxWidth();//获取ImageView 在layout 声明的最大宽度
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;//获取屏幕的宽度
        }

        //首先获取控件的宽度
        int height = imageView.getHeight();

        if (height <= 0) {
            height = layoutParams.height;//获取ImageView 在layout 声明的高度
        }
        if (height <= 0) {
            height = imageView.getMaxHeight();//获取ImageView 在layout 声明的最大高度
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;//获取屏幕的宽度
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 同步 保证
     *
     * @param runnable
     */
    private synchronized void addTasks(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
//        if (null==mPoolThreadHandler)wait();
            if (null == mPoolThreadHandler) {
                mSemaphoremPoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0X110);
    }

    private void sendImageMessage(ImageView imageView, String path, Bitmap bitmap) {
        if (null == mUIHandler) {
            return;
        }
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.mBitmap = bitmap;
        holder.mImageView = imageView;
        holder.path = path;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 根据path 在缓存中获取图片
     *
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }


    /**
     * 防止图片加载错乱
     */
    private class ImageBeanHolder {
        Bitmap mBitmap;
        ImageView mImageView;
        String path;
    }

    /**
     * 防止图片加载错乱
     */
    private class ImageSize {
        int width;
        int height;
    }
}
