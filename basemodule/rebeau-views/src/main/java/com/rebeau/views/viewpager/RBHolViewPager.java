package com.rebeau.views.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager
 * 拦截并处理横向滑动事件
 * 其他事件分发给子类view执行
 */

public class RBHolViewPager extends ViewPager {

    private int startX;
    private int startY;

    public RBHolViewPager(Context context) {
        super(context);
    }

    public RBHolViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                return (Math.abs(disX) > Math.abs(disY));
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
