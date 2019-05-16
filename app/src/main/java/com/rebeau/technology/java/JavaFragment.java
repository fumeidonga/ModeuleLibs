package com.rebeau.technology.java;

import android.os.Handler;
import android.os.Message;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseTopTabVPFragment;
import com.rebeau.views.loading.RBLoadStatusView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * author: david
 * date: 2019/5/10
 * description: ${Desc} .
 */
public class JavaFragment extends BaseTopTabVPFragment {


    @Override
    public void initTabUI() {
        RBLogUtil.dt();

        mTitles = new String[]{"基础", "深入", "原理", "更多"};
    }

    @Override
    public void initFragments() {
        RBLogUtil.dt();

        if (mFragmentMap == null) {
            mFragmentMap = new HashMap<>(mTitles.length);
        }
        mFragmentMap.put(0, JavaFragmentOne.newInstance("java fragment 0"));
        mFragmentMap.put(1, JavaFragmentDetail.newInstance("java fragment 1"));
        mFragmentMap.put(2, JavaFragmentDetail.newInstance("java fragment 2"));
        mFragmentMap.put(3, JavaFragmentDetail.newInstance("java fragment 3"));
    }

    @Override
    protected void onLoadData() {
        RBLogUtil.dt();
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
//        new BaseHandler(this).sendEmptyMessageDelayed(0, 1000);
    }

    public static class BaseHandler extends Handler {

        private WeakReference<JavaFragment> mReferenceContext;

        public BaseHandler(JavaFragment reference) {
            mReferenceContext = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mReferenceContext.get() != null) {
                mReferenceContext.get().notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
            }
        }
    }
}
