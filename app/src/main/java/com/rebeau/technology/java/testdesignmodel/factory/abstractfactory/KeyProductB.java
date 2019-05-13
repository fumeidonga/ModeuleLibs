package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public class KeyProductB extends AbstractKeyProduct {
    @Override
    public KeyProductB build() {

        name = "key product b";
        price = "100";
        return this;
    }
}
