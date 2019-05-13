package com.rebeau.technology.android.dagger.module;

import com.rebeau.technology.android.dagger.activitys.method_inject.FourActivity;
import com.rebeau.technology.android.dagger.activitys.method_inject.module.InjectModule;
import com.rebeau.technology.android.dagger.activitys.one_mvp.OneActivity;
import com.rebeau.technology.android.dagger.activitys.three_absdagger.ThreeActivity;
import com.rebeau.technology.android.dagger.activitys.three_absdagger.module.ABSThreeModule;
import com.rebeau.technology.android.dagger.activitys.two_mvp_dagger.TwoActivity;
import com.rebeau.technology.android.dagger.activitys.two_mvp_dagger.module.TwoModule;
import com.rebeau.technology.android.dagger.fragments.TasksActivity;
import com.rebeau.technology.android.dagger.fragments.TasksModule;
import com.rebeau.technology.android.dagger.scope.ActivityScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract OneActivity oneActivity();

//    @ActivityScoped
    @ContributesAndroidInjector(modules = TwoModule.class)
    abstract TwoActivity twoActivity();

//    @ActivityScoped
    @ContributesAndroidInjector(modules = {ABSThreeModule.class})
    abstract ThreeActivity threeActivity();

//    @ContributesAndroidInjector
    @ActivityScoped
    @ContributesAndroidInjector(modules = {InjectModule.class})
    abstract FourActivity fourActivity();

    @ContributesAndroidInjector(modules = {TasksModule.class})
    abstract TasksActivity tasksActivity();
}
