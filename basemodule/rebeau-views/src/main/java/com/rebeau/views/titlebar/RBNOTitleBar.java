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
 * 主要用在不要titlebar的地方，
 *
 */
public class RBNOTitleBar extends RBBaseTitleBar {

    @BindView(R2.id.tb_status_bar)
    protected View mStatusBar;

    public RBNOTitleBar(Context context) {
        super(context);
    }

    public RBNOTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RBNOTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow();
    }

    protected void attachedToWindow() {
        Activity activity = (Activity) getContext();
        RBStatusBarUtil.initSubStatusBar(activity, mStatusBar, activity.getResources().getColor(android.R.color.white));
    }

    @Override
    public void onInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_no_title_bar_sub_view, this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void setTitleBarName(String title) {

    }

    @Override
    public void setIsRemind(boolean remind) {

    }

}
