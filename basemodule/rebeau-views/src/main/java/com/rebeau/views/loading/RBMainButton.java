package com.rebeau.views.loading;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import com.rebeau.views.R;


/**
 * 主按钮
 *
 * @使用方式
 * 1、高度务必设定具体的值！高度务必设定具体的值！高度务必设定具体的值！important！！！
 * 2、不需要设定背景
 * 3、不需要设定字体颜色
 * 4、其它操作同button
 *
 * @特性
 * 1、支持常规／按下／disable状态的背景及字体颜色的切换
 * 2、按钮左右半圆形显示
 *
 */
public class RBMainButton extends RBBaseButton {

    public RBMainButton(Context context) {
        super(context);
    }

    public RBMainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RBMainButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSetBackground(Context context, int height) {
        switch (style) {
            case STYLE_ONE:
                setOneStyleBackground(context, height);
                break;
            case STYLE_TWO:
                setTwoStyleBackground(context, height);
                break;
            case STYLE_THREE:
                setThreeStyleBackground(context, height);
                break;
            case STYLE_FOUR:
                setFourStyleBackground(context, height);
                break;
            default:
                setOneStyleBackground(context, height);
                break;
        }
    }

    void setOneStyleBackground(Context context, int height) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        int[] normalColors = {0xFFFFB028, 0xFFFF8D00};
        GradientDrawable normalDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, normalColors);
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setCornerRadius(height);

        int[] pressedColors = {0xB3FFB028, 0xB3FF8D00};
        GradientDrawable pressedDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, pressedColors);
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setCornerRadius(height);

        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setColor(0xFFF5F5F5);
        disableDrawable.setShape(GradientDrawable.RECTANGLE);
        disableDrawable.setCornerRadius(height);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        setBackground(stateListDrawable);
    }

    void setTwoStyleBackground(Context context, int height) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(0xFFFFB028);
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setCornerRadius(height);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(0xB3FFB028);
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setCornerRadius(height);

        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setColor(0xFFF5F5F5);
        disableDrawable.setShape(GradientDrawable.RECTANGLE);
        disableDrawable.setCornerRadius(height);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        setBackground(stateListDrawable);
    }

    void setThreeStyleBackground(Context context, int height) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(0x00000000);
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(strokeWidth, 0xFFFFA200);
        normalDrawable.setCornerRadius(height);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(0x00000000);
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setStroke(strokeWidth, 0xB3FFA200);
        pressedDrawable.setCornerRadius(height);

        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setColor(0xFFF5F5F5);
        disableDrawable.setShape(GradientDrawable.RECTANGLE);
        disableDrawable.setCornerRadius(height);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        setBackground(stateListDrawable);
    }

    void setFourStyleBackground(Context context, int height) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(0x00000000);
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(strokeWidth, 0xFFDDDDDD);
        normalDrawable.setCornerRadius(height);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(0x00000000);
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setStroke(strokeWidth, 0xB3DDDDDD);
        pressedDrawable.setCornerRadius(height);

        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setColor(0xFFF5F5F5);
        disableDrawable.setShape(GradientDrawable.RECTANGLE);
        disableDrawable.setCornerRadius(height);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        setBackground(stateListDrawable);
    }

    @Override
    protected void onSetTextColor(Context context) {
        ColorStateList colorStateList;
        switch (style) {
            case STYLE_ONE:
                colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_one);
                break;
            case STYLE_TWO:
                colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_two);
                break;
            case STYLE_THREE:
                colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_three);
                break;
            case STYLE_FOUR:
                colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_four);
                break;
            default:
                colorStateList = context.getResources().getColorStateList(R.color.rb_ui_button_selector_text_color_one);
                break;
        }
        setTextColor(colorStateList);
    }
}