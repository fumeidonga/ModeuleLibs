package com.rebeau.technology.android.dagger.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by david on 2017/1/3.
 * 1181406436@qq.com
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    String value() default "";
}
