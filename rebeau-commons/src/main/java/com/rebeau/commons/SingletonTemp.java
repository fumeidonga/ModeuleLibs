package com.rebeau.commons;

/**
 *
 * 单例模式使用的几种方式
 * getSingleton3（）
 * getSingleton5（）
 */
public class SingletonTemp {

    //使用volatile关键字防止重排序，因为 new Instance()是一个非原子操作，可能创建一个不完整的实例
    private static volatile SingletonTemp singleton3;

    private SingletonTemp() {
    }

    public static SingletonTemp getSingleton3() {
        // Double-Check idiom
        if (singleton3 == null) {
            synchronized (SingletonTemp.class) {       // 1
                // 只需在第一次创建实例时才同步
                if (singleton3 == null) {       // 2
                    singleton3 = new SingletonTemp();      // 3
                }
            }
        }
        return singleton3;
    }


    // 私有内部类，按需加载，用时加载，也就是延迟加载
    private static class Holder {
        private static SingletonTemp singleton5 = new SingletonTemp();
    }

    public static SingletonTemp getSingleton5() {
        return Holder.singleton5;
    }
}
