package com.rebeau.commons;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.rebeau.base.cache.DataManager;
import com.rebeau.base.utils.RBApplicationUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;

/**
 * Created by Administrator on 2018/3/9.
 * BaseApplication，必须在组件中实现自己的Application，BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:RBApplicationUtil.getContext()，不允许其他写法；
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RBApplicationUtil.init(this);
        DataManager.getInstance().init(this);
        registerActivityLifecycleCallback();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback() {
        this.registerActivityLifecycleCallbacks(new RBActivityLifecycleCallbacks());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }
}
