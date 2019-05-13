package com.rebeau.views.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.Button;

import com.rebeau.base.utils.RBScreenUtil;
import com.rebeau.views.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 按钮基类
 *
 */
public abstract class RBBaseButton extends Button {
    private static final int DEFAULT_HEIGHT_DIP = 40;
    private static final String ATTR_LAYOUT_HEIGHT = "layout_height";
    private static final String INVALID_HEIGHT = "-1";
    private static final String UNIT_SIZE_DIP = "dip";
    private static final String UNIT_SIZE_DP = "dp";
    private static final String UNIT_SIZE_PX = "px";

    public static final int STYLE_ONE = 1;
    public static final int STYLE_TWO = 2;
    public static final int STYLE_THREE = 3;
    public static final int STYLE_FOUR = 4;

    @IntDef({STYLE_ONE, STYLE_TWO, STYLE_THREE, STYLE_FOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonStyle {}

    @ButtonStyle
    protected int style;

    protected int strokeWidth;

    protected int height;

    public RBBaseButton(Context context) {
        this(context, null);
    }

    public RBBaseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBBaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ButtonStyle, defStyleAttr, 0);
        style = a.getInt(R.styleable.ButtonStyle_style_type, STYLE_ONE);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ButtonStyle_stroke_width, RBScreenUtil.dpToPx(context, 1));
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        height = getHeight(context, attrs, defStyleAttr);
        onSetBackground(context, height);
        onSetTextColor(context);
    }

    /**
     * 设置样式（支持样式变更）
     *
     * @param style
     */
    public void setStyleType(@ButtonStyle int style) {
        this.style = style;
        onSetBackground(getContext(), height);
        onSetTextColor(getContext());
    }

    /**
     * 该接口用于子类设定按钮的背景
     * @param context   上下文
     * @param height    按钮的高度
     */
    protected abstract void onSetBackground(Context context, int height);

    /**
     * 该接口用于子类设定按钮的字体颜色
     * @param context
     */
    protected abstract void onSetTextColor(Context context);

    private int getHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            String layoutHeightValue = null;
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attrName = attrs.getAttributeName(i);
                if (ATTR_LAYOUT_HEIGHT.equals(attrName)) {
                    layoutHeightValue = attrs.getAttributeValue(i);
                }
            }

            if (!(INVALID_HEIGHT).equals(layoutHeightValue)) {
                if (layoutHeightValue.endsWith(UNIT_SIZE_DP)) {
                    String heightStr = layoutHeightValue.substring(0, layoutHeightValue.length() - 2);
                    return RBScreenUtil.dpToPx(context, Float.valueOf(heightStr));
                } else if (layoutHeightValue.endsWith(UNIT_SIZE_DIP)) {
                    String heightStr = layoutHeightValue.substring(0, layoutHeightValue.length() - 3);
                    return RBScreenUtil.dpToPx(context, Float.valueOf(heightStr));
                } else if (layoutHeightValue.endsWith(UNIT_SIZE_PX)) {
                    return Integer.valueOf(layoutHeightValue);
                }
            }
        }

        return RBScreenUtil.dpToPx(context, DEFAULT_HEIGHT_DIP);
    }
}
