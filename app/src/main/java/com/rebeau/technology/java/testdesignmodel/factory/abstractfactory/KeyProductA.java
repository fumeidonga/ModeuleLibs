package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public class KeyProductA extends AbstractKeyProduct {
    @Override
    public KeyProductA build() {
        name = "key product a";
        price = "100";
        return this;
    }
}
