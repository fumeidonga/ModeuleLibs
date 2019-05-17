package com.rebeau.technology.android.performance.layout;

import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayoutActivity extends BaseFragmentActivity {

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_layout, null);
        ButterKnife.bind(this, view);

        RBLogUtil.dt();
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return "布局优化";
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @OnClick(R.id.show_dialog0)
    public void button0(){
        MarkdownUtils.setData(this, "android/performace/layout优化.MD");
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){
        MarkdownUtils.setData(this, "android/performace/LayoutInflater.MD");
    }

    @OnClick(R.id.show_dialog2)
    public void button2(){
        MarkdownUtils.setData(this, "android/performace/ram/CPU_GPU.MD");
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

    }

    @OnClick(R.id.show_dialog7)
    public void button7(){

    }

    @OnClick(R.id.show_dialog8)
    public void button8(){

    }

    @OnClick(R.id.show_dialog9)
    public void button9(){

    }

}
