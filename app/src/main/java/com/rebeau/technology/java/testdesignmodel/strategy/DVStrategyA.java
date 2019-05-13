package com.rebeau.technology.java.testdesignmodel.strategy;


import com.rebeau.base.utils.RBLogUtil;

public class DVStrategyA implements IDVStrategy {
    @Override
    public void strategy() {
        RBLogUtil.dt("a");
    }
}
