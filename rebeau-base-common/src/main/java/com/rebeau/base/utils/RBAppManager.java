package com.rebeau.base.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */

public class RBAppManager {
    private static final String TAG = "RBAppManager";
    private static Stack<Activity> activityStack;
    private static RBAppManager instance;
    private static Queue<Activity> bookDetailActivities;
    private static final int BOOK_DETAIL_MAX_5 = 5;

    private RBAppManager() {
        activityStack = new Stack<Activity>();
        bookDetailActivities = new ArrayBlockingQueue<>(BOOK_DETAIL_MAX_5);
    }

    public static RBAppManager getAppManager() {
        if (instance == null) {
            synchronized (RBAppManager.class) {
                if (instance == null) {
                    instance = new RBAppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity, boolean isStint) {
        if (isStint) {
            if (bookDetailActivities == null) {
                bookDetailActivities = new ArrayBlockingQueue<>(BOOK_DETAIL_MAX_5);
            } else if (bookDetailActivities.size() == BOOK_DETAIL_MAX_5) {
                Activity oldAty = bookDetailActivities.remove();
                activityStack.remove(oldAty);
                if (oldAty != null && (!oldAty.isFinishing() || !oldAty.isDestroyed())) {
                    oldAty.finish();
                }
            }
            bookDetailActivities.add(activity);
        }

        addAty(activity);
        RBLogUtil.v(TAG, "新增加的Activity为：" + activity.toString());
    }

    private void addAty(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return
     */
    public Activity currentActivity() {
        try {
            return activityStack.lastElement();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据倒序索引获取Activity
     *
     * @param index
     * @return
     */
    public Activity getActivity(int index) {
        if (index >= 0 && index < activityStack.size()) {
            return activityStack.get(activityStack.size() - 1 - index);
        }
        return null;
    }

    public int getActivitySize(){
        return activityStack.size();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
        RBLogUtil.v(TAG, "结束当前的Activity为：" + activity.toString());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            bookDetailActivities.remove(activity);
            RBLogUtil.v(TAG, "结束指定的Activity为：" + activity.toString());
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                RBLogUtil.v(TAG, "结束指定类名的Activity为：" + activity.toString());
            }
        }
    }

    public boolean containActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                RBLogUtil.v(TAG, "结束所有Activity为：" + activityStack.get(i).toString());
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        bookDetailActivities.clear();
    }

    /**
     * 退回某Activity
     *
     * @param cls
     */
    public void returnActivity(Class<? extends Activity> cls) {
        if (containActivity(cls)) {
            for (int i = activityStack.size() - 1; i > 0; i--) {
                if (null != activityStack.get(i)) {
                    if (activityStack.get(i).getClass().equals(cls)) {
                        break;
                    } else {
                        activityStack.get(i).finish();
                        activityStack.pop();
                    }
                }
            }
        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            //ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //activityMgr.restartPackage(context.getPackageName());
            //android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            //android.os.Process.killProcess(android.os.Process.myPid());
        }
        /*报错umeng统计数据*//*
		MobclickAgent.onKillProcess(context);
		android.os.Process.killProcess(android.os.Process.myPid());//获取PID
		System.exit(0);*/
        RBLogUtil.v(TAG, "退出 应用程序");
    }
}