package com.rebeau.technology.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.android.dagger.DaggerTestActivity;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class AndroidFragmentOne extends BaseLazyLoadFragment {

    public static AndroidFragmentOne newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        AndroidFragmentOne fragment = new AndroidFragmentOne();
        fragment.setArguments(args);
        RBLogUtil.dt();
        return fragment;
    }

    private String mType;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RBLogUtil.dt();
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    };

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_android, container, false);
        ButterKnife.bind(this, view);

        RBLogUtil.dt();
        if (getArguments() != null) {
            mType = getArguments().getString("type");
        }
        return view;
    }


    @Override
    protected boolean haveLazyData() {
        RBLogUtil.dt();
        return isLazyLoad;
    }

    @Override
    protected void onLoadData() {
        RBLogUtil.dt();
        isLazyLoad = true;
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @OnClick(R.id.show_dialog0)
    public void button0(){
        //startActivity(new Intent(mActivity, MainPerformanceActivity.class));
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){

    }

    @OnClick(R.id.show_dialog2)
    public void button2(){

    }

    @OnClick(R.id.show_dialog3)
    public void button3(){

    }

    @OnClick(R.id.show_dialog4)
    public void button4(){

    }

    @OnClick(R.id.show_dialog5)
    public void button5(){

    }

    @OnClick(R.id.show_dialog6)
    public void button6(){

        startActivity(new Intent(mActivity, DaggerTestActivity.class));
    }

    @OnClick(R.id.show_dialog7)
    public void button7(){

        MarkdownUtils.setData(mActivity, "lifecycle/Lifecycle.md");
    }

    @OnClick(R.id.show_dialog8)
    public void button8(){

        MarkdownUtils.setData(mActivity, "android/监听应用前后台切换.md");
    }

    @OnClick(R.id.show_dialog9)
    public void button9(){
        MarkdownUtils.setData(mActivity, "android/监听锁屏.md");

    }
}
