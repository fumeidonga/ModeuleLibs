package com.rebeau.technology.java.testdesignmodel.proxy.statics;


import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.technology.java.testdesignmodel.proxy.IStaticProxy;

public class StaticClient implements IStaticProxy {
    @Override
    public void buy() {
        RBLogUtil.d("客户要买海外的东西");
    }

    @Override
    public void pay() {

        RBLogUtil.d("客户pay");
    }
}
