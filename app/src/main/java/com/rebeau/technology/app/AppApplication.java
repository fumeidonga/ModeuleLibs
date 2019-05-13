package com.rebeau.technology.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.BaseApplication;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.performancetools.uiwatch.AppUiWatcher;
import com.rebeau.technology.android.dagger.activitys.method_inject.itest.Test1;
import com.rebeau.technology.android.dagger.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * author: david
 * date: 2019/4/29
 * description: ${Desc} .
 */
public class AppApplication extends BaseApplication  implements HasActivityInjector,
        HasSupportFragmentInjector,
        HasServiceInjector,
        HasBroadcastReceiverInjector,
        HasContentProviderInjector {


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;
    @Inject
    DispatchingAndroidInjector<ContentProvider> contentProviderInjector;

    @Inject
    Test1 test1;

    @Override
    public void onCreate() {
        super.onCreate();
        RBLogUtil.defaultLog();
        DaggerAppComponent.builder().application(this).build().inject(this);

        mContext = getApplicationContext();
        RBLogUtil.dt(test1);
        registerActivityLifecycleCallback();


        ///target sdk 23以下才能用
        //BlockCanary.install(this, new AppBlockCanaryContext()).start();

//        BlockDetectByChoreographer.start();

//        Choreographer.getInstance().postFrameCallback(new MyChoreographerFrameCallback());

        AppUiWatcher.getInstance().cacheSize(10).minSkipFrameCount(4).startWatch(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RBLogUtil.dt();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RBLogUtil.dt();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        RBLogUtil.dt();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        RBLogUtil.dt();
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback() {
        this.registerActivityLifecycleCallbacks(new RBActivityLifecycleCallbacks());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RBLogUtil.dt();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return broadcastReceiverInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        return contentProviderInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }
    // 获取到主线程的上下文
    private static Context mContext;

    public static Context getAppContext(){
        return mContext;
    }

    public static SharedPreferences preferences() {
        if(sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences("other-sp",
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private static SharedPreferences sharedPreferences;
    /**
     * 获取字符串格式的数据
     */
    public static String getString(String key, String value) {
        preferences();
        return sharedPreferences.getString(key, value);
    }

    /**
     * 字符串格式保存数据
     */
    public static void saveString(String key, String data) {
        preferences();
        sharedPreferences.edit().putString(key, data).apply();
    }
}

/*public class AppApplication extends DaggerApplication {

//    @Override
//    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//        return DaggerAppComponent.builder().application(this).createMouse();
//    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }

}*/