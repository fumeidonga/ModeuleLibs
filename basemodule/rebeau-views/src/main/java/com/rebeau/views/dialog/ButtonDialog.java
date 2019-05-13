package com.rebeau.views.dialog;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.views.R;

/**
 * 渐变的
 */
public class ButtonDialog extends BaseButtonDialog {

    @LayoutRes
    public int getLayoutRes(){
        return R.layout.control_pic_popupwindow;
    }
    @Override
    public void onBindView(View v) {
        RBLogUtil.dt();
    }

    @Override
    protected ButtonDialog build(BaseButtonDialog.Builder builder) {
        super.setBuild(builder);
        return this;
    }

    public static class Builder extends BaseButtonDialog.Builder {
        public Builder() {
            setmTheme(R.style.dv_base_fragment_dialog_alpha);
        }

        @Override
        public ButtonDialog build() {
            return new ButtonDialog().build(this);
        }
    }
}
