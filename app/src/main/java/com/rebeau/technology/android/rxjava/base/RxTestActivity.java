package com.rebeau.technology.android.rxjava.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;


import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.technology.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RxTestActivity extends AppCompatActivity {

    @BindView(R.id.bottom0)
    Button button0;
    @BindView(R.id.bottom1)
    Button button1;
    @BindView(R.id.bottom2)
    Button button2;
    @BindView(R.id.bottom3)
    Button button3;
    @BindView(R.id.bottom4)
    Button button4;
    @BindView(R.id.bottom5)
    Button button5;
    @BindView(R.id.bottom6)
    Button button6;
    @BindView(R.id.bottom7)
    Button button7;
    @BindView(R.id.bottom8)
    Button button8;
    @BindView(R.id.bottom9)
    Button button9;
    @BindView(R.id.bottom10)
    Button button10;
    @BindView(R.id.bottom11)
    Button button11;
    @BindView(R.id.bottom12)
    Button button12;
    @BindView(R.id.bottom13)
    Button button13;
    @BindView(R.id.bottom14)
    Button button14;
    @BindView(R.id.bottom15)
    Button button15;

    /**
     * 获取布局ID
     *
     * @return  布局id
     */

    protected int getContentViewLayoutID() {
        return R.layout.activity_rxjava;
    }

    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 界面初始化前期准备
     */
    protected void beforeInit() {

    }

    /**
     * 初始化布局以及View控件
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInit();
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            ButterKnife.bind(this);
            initView(savedInstanceState);
        }
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
