package com.rebeau.views.viewgroup;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBScreenUtil;

/**
 * 拦截滑动事件，滑动时不触发点击、可见性检测
 */
public class AdLayout extends FrameLayout {

    private boolean mScrolling;
    private float touchDownX;
    private float touchDownY;
    float upY = 0;
    float upX = 0;

    /** 是否滑动过程中是否触发广告 true 触发广告，不拦截；   false 进行拦截，滑动不触发点击 **/
    private boolean mTriggerAdInScrollingEnable = true;

    public AdLayout(Context context) {
        super(context);
    }

    public AdLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置是否在滑动过程中触发广告
     *
     * @param enable
     */
    public void setTriggerAdInScrollingEnable(boolean enable) {
        this.mTriggerAdInScrollingEnable = enable;
    }

    //拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isTouchPointInView(this, (int) event.getRawX(), (int) event.getRawY())) {
            RBLogUtil.dt("");
            return true;
        }
        if(mTriggerAdInScrollingEnable) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()
                        || Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isTouchPointInView(this, (int) event.getRawX(), (int) event.getRawY())) {
            RBLogUtil.dt("");
            return true;
        }
        if(mTriggerAdInScrollingEnable) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ////消费触摸事件
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();


                if (touchDownX - upX > RBScreenUtil.dpToPx(getContext(), 40)) {
                    RBLogUtil.dt("right to left slide");
                }
                if (touchDownX - upX < - RBScreenUtil.dpToPx(getContext(), 40)) {
                    RBLogUtil.dt("left to right slide");
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0] + 50;
        int top = location[1] + 15;
        int right = left + view.getMeasuredWidth() - 50;
        int bottom = top + view.getMeasuredHeight() - 15;
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }
        return false;
    }

    /**
     * 可见性区域检测
     *
     * @param view                需要检测的View
     * @param minPercentageViewed 最小百分比 50 百分之50
     *
     * @return 是否满足可见性检测
     */
    private boolean isVisible(final View view, final int minPercentageViewed) {
        if (view == null || view.getVisibility() != View.VISIBLE
                || view.getParent() == null) {
            return false;
        }
        Rect mClipRect = new Rect();

        if (!view.getGlobalVisibleRect(mClipRect)) {
            // Not visible
            return false;
        }

        final long visibleViewArea = (long) mClipRect.height()
                * mClipRect.width();
        final long totalViewArea = (long) view.getHeight() * view.getWidth();

        if (totalViewArea <= 0) {
            return false;
        }

        return 100 * visibleViewArea >= minPercentageViewed * totalViewArea;
    }
}
