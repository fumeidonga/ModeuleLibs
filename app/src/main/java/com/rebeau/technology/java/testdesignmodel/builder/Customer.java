package com.rebeau.technology.java.testdesignmodel.builder;

import com.rebeau.technology.java.testdesignmodel.module.Computer;

/**
 * 顾客，要买电脑
 */
public class Customer {

    Boss boss;

    /**
     * 1. 顾客来到了电脑城，看到了一家比较顺眼的电脑店，然后走了进去
     */
    public Customer(){
        boss = new Boss();
    }

    /**
     * 2. 然后告诉老板，要买一台组装机， cpu: xxx   zhuban：xxx    yingpan :xxx
     * @param cpu
     * @param zhuban
     * @param yingpan
     * @return
     */
    public Computer buyComputer(int i, String cpu, String zhuban, String yingpan){
        return boss.buyComputer(i, cpu, zhuban, yingpan);
    }

    /**
     * 2. 然后告诉老板，要买一台品牌组装机，
     * @return
     */
    public Computer buyComputer(){
        return boss.buyComputer();
    }
}
