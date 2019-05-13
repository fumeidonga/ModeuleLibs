package com.rebeau.technology.android.dagger.activitys.two_mvp_dagger;

import android.app.Application;
import android.os.Bundle;
import com.google.gson.Gson;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.technology.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DaggerActivity;

public class TwoActivity extends DaggerActivity implements TwoContract.View{

    @Inject
    TwoPresenter twoPresenter;

    @Inject
    Application application;

    @Inject
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        RBLogUtil.dt(twoPresenter);
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
    public void setPresenter(TwoContract.Presenter presenter) {

    }
}
