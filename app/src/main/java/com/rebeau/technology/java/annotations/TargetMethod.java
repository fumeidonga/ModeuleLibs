package com.rebeau.technology.java.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *     1. ElementType .CONSTRUCTOR:用于描述构造器
 *     2. ElementType .FIELD:用于描述域，全局变量，枚举
 *     3. ElementType .LOCAL_VARIABLE:用于描述局部变量
 *     4. ElementType .METHOD:用于描述方法
 *     5. ElementType .PACKAGE:用于描述包
 *     6. ElementType .PARAMETER:用于描述参数
 *     7. ElementType .TYPE:用于描述类、接口(包括注解类型) 或enum声明
 *
 *     RetentionPolicy .SOURCE（源码时注解仅存在于源码中，在class字节码文件中不包含），
 *     RetentionPolicy .CLASS（编译时默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得），
 *     RetentionPolicy .RUNTIME（运行时注解会在class字节码文件中存在，在运行时可以通过反射获取到），
 *
 *     注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为null
 *     注解方法不能有参数,如下面带删除线的方法
 *     举个例子：下面自定义的注解中，有两个方法给了默认的值，那么我们在使用的时候，就可以省略
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetMethod {
    String author() default "hrl";

    String data();

    String url();

    int version() default 1;
}
