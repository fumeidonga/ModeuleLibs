package com.rebeau.technology.java.testdesignmodel.singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum Singleton implements IMySingleton {

    INSTANCE {
        @Override
        public void doSomething() {
            gson = new GsonBuilder().create();
        }
    };

    Gson gson;

    // 这里隐藏了一个空的私有构造方法
    private Singleton () {}

    public static Singleton getInstance() {
        return Singleton.INSTANCE;
    }

    public Gson getGson(){
        return gson;
    }


}
