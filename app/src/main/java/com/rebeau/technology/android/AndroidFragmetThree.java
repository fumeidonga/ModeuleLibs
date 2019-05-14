package com.rebeau.technology.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class AndroidFragmetThree extends BaseLazyLoadFragment {

    public static AndroidFragmetThree newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        AndroidFragmetThree fragment = new AndroidFragmetThree();
        fragment.setArguments(args);
        RBLogUtil.dt();
        return fragment;
    }


    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_android_three, container, false);
        ButterKnife.bind(this, view);

        RBLogUtil.dt();
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
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @OnClick(R.id.show_dialog0)
    public void button0(){
        MarkdownUtils.setData(mActivity, "android/performace/Android绘制原理.MD");
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){
        MarkdownUtils.setData(mActivity, "android/performace/Android硬件加速原理.MD");

    }

    @OnClick(R.id.show_dialog2)
    public void button2(){
        MarkdownUtils.setData(mActivity, "android/performace/view的绘制原理.MD");
    }

    @OnClick(R.id.show_dialog3)
    public void button3(){


    }

    @OnClick(R.id.show_dialog4)
    public void button4(){

        MarkdownUtils.setData(mActivity, "android/performace/事件分发.md");
    }

    @OnClick(R.id.show_dialog5)
    public void button5(){

    }
}
