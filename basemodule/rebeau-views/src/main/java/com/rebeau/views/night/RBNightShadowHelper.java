package com.rebeau.views.night;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.config.Constants;
import com.rebeau.views.slidingview.SlidingPaneLayout;

import java.util.Observable;

/**
 * 夜间模式遮罩布局Layout辅助类
 */

public class RBNightShadowHelper implements AppNightModeObserver, Application.ActivityLifecycleCallbacks {

    private static final String TAG = "RBNightShadowHelper";

    private RBNightShadowFrameLayout mView;
    private Activity mContext;
    /** 在白天夜间模式改变时，是否启用渐变动画 */
    private boolean isGradientAnimatEnable = false;

    private int mBgMode;

    private RBNightShadowHelper(Activity context, int mBgMode) {
        mContext = context;
        mView = new RBNightShadowFrameLayout(context);
        initNightShadow();
        registerNightChangeObserver();
        this.mBgMode =mBgMode;
    }

    public static RBNightShadowHelper create(Activity context, int mBgMode) {
        return new RBNightShadowHelper(context, mBgMode);
    }

    @Override
    public void update(Observable o, Object arg) {
        int bgMode = (int) arg;
        boolean enableNight = (bgMode == Constants.BG_NIGHT);
        if (isGradientAnimatEnable) {
            // night - > day
            if (mBgMode == Constants.BG_NIGHT && !enableNight) {
                mView.valueAnimator(true);
            } else {
                //day - > night
                mView.valueAnimator(false);
            }
        }
        if (mBgMode != bgMode) {
            mBgMode = bgMode;
        }
        mView.invalidate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /* do nothing */
    }

    @Override
    public void onActivityStarted(Activity activity) {
        /* do nothing */
    }

    @Override
    public void onActivityResumed(Activity activity) {
        /* do nothing */
    }

    @Override
    public void onActivityPaused(Activity activity) {
        /* do nothing */
    }

    @Override
    public void onActivityStopped(Activity activity) {
        /* do nothing */
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        /* do nothing */
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        RBLogUtil.d(TAG, "onActivityDestroyed >>> " + activity);
        // 相同的情况下才处理
        if (activity == mContext) {
            unregisterNightChangeObserver();
        }
    }

    private void initNightShadow() {
        ViewGroup decorView = (ViewGroup) mContext.getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 获取DecorView的第一个子View
        ViewGroup child = (ViewGroup) decorView.getChildAt(0);
        // 1.默认情形下：DecorView(FrameLayout) -> LinearLayout -> [id:stub, content]View
        // 2.在继承BaseXXXActivityXXX情形下，会添加RBSlidingPaneLayout，
        //   使其变为：DecorView(FrameLayout) -> RBSlidingPaneLayout(FrameLayout) -> LinearLayout -> [id:stub, content]View
        if (child instanceof LinearLayout) {
            // 如果第一个子View是LinearLayout，那么就在decorView(FrameLayout)下最后位置直接添加夜间遮罩层
            //int childCount = decorView.getChildCount();
            //decorView.addView(nightShadowFrameLayout, childCount - 1 ,layoutParams);
            decorView.addView(mView, layoutParams);
        } else if (child instanceof SlidingPaneLayout) {
            // 如果第一个子View是添加了侧滑RBSlidingPaneLayout(FrameLayout)，那么就在RBSlidingPaneLayout下添加夜间遮罩层
            child.addView(mView, layoutParams);
        } else {
            // 使用默认情形
            decorView.addView(mView, layoutParams);
        }
    }

    private void registerNightChangeObserver() {
        RBLogUtil.d(TAG, "registerNightChangeObserver >>>" + mContext);
        // 注册夜间模式观测者
//        DayNightGlobalObserver.getInstance().registerNightChangeObserver(this);
        AppNightModeObservable.getInstance().addObserver(this);
        // 注册Activity生命周期监听
        mContext.getApplication().registerActivityLifecycleCallbacks(this);
    }

    private void unregisterNightChangeObserver() {
        RBLogUtil.d(TAG, "unregisterNightChangeObserver >>>" + mContext);
//        DayNightGlobalObserver.getInstance().unregisterNightChangeObserver(this);
        AppNightModeObservable.getInstance().deleteObserver(this);
        mContext.getApplication().unregisterActivityLifecycleCallbacks(this);
    }

    public void setGradientAnimatEnable(boolean gradientAnimatEnable) {
        this.isGradientAnimatEnable = gradientAnimatEnable;
    }
}
