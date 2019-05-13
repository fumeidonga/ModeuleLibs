package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public class BFactory1 extends AbsFactory {

    @Override
    public AbstractMouseProduct createMouse() {
        return new MouseProductB();
    }

    @Override
    public AbstractKeyProduct createKey() {
        return new KeyProductB();
    }
}
