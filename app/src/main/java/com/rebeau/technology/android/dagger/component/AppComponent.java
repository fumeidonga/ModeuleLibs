package com.rebeau.technology.android.dagger.component;

import android.app.Application;

import com.rebeau.technology.android.dagger.module.AbsApplicationModule;
import com.rebeau.technology.android.dagger.module.ActivityBindingModule;
import com.rebeau.technology.android.dagger.module.ApplicationModule;
import com.rebeau.technology.app.AppApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        AbsApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<AppApplication> {

    Application application();

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).createMouse().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }

//    @Component.OneBuilder
//    abstract class OneBuilder extends AndroidInjector.OneBuilder<AppApplication>{}
}
