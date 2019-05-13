package com.rebeau.technology.android.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Application Scope 对象
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScoped {}