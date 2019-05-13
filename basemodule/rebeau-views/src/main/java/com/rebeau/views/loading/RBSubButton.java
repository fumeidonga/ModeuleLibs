package com.rebeau.views.loading;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import com.rebeau.base.utils.RBScreenUtil;
import com.rebeau.views.R;


/**
 * 次按钮
 *
 * @使用方式
 * 1、高度务必设定具体的值！高度务必设定具体的值！高度务必设定具体的值！important！！！
 * 2、不需要设定背景
 * 3、不需要设定字体颜色
 * 4、其它操作同button
 *
 * @特性
 * 1、支持常规／按下状态的背景及字体颜色的切换
 * 2、按钮左右半圆形显示
 * 3、样式见次按钮
 *
 */
public class RBSubButton extends RBBaseButton {
    public RBSubButton(Context context) {
        super(context);
    }

    public RBSubButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RBSubButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSetBackground(Context context, int height) {
        // 设置不同状态下背景.
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(context.getResources().getColor(android.R.color.transparent));
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(RBScreenUtil.dpToPx(context, 0.5f), context.getResources().getColor(R.color.rb_ui_app_color_primary));
        normalDrawable.setCornerRadius(height);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(context.getResources().getColor(android.R.color.transparent));
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setStroke(RBScreenUtil.dpToPx(context, 0.5f), context.getResources().getColor(R.color.rb_ui_app_color_primary_dark));
        pressedDrawable.setCornerRadius(height);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        setBackgroundDrawable(stateListDrawable);
    }

    @Override
    protected void onSetTextColor(Context context) {
        // 设置不同状态下字体颜色.
        ColorStateList colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_sub);
        setTextColor(colorStateList);
    }
}
