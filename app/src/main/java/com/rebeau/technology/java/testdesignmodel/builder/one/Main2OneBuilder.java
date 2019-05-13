package com.rebeau.technology.java.testdesignmodel.builder.one;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.technology.java.testdesignmodel.module.Computer;

public class Main2OneBuilder extends OneBuilder {

    Computer computer;

    public Main2OneBuilder() {
        init();
    }

    @Override
    public void init() {
        computer = new Computer();
    }

    @Override
    public void buildKeHu() {
        computer.kehu = "kehu";
    }

    @Override
    public void buildCpu() {
        computer.cpu = "cupu";
    }

    @Override
    public void buildZhuBan() {
        computer.zhuban = "zhuban";
    }

    @Override
    public void buildYingPan() {
        computer.yingpan = "yingpan";
    }

    @Override
    public Computer buildComputer() {
        RBLogUtil.d(computer);
        return computer;
    }
}
