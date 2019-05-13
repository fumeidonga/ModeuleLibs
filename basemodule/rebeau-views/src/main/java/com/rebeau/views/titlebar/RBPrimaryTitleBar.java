package com.rebeau.views.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rebeau.base.utils.RBStatusBarUtil;
import com.rebeau.views.R;
import com.rebeau.views.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一级页面title bar.
 * 沉浸式
 */

public class RBPrimaryTitleBar extends RBBaseTitleBar {

    protected int mRightType;
    @BindView(R2.id.tb_root_layout)
    protected LinearLayout mRoot;
    @BindView(R2.id.tb_status_bar)
    protected View mStatusBar;
    @BindView(R2.id.tb_title_view)
    protected RelativeLayout tb_title_view;
    @BindView(R2.id.tb_left_button)
    protected ImageButton mLeftButton;
    @BindView(R2.id.tb_center_name)
    protected TextView mCenterName;
    @BindView(R2.id.tb_right_text)
    protected TextView mRightTextView;
    @BindView(R2.id.tb_right_button)
    protected ImageButton mRightButton;
    @BindView(R2.id.tb_left_text)
    protected TextView mTbLeftText;

    public RBPrimaryTitleBar(Context context) {
        super(context);
    }

    public RBPrimaryTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RBPrimaryTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_title_bar_primary_view, this);
        ButterKnife.bind(this, view);
        mRightType = RIGHT_TYPE_EMPTY;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow();
    }

    @Override
    protected void onSupportTextTypeFaceChange() {
        if (!mSupportTextTypeFace) {
            int style = Typeface.BOLD;
            mCenterName.setTypeface(Typeface.defaultFromStyle(style));
        }
    }

    @Override
    public void setTitleBarName(String title) {
        mCenterName.setText(title);
    }

    @Override
    public void setIsRemind(boolean remind) {
        // do nothing
    }

    protected void attachedToWindow() {
        Activity activity = (Activity) getContext();
        RBStatusBarUtil.initSubStatusBar(activity, mStatusBar, activity.getResources().getColor(android.R.color.white));
    }

    /**
     * 设置左侧资源.
     *
     * @param resouceId
     */
    public void setLeftResource(@DrawableRes int resouceId) {
        mLeftButton.setBackgroundResource(resouceId);
        mLeftButton.setVisibility(View.VISIBLE);
        mTbLeftText.setVisibility(GONE);
    }

    /**
     * 设置右侧资源.
     *
     * @param resouceId
     */
    public void setRightResource(@DrawableRes int resouceId) {
        mRightButton.setBackgroundResource(resouceId);
        mRightButton.setVisibility(View.VISIBLE);
        mRightTextView.setVisibility(View.GONE);
        mRightType = RIGHT_TYPE_RESOURCE;
    }

    /**
     * 获取右侧文字信息.
     *
     * @return
     */
    public String getRightText() {
        return mRightTextView.getText().toString();
    }

    public ImageButton getRightButton() {
        return mRightButton;
    }

    /**
     * 设置右侧文字.
     *
     * @param text
     */
    public void setRightText(@StringRes int text) {
        mRightTextView.setText(text);
        mRightTextView.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.GONE);
        mRightType = RIGHT_TYPE_TEXT;
    }

    public void setLeftText(@StringRes int text) {
        mTbLeftText.setText(text);
        mTbLeftText.setVisibility(VISIBLE);
        mRightButton.setVisibility(GONE);
    }

    /**
     * 设置右侧可见度.
     *
     * @param visibility
     */
    public void setRightVisibility(int visibility) {
        if (mRightType == RIGHT_TYPE_TEXT) {
            mRightTextView.setVisibility(visibility);
        } else if (mRightType == RIGHT_TYPE_RESOURCE) {
            mRightButton.setVisibility(visibility);
        } else {
            // do nothing.
        }
    }

    /**
     * 设置XML根布局的背景.
     *
     * @param res
     */
    public void setRootBackgroundResource(int res) {
        mRoot.setBackgroundResource(res);
    }

    /**
     * 右侧按钮跟文字替换
     *
     * @param type
     */
    public void switchRight(@TitleBarRightType int type) {
        if (type == RIGHT_TYPE_TEXT) {
            mRightButton.setVisibility(GONE);
            mRightTextView.setVisibility(VISIBLE);
            mRightType = RIGHT_TYPE_TEXT;
        } else if (type == RIGHT_TYPE_RESOURCE) {
            mRightTextView.setVisibility(GONE);
            mRightButton.setVisibility(VISIBLE);
            mRightType = RIGHT_TYPE_RESOURCE;
        }
    }

    public void showLeftButton() {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(VISIBLE);
        }
    }

    public void hideLeftButton() {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧文字
     *
     * @param text
     */
    public void initRightText(@StringRes int text) {
        mRightTextView.setText(text);
    }

    @OnClick(R2.id.tb_left_button)
    void onLeftButtonClicked(View v) {
        onLeftClick(v);
    }

    @OnClick(R2.id.tb_right_text)
    void onRightTextClick(View v) {
        onRightClick(v, mRightType);
    }

    @OnClick(R2.id.tb_right_button)
    void onRightButtonClicked(View v) {
        onRightClick(v, mRightType);
    }

    @OnClick(R2.id.tb_left_text)
    void onLeftTextClicked(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onLeftClick(v);
        }
    }
}
