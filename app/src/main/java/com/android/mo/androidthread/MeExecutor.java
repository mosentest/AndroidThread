package com.android.mo.androidthread;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * http://www.cnblogs.com/dolphin0520/p/3949310.html
 * http://www.cnblogs.com/wanqieddy/archive/2013/09/06/3305482.html
 * Created by Administrator on 2016/5/24.
 */
public class MeExecutor {

    private MeExecutor() {
        throw new RuntimeException("no access permission");
    }

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;


    private HandlerThread mHandlerThread;

    private Handler localHandler;//用于操作本地IO

    private static NetHandler netHandler;//用于操作网络IO

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "MeExecutor #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);


    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            sThreadFactory);


    private static class NetHandler extends Handler {
        public NetHandler() {
            super(Looper.getMainLooper());
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {

        }
    }

    private static Handler getNetHandler() {
        synchronized (MeExecutor.class) {
            if (netHandler == null) {
                netHandler = new NetHandler();
            }
            return netHandler;
        }
    }

    public static void doNetIOGet(int tag, String url, final MeCallback callback) {
        THREAD_POOL_EXECUTOR.execute(new MeNetRunnable(MeNetRunnable.GET, tag, url, new MeNetCallback() {

            @Override
            public void onSuccess(final int tag, final String entity) {
                getNetHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(tag, entity);
                    }
                });
            }

            @Override
            public void onFailure(final int tag, final String msg, final int code) {
                getNetHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(tag, msg, code);
                    }
                });
            }

        }));
    }
}
