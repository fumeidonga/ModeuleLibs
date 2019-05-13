package com.rebeau.views.titlebar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rebeau.base.utils.RBStatusBarUtil;
import com.rebeau.views.R;
import com.rebeau.views.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二级页面title bar.
 * 非沉浸式
 * 主要用在右侧按钮有提醒的情况，如问题反馈页面
 *
 */
public class RBRemindTitleBar extends RBSubPrimaryTitleBar {

    @BindView(R2.id.tb_right_remind)
    TextView mRightRemind;

    public RBRemindTitleBar(Context context) {
        super(context);
    }

    public RBRemindTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RBRemindTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void attachedToWindow() {
        Activity activity = (Activity) getContext();
        RBStatusBarUtil.initSubStatusBar(activity, mStatusBar, activity.getResources().getColor(android.R.color.white));
    }

    @Override
    public void onInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_title_bar_sub_remind_view, this);
        ButterKnife.bind(this, view);
        mRightType = RIGHT_TYPE_EMPTY;
    }

    @Override
    public void setTitleBarName(String title) {
        mCenterName.setText(title);
    }

    @Override
    public void setIsRemind(boolean remind) {
        if (remind) {
            mRightRemind.setVisibility(View.VISIBLE);
        } else {
            mRightRemind.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧文字.
     *
     * @param text
     */
    @Override
    public void setRightText(String text) {
        mRightTextView.setText(text);
        mRightTextView.setVisibility(View.VISIBLE);
    }
}
