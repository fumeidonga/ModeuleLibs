package com.rebeau.technology.java.testdesignmodel.lianshi;

import android.text.TextUtils;

import com.rebeau.base.utils.RBLogUtil;


/**
 * author: hrl
 * date: 2019/4/12
 * description: $ 责任链A .
 */
public class LianShiA extends BaseHandlerResult {
    @Override
    public boolean handleResult(String url) {
        RBLogUtil.d("责任链A");
        if(TextUtils.isEmpty(url)) {
            return handleNext(url);
        }
        RBLogUtil.d("责任链A " + url);
        return true;
    }
}
