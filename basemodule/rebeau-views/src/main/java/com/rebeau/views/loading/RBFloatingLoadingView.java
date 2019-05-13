package com.rebeau.views.loading;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.rebeau.views.R;

/**
 * RBFloatingLoadingView
 * 模态（悬浮）状态的loading
 *
 */
public class RBFloatingLoadingView extends RelativeLayout {

    private LottieAnimationView lottieAnimationView;

    public RBFloatingLoadingView(Context context) {
        this(context, null);
    }

    public RBFloatingLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBFloatingLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER);
        setLayoutParams(layoutParams);
        View view = LayoutInflater.from(context).inflate(R.layout.rb_ui_loading_view_main, this);
        lottieAnimationView = (LottieAnimationView)view.findViewById(R.id.rb_ui_loading_view_lotti);
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
