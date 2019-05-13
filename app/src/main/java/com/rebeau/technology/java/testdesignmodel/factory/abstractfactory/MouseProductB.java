package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public class MouseProductB extends AbstractMouseProduct {
    @Override
    public MouseProductB build() {

        name = "mouse product a";
        price = "100";
        return this;
    }
}
