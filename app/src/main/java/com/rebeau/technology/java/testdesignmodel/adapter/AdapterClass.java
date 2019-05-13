package com.rebeau.technology.java.testdesignmodel.adapter;


import com.rebeau.base.utils.RBLogUtil;

/**
 * 类适配器
 */
public class AdapterClass extends ThreeAdaptee implements ITarget {

    public void charge(){
        RBLogUtil.dt();
    }
}
