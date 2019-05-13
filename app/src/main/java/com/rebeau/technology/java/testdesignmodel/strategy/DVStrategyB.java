package com.rebeau.technology.java.testdesignmodel.strategy;

import com.rebeau.base.utils.RBLogUtil;

public class DVStrategyB implements IDVStrategy {
    @Override
    public void strategy() {

        RBLogUtil.dt("b");
    }
}
