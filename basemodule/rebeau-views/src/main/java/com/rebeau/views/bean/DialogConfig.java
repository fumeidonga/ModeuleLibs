package com.rebeau.views.bean;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;

import com.rebeau.views.R;


public class DialogConfig {

    public static class DVFragmentDialogConfig{

        /**
         * 设置幕布，也就是本dialog的背景层
         * dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的,透明的 ，1.0f时候，背景全部变黑
         * 如果要达到背景全部变暗的效果，需要设置  dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
         * ，否则，背景无效果
         */
        public static final float DIM_AMOUNT = 0.5f;

        /**
         * 设置透明度，主要设置的是dialog自身的透明度
         * alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明，自身不可见
         */
        public static final float ALPHA = 1.0f;

        /**
         * 点击框框外面是否可取消
         */
        public boolean mIsCancelOutside = true;

        @LayoutRes
        public int mLayoutRes;

        /**
         * dialog的主题
         */
        public int mTheme = R.style.dv_base_fragment_dialog_slide;

        public String mFragmentTag = "base_bottom_dialog";

        public float mDimAmount = DIM_AMOUNT;

        public float mAlpha = ALPHA;

        public int mHeight = -1;

        public int mWidth = -2;

        public int mGravity = Gravity.CENTER;

        public FragmentManager mFragmentManager;
    }

}
