package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public class AFactory1 extends AbsFactory {

    @Override
    public AbstractMouseProduct createMouse() {
        return new MouseProductA();
    }

    @Override
    public AbstractKeyProduct createKey() {
        return new KeyProductA();
    }
}
