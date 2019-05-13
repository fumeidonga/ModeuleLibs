package com.rebeau.views.night;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * 夜间模式遮罩布局Layout
 * Created by Administrator on 2018/4/28.
 */

public class RBNightShadowFrameLayout extends FrameLayout {
    private Paint paint;
    private boolean invalidateChildIn = false;

    public RBNightShadowFrameLayout(Context paramContext) {
        super(paramContext);
        init();
    }

    public RBNightShadowFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RBNightShadowFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setFlags(1);
        this.paint.setColor(Color.argb(Math.round(127.0F), 0, 0, 0));
    }

    public void postInvalidateAnimation(boolean paramBoolean) {
        this.invalidateChildIn = paramBoolean;
        if (!this.invalidateChildIn) {
            if (getChildCount() > 0) {
                ViewCompat.postInvalidateOnAnimation(getChildAt(0));
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void valueAnimator(boolean paramBoolean) {
        this.invalidateChildIn = paramBoolean;
        if(invalidateChildIn) {
            clearAnimation();
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
            animator.setDuration(200);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
            animator.setDuration(135);
            animator.start();
        }
    }

    @Override
    protected void dispatchDraw(Canvas paramCanvas) {
        super.dispatchDraw(paramCanvas);
        if (AppNightModeObservable.getInstance().isNightMode()) {
            drawRect(paramCanvas);
        } else {
            if(invalidateChildIn) {
                drawRect(paramCanvas);
            }
        }
    }

    private void drawRect(Canvas paramCanvas){
        Paint localPaint;
        localPaint = this.paint;
        localPaint.setColor(Color.argb(Math.round(255.0F * 0.5F), 0, 0, 0));
        paramCanvas.drawRect(0.0F, 0.0F, getWidth(), getHeight(), this.paint);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] paramArrayOfInt, Rect paramRect) {
        if (this.invalidateChildIn) {
            return null;
        }
        return super.invalidateChildInParent(paramArrayOfInt, paramRect);
    }
}