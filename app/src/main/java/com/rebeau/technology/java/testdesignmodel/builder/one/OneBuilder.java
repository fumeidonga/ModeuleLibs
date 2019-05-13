package com.rebeau.technology.java.testdesignmodel.builder.one;

import com.rebeau.technology.java.testdesignmodel.module.Computer;

public abstract class OneBuilder {

    /**
     * 初始化一个空的电脑，没有cpu，没有主板等
     */
    public abstract void init();
    /**
     * 创建cpu
     */
    public abstract void buildKeHu();
    /**
     * 创建cpu
     */
    public abstract void buildCpu();

    /**
     * 创建主板
     */
    public abstract void buildZhuBan();

    /**
     * 创建硬盘
     */
    public abstract void buildYingPan();

    /**
     * 创建电脑
     */
    public abstract Computer buildComputer();
}
