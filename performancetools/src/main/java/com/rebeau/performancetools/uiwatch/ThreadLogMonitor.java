package com.rebeau.performancetools.uiwatch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.rebeau.performancetools.UIWatchLogCat;

import java.lang.reflect.Method;

/**
 * author: hrl
 * date: 2019/4/22
 * description: ${Desc} .
 */
public class ThreadLogMonitor {
    private static ThreadLogMonitor sInstance = new ThreadLogMonitor();
    private HandlerThread mLogThread = new HandlerThread("线程卡顿检测");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 510L;

    private ThreadLogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            sb.append("线程卡顿检测");
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            UIWatchLogCat.dt("线程卡顿检测", sb.toString());
        }
    };

    public static ThreadLogMonitor getInstance() {
        return sInstance;
    }

    // 隐藏的方法
    public boolean isMonitor() {
        Class handler;

        try {
            handler = Class.forName("android.os.Handler");


            Method hasCallbacks = handler.getDeclaredMethod("hasCallbacks", Runnable.class);
            hasCallbacks.setAccessible(true);

            boolean screenState = (boolean) hasCallbacks.invoke(mIoHandler,mLogRunnable);

//            DVLogUtils.dt(screenState);

            return screenState;

        } catch (Exception e) {
            UIWatchLogCat.dt("线程卡顿检测", e.toString());
        }


//        DVLogUtils.dt(false);
        //return mIoHandler.hasCallbacks(mLogRunnable);
        return false;
    }

    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
