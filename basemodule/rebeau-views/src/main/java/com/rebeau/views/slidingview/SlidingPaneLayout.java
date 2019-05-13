package com.rebeau.views.slidingview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.rebeau.views.R;
import com.rebeau.base.utils.RBScreenUtil;


/**
 * @Scroller 是一个专门用于处理滚动效果的工具类，可能在大多数情况下，我们直接使用Scroller的场景并不多，
 * 但是很多大家所熟知的控件在内部都是使用Scroller来实现的，如ViewPager、ListView等
 * @VelocityTracker 主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。
 * 用addMovement(MotionEvent)函数将Motion event加入到VelocityTracker类实例中.
 * 你可以使用getXVelocity() 或getXVelocity()获得横向和竖向的速率到速率时，但是使用它们之前请先
 * 调用computeCurrentVelocity(int)来初始化速率的单位
 */
public class SlidingPaneLayout extends FrameLayout {
    private Activity mActivity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private SlidingPaneListener mSlidingPaneListener;
    private boolean isTranslucent=true;

    private final int VELOCITY_THRESHOLD = 2000;
    private final int SCROLLER_X_THRESHOLD = 3;

    private int mLastInterceptX, mLastInterceptY;
    private int mLastTouchX, mLastTouchY;
    private int mDownX;

    private float mVelocityX, mVelocityY;

    private int mSwipeBackThreshold;

    private boolean mCanSwipeBack = true;
    private boolean mConsumed = false;

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化滑动返回的 有效区域
     *
     * @param context
     */
    private void initView(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeBackThreshold = context.getResources().getDimensionPixelSize(R.dimen.modulecommon_dimen_swipe_back);
        mScroller = new Scroller(context);
    }

    /**
     * 绑定Activity，替换decorView
     *
     * @param activity
     */
    public void bindActivity(Activity activity) {
        mActivity = activity;
        try {
            ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            View child = decorView.getChildAt(0);
            decorView.removeView(child);
            addView(child);
            decorView.addView(this, 0);
        }catch (Exception e) {
            Log.e("SlidingPaneLayout", e.toString());
        }
    }


    public void setSlidingPaneListener(SlidingPaneListener slidingPaneListener) {
        mSlidingPaneListener = slidingPaneListener;
    }


    public void setTranslucent(boolean isTranslucent){
        this.isTranslucent=isTranslucent;
    }
    /**
     * 设置是否关闭滑动返回
     *
     * @param canSwipeBack
     */
    public void setCloseSlidingPane(boolean canSwipeBack) {
        mCanSwipeBack = !canSwipeBack;
        if (!mCanSwipeBack) {
            scrollTo(0, 0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = x;
                mLastInterceptY = y;
                mDownX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                if (intercept(deltaX, deltaY)) {
                    if(isTranslucent){
                        return true;
                    }else{
                        RBScreenUtil.convertActivityToTranslucent(mActivity);
                        isTranslucent=true;
                    }
                }
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                mLastInterceptX = mLastInterceptY = 0;
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mCanSwipeBack || mDownX > mSwipeBackThreshold) {
            return super.onTouchEvent(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
            mVelocityTracker.addMovement(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = x;
                mLastTouchY = y;
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastTouchX;
                int deltaY = y - mLastTouchY;

                if (!mConsumed && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mConsumed = true;
                }

                if (mConsumed) {
                    int rightMovedX = mLastTouchX - (int) ev.getX();
                    // 左侧即将滑出屏幕
                    if (getScrollX() + rightMovedX >= 0) {
                        scrollTo(0, 0);
                    } else {
                        scrollBy(rightMovedX, 0);
//                        if(!isTrans){
//                            isTrans=true;
//                            RBScreenUtil.convertActivityToTranslucent(mActivity);
//                        }
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;

                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                mVelocityX = mVelocityTracker.getXVelocity();
                mVelocityY = Math.abs(mVelocityTracker.getYVelocity());
                break;
            case MotionEvent.ACTION_UP:
                mLastTouchX = mLastTouchY = 0;
                // 根据手指释放时的位置或者瞬间滑动的速度决定回弹还是关闭
                if (mConsumed && scrollForward()) {
                    scrollClose();
                } else {
                    scrollBack();
                }
                mConsumed = false;
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            if (mSlidingPaneListener != null) {
                mSlidingPaneListener.onSlidingPaneClosed();
            }
            mActivity.finish();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    /**
     * 滑动关闭
     */
    private void scrollClose() {
        int startX = getScrollX();
        int dx = -getScrollX() - getWidth();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    /**
     * 拦截判定
     *
     * @param deltaX
     * @param deltaY
     * @return 为true的条件：1.支持滑动返回 2. 在点击区域范围内 3. 滑动的趋势正确
     */
    private boolean intercept(int deltaX, int deltaY) {
        if (!mCanSwipeBack || mDownX > mSwipeBackThreshold) {
            return false;
        }

        if (deltaX <= 0 || Math.abs(deltaX) <= Math.abs(deltaY)) {
            return false;
        }

        return true;
    }

    /**
     * 滑动方向判定
     *
     * @return 为true的条件：滑动的区域超过阈值或者滑动的速度超过阈值
     */
    private boolean scrollForward() {
        if (-getScrollX() > getWidth() / SCROLLER_X_THRESHOLD) {
            return true;
        }
        if (mVelocityX > 0 && mVelocityX > mVelocityY && mVelocityX > VELOCITY_THRESHOLD) {
            return true;
        }

        return false;
    }


    public interface SlidingPaneListener {
        /**
         * 滑动返回动画完成后的回调
         */
        void onSlidingPaneClosed();
        void onSlidingPaneOpen();
    }
}
