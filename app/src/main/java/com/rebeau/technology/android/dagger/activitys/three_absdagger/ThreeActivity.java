package com.rebeau.technology.android.dagger.activitys.three_absdagger;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.technology.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ThreeActivity extends AppCompatActivity implements ThreeContract.View{

    @Inject
    ThreePresenter threePresenter;

    @Inject
    Application application;

    @Inject
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        RBLogUtil.dt(threePresenter);
        RBLogUtil.dt(application);
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
    @Override
    public void editTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void completeTask() {

    }

    @Override
    public void setPresenter(ThreeContract.Presenter presenter) {

    }
}
