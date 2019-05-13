package com.rebeau.views.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 *
 */

public abstract class RBBaseEmptyDataView extends LinearLayout {

    public RBBaseEmptyDataView(Context context) {
        this(context, null);
    }

    public RBBaseEmptyDataView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBBaseEmptyDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit(context);
    }

    /**
     * 初始化view
     * @param context
     */
    protected abstract void onInit(Context context);

    public abstract void setEmptyDataText(String text);
}
