package com.rebeau.technology.java.testdesignmodel.proxy.statics;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.technology.java.testdesignmodel.proxy.IStaticProxy;

public class StaticProxy implements IStaticProxy {
    IStaticProxy client;

    public StaticProxy(IStaticProxy client) {
        this.client = client;
    }

    @Override
    public void buy() {
        RBLogUtil.d("这里是代购，可以买海外的东西");
        client.buy();

    }

    @Override
    public void pay() {

        RBLogUtil.d("这里是代购 pay");
    }
}
