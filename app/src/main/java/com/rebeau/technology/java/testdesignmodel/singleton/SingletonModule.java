package com.rebeau.technology.java.testdesignmodel.singleton;

public class SingletonModule {


    private static volatile SingletonModule mSingletonModule;

    private SingletonModule() {
    }

    public static SingletonModule getInstance(){

        if(mSingletonModule == null) {
            synchronized(SingletonModule.class){
                if(mSingletonModule == null) {
                    mSingletonModule = new SingletonModule();
                }
            }
        }
        return mSingletonModule;
    }

    public void testClass() throws Exception{
        /*Class classd = SingletonModule.class;
        Constructor constructor = classd.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        SingletonModule singletonModule =  (SingletonModule)constructor.newInstance(null);*/
    }


}
