package com.rebeau.technology;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.TextUtils;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;

import java.lang.reflect.Method;

/**
 * @Descriptionn 广告消息接收
 */
public class ScreenOnOffReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            isScreenOn(context);
            isScreenOn1(context);
            isKeyguard(context);
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            RBActivityLifecycleCallbacks.mIsScreenOff = true;
            isScreenOn(context);
            isScreenOn1(context);
            isKeyguard(context);
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            //KMActivityLifecycleCallbacks.mIsScreenOff = true;
            isScreenOn(context);
            isScreenOn1(context);
            isKeyguard(context);
        }
    }



    /////////////////// method two ////////////////////////
//    通过PowerManager的isScreenOn方法
//屏幕“亮”，表示有两种状态：a、未锁屏  b、目前正处于解锁状态  。这两种状态屏幕都是亮的
//
//屏幕“暗”，表示目前屏幕是黑的
//
    public void isScreenOn(Context context) {
        try {
            PowerManager manager = (PowerManager)context.getSystemService(Activity.POWER_SERVICE);
            boolean isScreenOn = manager.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。

            //还可以直接用反射
            Class<?> powerManager = Class.forName("android.os.PowerManager");
            Method method = powerManager.getMethod("isScreenOn", new Class[]{});
            method.setAccessible(true);
            boolean screenState = (boolean) method.invoke(manager);
            RBLogUtil.d(screenState);
        } catch (Exception e) {
            RBLogUtil.d(e);
        }

    }
    public void isScreenOn1(Context context) {
        try {
            PowerManager manager = (PowerManager)context.getSystemService(Activity.POWER_SERVICE);
            ClassLoader cl = context.getClassLoader();
            Class powerManager = cl.loadClass("android.os.PowerManager");
            Method method = powerManager.getMethod("isScreenOn");
            method.setAccessible(true);
            boolean screenState = (boolean) method.invoke(manager);
            RBLogUtil.d(screenState);
        } catch (Exception e) {
            RBLogUtil.d(e);
        }

    }

    public void isKeyguard(Context context) {
        try {
            KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();

            RBLogUtil.d(flag);
        } catch (Exception e) {
            RBLogUtil.d(e);
        }

    }



}
