package com.rebeau.views.loading;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.rebeau.views.R;


/**
 * RBInnerLoadingView
 * 页面内嵌的loading
 */
public class RBInnerLoadingView extends LinearLayout {

    private LottieAnimationView lottieAnimationView;

    public RBInnerLoadingView(Context context) {
        this(context, null);
    }

    public RBInnerLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBInnerLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_loading_view_main_inner, this);
        lottieAnimationView = (LottieAnimationView)view.findViewById(R.id.rb_ui_loading_view);
    }

    public void controlAnimation(boolean open) {
        if (lottieAnimationView != null) {
            lottieAnimationView.setRepeatCount(open ? LottieDrawable.INFINITE : 0);
            if (open) {
                lottieAnimationView.playAnimation();
            } else {
                lottieAnimationView.cancelAnimation();
            }
        }
    }
}
