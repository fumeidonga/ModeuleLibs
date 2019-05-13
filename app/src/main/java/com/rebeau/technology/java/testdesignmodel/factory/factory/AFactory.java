package com.rebeau.technology.java.testdesignmodel.factory.factory;


import com.rebeau.technology.java.testdesignmodel.module.BiJiBenComputer;
import com.rebeau.technology.java.testdesignmodel.module.Computer;

public class AFactory extends AbstractFactory {
    @Override
    public Computer buildComputer() {
        return new BiJiBenComputer();
    }
}
