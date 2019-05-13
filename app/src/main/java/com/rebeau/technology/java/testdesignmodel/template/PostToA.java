package com.rebeau.technology.java.testdesignmodel.template;

public class PostToA extends ABSKuaiDi {
    //联系收货，实现父类的抽象方法
    @Override
    protected void call() {
        System.out.println("联系A先生并送到门口");
    }
}
