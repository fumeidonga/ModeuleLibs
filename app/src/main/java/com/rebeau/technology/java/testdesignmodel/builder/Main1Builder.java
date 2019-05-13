package com.rebeau.technology.java.testdesignmodel.builder;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.technology.java.testdesignmodel.module.Computer;

public class Main1Builder extends Builder {

    Computer computer;

    public Main1Builder() {
        init();
    }

    @Override
    public void init() {
        computer = new Computer();
    }

    @Override
    public void buildKeHu(String cpu) {
        computer.kehu = cpu;
    }

    @Override
    public void buildCpu(String cpu) {
        computer.cpu = cpu;
    }

    @Override
    public void buildZhuBan(String zhuban) {
        computer.zhuban = zhuban;
    }

    @Override
    public void buildYingPan(String yingpan) {
        computer.yingpan = yingpan;

    }


    @Override
    public Computer buildComputer() {
        RBLogUtil.d(computer);
        return computer;
    }
}
