package com.rebeau.technology.java.testdesignmodel.adapter;


import com.rebeau.base.utils.RBLogUtil;

public class ThreeAdaptee implements IAdaptee {
    @Override
    public void charge() {
        RBLogUtil.dt("这是 3 孔的插座");
    }
}
