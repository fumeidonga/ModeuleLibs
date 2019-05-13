package com.rebeau.technology.android.dagger.activitys.method_inject.itest;

import android.content.Context;

import com.rebeau.base.utils.RBLogUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Test1 {

    private Context mContext;
//    @Inject
    public Test1() {
    }

    @Inject
    public Test1(Context mContext) {
        this.mContext = mContext;
        RBLogUtil.dt(mContext);
    }

}
