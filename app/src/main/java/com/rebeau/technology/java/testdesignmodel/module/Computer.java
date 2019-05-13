package com.rebeau.technology.java.testdesignmodel.module;

/**
 * 假设电脑包含： cpu, 主板， 硬盘
 */
public class Computer {

    public String cpu;

    public String zhuban;

    public String yingpan;

    public String kehu;

    @Override
    public String toString() {
        return kehu + "的电脑 : Computer{" +
                "cpu='" + cpu + '\'' +
                ", zhuban='" + zhuban + '\'' +
                ", yingpan='" + yingpan + '\'' +
                '}';
    }
}
