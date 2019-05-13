package com.rebeau.technology.android.dagger.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {


    @Singleton
//    @ApplicationScoped
    @Provides
    Gson provideGson() {
        return new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    }

}
