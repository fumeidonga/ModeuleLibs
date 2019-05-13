package com.rebeau.commons.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.rebeau.base.cache.DataManager;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.config.Constants;

/**
 * 在Application中监听所有Activity生命周期的管理类
 */
public class RBActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private int appCount = 0;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_BACK_TO_FRONT = 1;
    public static final int STATE_FRONT_TO_BACK = 2;
    public static int sAppState = STATE_NORMAL;
    public static boolean mIsScreenOff = false;

    public RBActivityLifecycleCallbacks() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        RBLogUtil.i("onActivityCreated --> %1s", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        appCount++;
        RBLogUtil.i("appCount --> %1d \n onActivityPaused --> %2s", appCount, activity.getClass().getSimpleName());
        if (appCount == 1) {
            if(mIsScreenOff){
                mIsScreenOff = false;
                // 否则是正常状态
                sAppState = STATE_NORMAL;
            } else {
                // 从后台进入前台
                sAppState = STATE_BACK_TO_FRONT;
            }
        } else {
            // 否则是正常状态
            sAppState = STATE_NORMAL;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        RBLogUtil.i("onActivityResumed --> %1s", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        RBLogUtil.i("onActivityPaused --> %1s", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        /**
         * Add In Version 2.4 新增用户判断是否需要立刻上传统计事件
         */
        appCount--;
        RBLogUtil.i("appCount --> %1d \n onActivityStopped --> %2s", appCount, activity.getClass().getSimpleName());
        if (appCount == 0) {
            //保存进入到后台的时间
            DataManager.getInstance().putLong(Constants.SPLASH_ACTIVITY_AD_TIME, System.currentTimeMillis());
            sAppState = STATE_FRONT_TO_BACK;
        } else {
            sAppState = STATE_NORMAL;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        RBLogUtil.i("onActivityDestroyed --> %1s", activity.getClass().getSimpleName());
    }
}
