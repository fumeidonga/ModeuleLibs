package com.rebeau.technology.android.performance.layout;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RBLogUtil.dt(1);
        super.onCreate(savedInstanceState);
        RBLogUtil.dt(2);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        RBLogUtil.dt(3);
    }

    @OnClick(R.id.show_dialog0)
    public void button0(){
        MarkdownUtils.setData(this, "android/performace/Android绘制原理.MD");
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){
        MarkdownUtils.setData(this, "android/performace/Android硬件加速原理.MD");

    }

    @OnClick(R.id.show_dialog2)
    public void button2(){
        MarkdownUtils.setData(this, "android/performace/layout优化.MD");

    }

    @OnClick(R.id.show_dialog3)
    public void button3(){
        MarkdownUtils.setData(this, "android/performace/LayoutInflater.MD");

    }

    @OnClick(R.id.show_dialog4)
    public void button4(){

        MarkdownUtils.setData(this, "android/performace/view的绘制原理.MD");
    }

    @OnClick(R.id.show_dialog5)
    public void button5(){
        MarkdownUtils.setData(this, "android/performace/事件分发.md");

    }

    @OnClick(R.id.show_dialog6)
    public void button6(){
        MarkdownUtils.setData(this, "android/performace/CPU_GPU.MD");

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
