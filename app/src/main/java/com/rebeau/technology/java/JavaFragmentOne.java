package com.rebeau.technology.java;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.android.rxjava.RxjavaActivity;
import com.rebeau.technology.java.annotations.AnnimationActivity;
import com.rebeau.technology.java.testdesignmodel.DesignModelActivity;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class JavaFragmentOne extends BaseLazyLoadFragment {

    public static JavaFragmentOne newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        JavaFragmentOne fragment = new JavaFragmentOne();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.java_fragment_one, container, false);
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

    @OnClick(R.id.java_fragment_innerclass)
    public void innerClass(){
        MarkdownUtils.setData(mActivity, "lamada/InnerClass.MD");
    }

    @OnClick(R.id.java_fragment_lamada)
    public void ladamda(){
        MarkdownUtils.setData(mActivity, "lamada/LAMBDA.MD");
    }

    @OnClick(R.id.java_fragment_rxjava)
    public void rxjava(){
        startActivity(new Intent(mActivity, RxjavaActivity.class));
    }

    @OnClick(R.id.java_fragment_designmodel)
    public void designmmd(){
        startActivity(new Intent(mActivity, DesignModelActivity.class));
    }

    @OnClick(R.id.java_reflect)
    public void java_reflect(){

    }

    @OnClick(R.id.java_annitation)
    public void java_annitation(){
        startActivity(new Intent(mActivity, AnnimationActivity.class));

    }

    @OnClick(R.id.java_atomic)
    public void java_atomic(){
        //https://www.jianshu.com/p/577c0dc8862c
    }


    @OnClick(R.id.java_map)
    public void java_map(){
        //map hashmap LinkedList ...
    }

}
