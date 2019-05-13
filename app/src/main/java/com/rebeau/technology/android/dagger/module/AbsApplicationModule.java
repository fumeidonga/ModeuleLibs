package com.rebeau.technology.android.dagger.module;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AbsApplicationModule {
    /**
     * @Binds和@Provides最大区别就是@Binds只能修饰抽象方法，比如说当A1类继承自A，
     * 而在当前的依赖图中可以提供A1的对象(如A1已经可以通过构造方法注入到Component中)，
     * 而在被注入类中需要A的对象，那么就可以定义Bind的抽象方法来将A1作为A的对象注入。
     * 再以上面AppComponent为例，Component实例化中通过Builder可以获得Application的对象，
     * 而如果依赖图中需要context，就可以提供给他们这个Application对象*/
    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);

    /*@Provides
    public Context providesContext(Application application) {
        return application;
    }*/

}
