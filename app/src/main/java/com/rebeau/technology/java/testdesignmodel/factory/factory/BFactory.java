package com.rebeau.technology.java.testdesignmodel.factory.factory;

import com.rebeau.technology.java.testdesignmodel.module.Computer;
import com.rebeau.technology.java.testdesignmodel.module.TaiShiJiComputer;

public class BFactory extends AbstractFactory {
    @Override
    public Computer buildComputer() {
        return new TaiShiJiComputer();
    }
}
