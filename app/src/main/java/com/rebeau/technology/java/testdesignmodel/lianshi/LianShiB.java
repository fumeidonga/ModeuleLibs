package com.rebeau.technology.java.testdesignmodel.lianshi;

import android.text.TextUtils;

import com.rebeau.base.utils.RBLogUtil;

/**
 * author: huruilong
 * date: 2019/4/12
 * description: ${责任链B} .
 */
public class LianShiB extends BaseHandlerResult {
    @Override
    public boolean handleResult(String url) {
        RBLogUtil.d("责任链B");
        if(TextUtils.isEmpty(url)) {
            return handleNext(url);
        }
        RBLogUtil.d("责任链B " + url);
        return true;
    }
}
