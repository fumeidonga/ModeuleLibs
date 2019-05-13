package com.rebeau.technology.java.testdesignmodel.builder;

import com.rebeau.technology.java.testdesignmodel.module.Computer;

public abstract class Builder {

    /**
     * 初始化一个空的电脑，没有cpu，没有主板等
     */
    public abstract void init();
    /**
     * 创建cpu
     */
    public abstract void buildKeHu(String cpu);
    /**
     * 创建cpu
     */
    public abstract void buildCpu(String cpu);

    /**
     * 创建主板
     */
    public abstract void buildZhuBan(String zhuban);

    /**
     * 创建硬盘
     */
    public abstract void buildYingPan(String yingpan);

    /**
     * 创建电脑
     */
    public abstract Computer buildComputer();
}
