package com.rebeau.views.titlebar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * title bar基类.
 *
 */
public abstract class RBBaseTitleBar extends LinearLayout {

    public final static int RIGHT_TYPE_EMPTY = -1;
    public final static int RIGHT_TYPE_TEXT = 1;
    public final static int RIGHT_TYPE_RESOURCE = 2;

    protected OnClickListener mOnClickListener;
    protected OnMoreClickListener mOnMoreClickListener;
    protected boolean mSupportTextTypeFace = true;

    public RBBaseTitleBar(Context context) {
        this(context, null);
    }

    public RBBaseTitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBBaseTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        onInit(context);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        mOnMoreClickListener = onMoreClickListener;
    }

    /**
     * 初始化view等操作
     * @param context
     */
    public abstract void onInit(Context context);

    /**
     * 设定标题栏的名字
     * @param title
     */
    public abstract void setTitleBarName(String title);

    /**
     * 设置是否需要提醒
     * @param remind
     */
    public abstract void setIsRemind(boolean remind);

    /**
     * 设置是否支持字体，默认支持
     *
     * @param enable
     */
    public void setSupportTextTypeFace(boolean enable) {
        mSupportTextTypeFace = enable;
        onSupportTextTypeFaceChange();
    }

    protected void onSupportTextTypeFaceChange() {
        // do nothing
    }

    public interface OnClickListener {
        /**
         * 点击左侧按钮事件.
         */
        void onLeftClick(View v);

        /**
         * 点击右侧按钮事件.
         */
        void onRightClick(View v, @TitleBarRightType int type);

    }

    public interface OnMoreClickListener {
        /**
         * 点击更多事件.
         */
        void onMoreClick(View v);
    }

    protected void onLeftClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onLeftClick(v);
        }
    }

    protected void onRightClick(View v, @TitleBarRightType int type) {
        if (mOnClickListener != null) {
            mOnClickListener.onRightClick(v,type);
        }
    }

    protected void onMoreClick(View view) {
        if (mOnMoreClickListener != null) {
            mOnMoreClickListener.onMoreClick(view);
        }
    }

    @IntDef({RIGHT_TYPE_TEXT, RIGHT_TYPE_RESOURCE, RIGHT_TYPE_EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TitleBarRightType {
    }
}
