package com.rebeau.performancetools.lancet;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.performancetools.UIWatchLogCat;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * author: hrl
 * date: 2019/4/18
 * description: ${Desc} .
 */
public class LancetActivityHook {


    public static ActivityInfoRecord mActivityInfoRecord;

    static {
        mActivityInfoRecord = new ActivityInfoRecord();
    }

    @Insert(value = "onCreate",mayCreateSuper = true)
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity",scope = Scope.LEAF)
    protected void onCreate(Bundle savedInstanceState) {
        UIWatchLogCat.resetTime();
        mActivityInfoRecord.mOnCreateTime = System.currentTimeMillis();
        mActivityInfoRecord.isOnCreate = true;
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onCreate 方法 完成 ");
    }

    @Insert(value = "onWindowFocusChanged",mayCreateSuper = true)
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity",scope = Scope.LEAF)
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus && mActivityInfoRecord.isOnCreate) {
            mActivityInfoRecord.isOnCreate = false;
            mActivityInfoRecord.mOnWindowsFocusChangedTime = System.currentTimeMillis();
            UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> 从onCreate -> onWindowFocusChanged 用时 " + " time " +(mActivityInfoRecord.mOnWindowsFocusChangedTime - mActivityInfoRecord.mOnCreateTime));
        }
        Origin.callVoid();
    }


    @Insert(value = "onResume",mayCreateSuper = true)
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity",scope = Scope.LEAF)
    protected void onResume() {
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onResume 方法 完成 ");
        
    }

    @Insert(value = "initSubStatusBar")
    @TargetClass(value = "com.rebeau.base.utils.RBStatusBarUtil")
    public static void initSubStatusBar(Activity activity) {
        UIWatchLogCat.dt("lancet", " StatusBarUtil---> initSubStatusBar begin3 ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", " StatusBarUtil---> initSubStatusBar end3 ");
    }

    @Insert(value = "initLoadStatusView")
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity")
    private void initLoadStatusView() {
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initLoadStatusView begin ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initLoadStatusView end ");
    }

    @Insert(value = "initSlidingPaneBack")
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity")
    private void initSlidingPaneBack() {
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initSlidingPaneBack begin ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initSlidingPaneBack end ");
    }

    @Insert(value = "initTitleBar")
    @TargetClass(value = "com.rebeau.commons.activity.BaseFragmentActivity")
    protected void initTitleBar() {
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initTitleBar begin ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", "BaseAppActivity ---> initTitleBar end ");
    }

}
