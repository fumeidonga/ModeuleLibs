package com.rebeau.technology.android.dagger.activitys.method_inject;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.technology.R;
import com.rebeau.technology.android.dagger.activitys.method_inject.itest.ITest;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class FourActivity extends AppCompatActivity {

    @Inject
    Application application;

    @Inject
    Gson gson;

    /**
     * 构造方法注入
     *
     *
     * iTest 不为空，必须满足下面几个条件
     * 1， 构造方法添加inject
     *     @Inject
     *     public Test() {
     *     }
     * 2，ActivityBindingModule
     *     @ContributesAndroidInjector(modules = {InjectModule.class})
     *     abstract FourActivity fourActivity();
     * 3，oncreate  AndroidInjection.inject(this);
     * 4，
     */
    @Inject
//    @ActivityScoped
            ITest iTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        RBLogUtil.dt(application);
        RBLogUtil.dt(iTest);
        RBLogUtil.dt(gson);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 判断是否从后台恢复, 且时间间隔符合要求, 显示广告页面
        boolean isFromBackToFront = RBActivityLifecycleCallbacks.sAppState == RBActivityLifecycleCallbacks.STATE_BACK_TO_FRONT;
        if (isFromBackToFront) {
            RBLogUtil.e("");
        }
    }
}
