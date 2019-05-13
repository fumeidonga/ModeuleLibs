package com.rebeau.technology.java.testdesignmodel.factory.abstractfactory;

public abstract class AbstractProduct {

    public String name;
    public String price;

    public abstract AbstractProduct build();

    @Override
    public String toString() {
        return "AbstractProduct{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
