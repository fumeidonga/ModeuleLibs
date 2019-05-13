package com.rebeau.technology.java.testdesignmodel.adapter;

/**
 * 对象适配器
 * 通过在内部包装一个Adaptee对象，把源对象接口转换成目标接口
 */
public class Adapter implements ITarget {

    IAdaptee iAdaptee;

    public void setIAdaptee(IAdaptee iAdaptee) {
        this.iAdaptee = iAdaptee;
    }

    @Override
    public void charge() {
        iAdaptee.charge();
    }
}
