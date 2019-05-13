package com.rebeau.views.dialog;

import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;

import com.rebeau.base.utils.RBLogUtil;

/**
 * 从下往上弹出
 */
public class BaseButtonDialog extends AbsBaseFrgDialog<BaseButtonDialog, BaseButtonDialog.Builder> {
    @LayoutRes
    public int getLayoutRes(){
        return dvFragmentDialogConfig.mLayoutRes;
    }

    @Override
    public void onBindView(View v) {
        RBLogUtil.dt();
    }

    @Override
    protected BaseButtonDialog build(Builder builder) {
        super.setBuild(builder);
        return this;
    }


    public static class Builder extends AbsBaseFrgDialog.Builder<Builder> {

        public Builder() {
            setmGravity(Gravity.BOTTOM);
        }

        @Override
        public BaseButtonDialog build() {
            return new BaseButtonDialog().build(this);
        }
    }


}
