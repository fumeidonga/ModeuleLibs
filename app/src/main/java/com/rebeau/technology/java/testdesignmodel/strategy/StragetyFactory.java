package com.rebeau.technology.java.testdesignmodel.strategy;

public enum StragetyFactory {

    INSTANCE{

    };

    public static StragetyFactory getInstance(){
        return StragetyFactory.INSTANCE;
    }


    public IDVStrategy strategy(int strategy){
        switch (strategy) {
            case 0:
                return new DVStrategyA();
            case 1:
                return new DVStrategyB();
            default:
                return new DVStrategyA();

        }
    }
}
