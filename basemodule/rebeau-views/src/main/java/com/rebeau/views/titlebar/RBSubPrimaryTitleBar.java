package com.rebeau.views.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rebeau.base.utils.RBStatusBarUtil;
import com.rebeau.views.R;
import com.rebeau.views.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二级页面title bar.
 * 非沉浸式（通用情况下）
 * 但提供沉浸式的接口.
 */
public class RBSubPrimaryTitleBar extends RBBaseTitleBar {

    public final static int RIGHT_TYPE_EMPTY = -1;
    public final static int RIGHT_TYPE_TEXT = 1;
    public final static int RIGHT_TYPE_RESOURCE = 2;

    @BindView(R2.id.tb_root_layout)
    LinearLayout mRoot;
    @BindView(R2.id.tb_status_bar)
    View mStatusBar;
    @BindView(R2.id.tb_center_name)
    TextView mCenterName;
    @BindView(R2.id.tb_right_text)
    TextView mRightTextView;
    @BindView(R2.id.tb_right_button)
    ImageButton mRightButton;
    @BindView(R2.id.tb_navi_back)
    ImageButton mLeftButton;

    protected int mRightType;

    public RBSubPrimaryTitleBar(Context context) {
        super(context);
    }

    public RBSubPrimaryTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RBSubPrimaryTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow();
    }

    @Override
    public void onInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_title_bar_sub_primary_view, this);
        ButterKnife.bind(this, view);
        mRightType = RIGHT_TYPE_EMPTY;
    }

    @Override
    public void setTitleBarName(String title) {
        mCenterName.setText(title);
    }

    @Override
    public void setIsRemind(boolean remind) {
        // do nothing.
    }

    protected void attachedToWindow() {
        Activity activity = (Activity) getContext();
        RBStatusBarUtil.initSubStatusBar(activity, mStatusBar, activity.getResources().getColor(android.R.color.white));
    }

    @Override
    protected void onSupportTextTypeFaceChange() {
        if (!mSupportTextTypeFace) {
            int style = Typeface.BOLD;
            mCenterName.setTypeface(Typeface.defaultFromStyle(style));
        }
    }

    /**
     * 隐藏或者显示标题（特殊需求：个人主页）
     *
     * @param visible
     */
    public void setTitleBarNameVisible(int visible) {
        mCenterName.setVisibility(visible);
    }

    /**
     * 特殊需求：微信通过schema进入书评出现title bar没有顶到全屏
     *
     * @param activity
     */
    public void setTitleBarFullSrceen(Activity activity) {
        RBStatusBarUtil.initSubStatusBar(activity, mStatusBar, activity.getResources().getColor(android.R.color.white));
    }

    /**
     * 设置右侧文字.
     *
     * @param text
     */
    public void setRightText(String text) {
        mRightTextView.setText(text);
        mRightTextView.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.GONE);
        mRightType = RIGHT_TYPE_TEXT;
    }

    /**
     * 设置右侧资源.
     *
     * @param resouceId
     */
    public void setRightResource(int resouceId) {
        mRightButton.setBackgroundResource(resouceId);
        mRightButton.setVisibility(View.VISIBLE);
        mRightTextView.setVisibility(View.GONE);
        mRightType = RIGHT_TYPE_RESOURCE;
    }

    /**
     * 设置左侧资源.
     *
     * @param resouceId
     */
    public void setLeftResource(@DrawableRes int resouceId) {
        mLeftButton.setBackgroundResource(resouceId);
    }

    /**
     * 获取右侧文字信息.
     *
     * @return
     */
    public String getRightText() {
        return mRightTextView.getText().toString();
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

    @OnClick(R2.id.tb_navi_back)
    void onLeftButtonClick(View v) {
        onLeftClick(v);
    }

    @OnClick(R2.id.tb_right_text)
    void onRightTextClick(View v) {
        onRightClick(v, mRightType);
    }

    @OnClick(R2.id.tb_right_button)
    void onRightButtonClick(View v) {
        onRightClick(v, mRightType);
    }
}
