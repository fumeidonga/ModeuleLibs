package com.rebeau.technology.java.testdesignmodel.proxy.dynamic;


import com.rebeau.base.utils.RBLogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 */
public class DynamicProxy implements InvocationHandler {

    private Object object;

    public DynamicProxy(Object object) {
        this.object = object;
    }

    /**
     * 对代理类中的所有方法的调用都会变为对invoke的调用, 也可以理解为一个拦截器
     *
     * @param proxy 代理类对象
     * @param method 具体调用的是代理类的哪个方法
     * @param args 方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RBLogUtil.d("动态代理 调用方法：" + method.getName());
        if(method.getName().equals("pay") || method.getName().equals("buy")) {
            //do somethings
        }
        Object result = method.invoke(object, args);
        return result;
    }

    public <T> T getProxy(){
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                this
            );
    }
}
