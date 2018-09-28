package com.colin.demo.app.activity.thread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.other.MyHandler;
import com.colin.demo.app.utils.ToastUtil;

/**
 * 讨论线程 顺序执行的问题
 * 1当前注意问题  Handler  内存溢出
 * 2更新UI操作
 * 3Thread运行判断
 * 3多次执行Thread start问题
 * 4资源回收
 */
public class ThreadActivity extends BaseActivity {
    private ItemBean mItemBean;
    private TextView text_show_thread_log;
    private Thread firstThread;
    private Thread secondThread;
    private MyThread thirdThread;

    private static final int THREAD_STOP_FLAG = 1;          //利用标记停止线程
    private static final int THREAD_STOP_SUSPEND = 2;       //利用标记停止线程
    private static final int THREAD_STOP_INTERRUPT = 3;     //利用线程方法处理来实现停止

    //接收消息 输出打印日志  主线程操作
    @SuppressLint("HandlerLeak")
    private MyHandler mMyHandler = new MyHandler(this) {
        @Override
        public void weakHandleMessage(Message message) {
            if (isDestroy || null == message || null == message.obj) {
                return;
            }
            text_show_thread_log.append("\n" + message.obj);
        }
    };

    @Override
    protected void onDestroy() {
        mItemBean = null;
        firstThread = null;
        secondThread = null;
        thirdThread = null;
        mMyHandler.removeCallbacksAndMessages(null);
        mMyHandler = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
    }

    @Override
    protected void initView() {
        text_show_thread_log = findViewById(R.id.text_show_thread_log);
        text_show_thread_log.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);


    }


    @Override
    protected void initListener() {
        findViewById(R.id.button_thread_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThread(false);
            }

        });
        findViewById(R.id.button_thread_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThread(true);
            }

        });
        //stop:建议不要用，会让线程戛然而止，无法得知线程完成了什么、没完成什么，当线程正在进行一些耗时操作如读写、数据库操作，突然终止很可能会有错误发生


        //volatile:建议使用，线程会执行完当前操作后停止。
        findViewById(R.id.button_thread_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThirdThread(THREAD_STOP_FLAG);
            }

        });
        //volatile:建议使用，线程会执行完当前操作后停止。
        findViewById(R.id.button_thread_suspend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThirdThread(THREAD_STOP_SUSPEND);
            }

        });
        //interrupt:建议不要用，当线程进入阻塞如 Thread.sleep(5000);调用interrupt会抛出异常，而且线程不会停止
        //interrupt()方法中断正在运行中的线程只会修改中断状态位;阻塞中的线程，那么就会抛出InterruptedException异常
        findViewById(R.id.button_thread_interrupt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThirdThread(THREAD_STOP_INTERRUPT);
            }

        });

        findViewById(R.id.button_thread_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                if (null == thirdThread) {
                    text_show_thread_log.setText("测试线程操作开始、挂起、暂停、恢复等操作");
                    text_show_thread_log.scrollTo(0, 0);
                    thirdThread = new MyThread("线程--3-->>");
                    thirdThread.start();
                    return;
                }
                if (thirdThread.getStatus() == MyThread.RUNNING) {
                    ToastUtil.showToast("当前线程正在运行");
                    return;
                }
                //停止了 重新开始
                if (thirdThread.getStatus() == MyThread.STOP) {
                    sendMessage(String.format("%s:%s", thirdThread.getName(), "重新开始"));
                    thirdThread.myResume();
                    return;
                }
                //挂起 可以重新开始
                if (thirdThread.getStatus() == MyThread.SUSPEND) {
                    sendMessage(String.format("%s:%s", thirdThread.getName(), "手动挂起"));
                    thirdThread.myResume();
                    return;
                }
            }

        });
    }

    private void stopThirdThread(int stopWhich) {
        if (null == thirdThread) {
            ToastUtil.showToast("还未初始化线程");
            return;
        }
        if (stopWhich == THREAD_STOP_FLAG && thirdThread.getStatus() == MyThread.STOP) {
            ToastUtil.showToast("线程停止状态中");
            return;
        }
        if (stopWhich == THREAD_STOP_SUSPEND && thirdThread.getStatus() == MyThread.SUSPEND) {
            ToastUtil.showToast("线程挂起状态");
            return;
        }
        if (stopWhich == THREAD_STOP_FLAG) {
            thirdThread.myStop();
            return;
        }
        if (stopWhich == THREAD_STOP_SUSPEND) {
            thirdThread.mySuspend();
            return;
        }
    }

    @Override
    protected void initAsync() {

    }

    /**
     * 开始线程运行
     *
     * @param isJoin
     */
    private void startThread(boolean isJoin) {
        //判断线程是否正在运行
        if (null != firstThread && firstThread.isAlive()) {
            ToastUtil.showToast("线程1正在运行，请稍等。。。");
            return;
        }
        if (null != secondThread && secondThread.isAlive()) {
            ToastUtil.showToast("线程2正在运行，请稍等。。。");
            return;
        }
        stopThirdThread(THREAD_STOP_FLAG);
        text_show_thread_log.setText(isJoin ? "按顺序执行线程" : "不按顺序执行线程");
        text_show_thread_log.scrollTo(0, 0);
        startFirstThread(isJoin);
        startSecondThread();
    }

    /**
     * 运行第一个线程
     *
     * @param isJoin 是否需要加入顺序队列
     */
    private void startFirstThread(boolean isJoin) {
        firstThread = new Thread(new FirstRunnable(), "线程--1-->>");
        firstThread.start();
        if (isJoin) {
            try {
                firstThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 运行第二个线程
     */
    private void startSecondThread() {
        secondThread = new Thread(new SecondRunnable(), "线程--2-->>");
        secondThread.start();
    }

    /**
     * 子线程操作并且实现 线程阻塞 sleep
     */
    private class FirstRunnable implements Runnable {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                sendMessage(String.format("%s:%d", Thread.currentThread().getName(), i));
                if (i == 50) {
                    try {
                        sendMessage(String.format("%s:%s", Thread.currentThread().getName(), "休息0.5秒钟"));
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 子线程操作
     */
    private class SecondRunnable implements Runnable {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                sendMessage(String.format("%s:%d", Thread.currentThread().getName(), i));
            }
        }
    }

    /**
     * 需要控制暂停开始定操作
     */
    private class ThirdRunnable implements Runnable {
        private int count = -1;
        //外部条件 进行线程停止操作 退出标志;使用volatile目的是保证可见性，一处修改了标志，处处都要去主存读取新的值，而不是使用缓存
        private volatile boolean isRunning = true;

        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            //第一种停止线程
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                count += 1;
                sendMessage(String.format("%s:%d", Thread.currentThread().getName(), count));
                try {
                    sendMessage(String.format("%s:%s", Thread.currentThread().getName(), "休息0.5秒钟"));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sendMessage(String.format("%s:%s", Thread.currentThread().getName(), "线程停止"));
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }
    }

    /**
     * 发送消息内容
     *
     * @param messageObj
     */
    private void sendMessage(@NonNull String messageObj) {
        Message message = Message.obtain();
        message.obj = messageObj;
        mMyHandler.sendMessage(message);
    }


    private class MyThread extends Thread {
        public static final int STOP = -1;        //停止
        public static final int SUSPEND = 0;      //挂起
        public static final int RUNNING = 1;      //运行
        private int status = RUNNING;
        private long count = STOP;


        public MyThread(String name) {
            super(name);
        }

        @Override
        public synchronized void start() {
            super.start();
            status = RUNNING;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public synchronized void run() {
            // 判断是否停止
            while (status != STOP) {
                // 判断是否挂起
                if (status == SUSPEND) {
                    try {
                        // 若线程挂起则阻塞自己
                        wait();
                    } catch (InterruptedException e) {
                        sendMessage(String.format("%s:%s", getName(), "线程异常终止..."));
                    }
                } else {
                    count++;
                    sendMessage(String.format("%s:%d", getName(), count));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        sendMessage(String.format("%s:%s", getName(), "线程异常终止..."));
                    }
                }
            }
        }


        /**
         * 恢复
         */
        public synchronized void myResume() {
            // 修改状态
            status = RUNNING;
            // 唤醒
            notifyAll();
        }

        /**
         * 获取线程状态
         */
        public synchronized int getStatus() {
            return status;
        }

        /**
         * 挂起
         */
        public void mySuspend() {
            // 修改状态
            status = SUSPEND;
        }

        /**
         * 停止
         */
        public void myStop() {
            // 修改状态
            status = STOP;
        }


    }


}
