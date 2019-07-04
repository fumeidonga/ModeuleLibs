package com.rebeau.performancetools.lancet;

import android.content.Context;

import com.rebeau.performancetools.UIWatchLogCat;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * author: hrl
 * date: 2019/4/18
 * description: ${Desc} .
 */
public class LancetApplicationHook {

    @Insert(value = "onCreate",mayCreateSuper = true)
    @TargetClass(value = "android.app.Application",scope = Scope.LEAF)
    protected void onCreate() {
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onCreate begin3 ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onCreate end3 ");
    }

    /*@Insert(value = "onCreate",mayCreateSuper = true)
    @TargetClass(value = "app.MainApplication",scope = Scope.LEAF)
    protected void onCreate() {
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onCreate begin ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> onCreate end ");
    }*/

    @Insert(value = "attachBaseContext",mayCreateSuper = true)
    @TargetClass(value = "android.app.Application",scope = Scope.LEAF)
    protected void attachBaseContext(Context base) {
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> attachBaseContext begin3 ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", This.get().getClass().getName() + " ---> attachBaseContext end3 ");
    }

    //当目标方法不存在时，还可以使用mayCreateSuper参数来创建目标方法,mayCreateSuper不可用于静态方法
    @Insert(value = "install")
    @TargetClass(value = "android.support.multidex.MultiDex",scope = Scope.LEAF)
    public static void install(Context base) {
        UIWatchLogCat.dt("lancet", " android.support.multidex.MultiDex.install() begin3 ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", " android.support.multidex.MultiDex.install() end3 ");
    }

    //当目标方法不存在时，还可以使用mayCreateSuper参数来创建目标方法,mayCreateSuper不可用于静态方法
    @Insert(value = "installTinker")
    @TargetClass(value = "com.tencent.bugly.beta.Beta",scope = Scope.SELF)
    public static void installTinker() {
        UIWatchLogCat.dt("lancet", " com.tencent.bugly.beta.Beta.installTinker() begin3 ");
        UIWatchLogCat.resetTime();
        Origin.callVoid();
        UIWatchLogCat.dt("lancet", " com.tencent.bugly.beta.Beta.installTinker() end3 ");
    }

    //对于单个方法等监控，我们可以在方法中插入，如上面等(Insert)方法，也可以整个替换(Proxy)需要监控等方法，
    //当目标方法不存在时，还可以使用mayCreateSuper参数来创建目标方法,mayCreateSuper不可用于静态方法
    @Proxy("i")
    @TargetClass(value = "android.util.Log")
    public static int i(String tag, String msg) {
        UIWatchLogCat.dt("lancet", " android.util.Log i begin3 ");
        UIWatchLogCat.resetTime();
        int d = (int) Origin.call();
        UIWatchLogCat.dt("lancet", " android.util.Log i end3 ");
        return d;
    }

//    Proxy 使用新的方法替换代码里存在的原有的目标方法,通常用与对系统 API 的劫持。因为虽然我们不能注入代码到系统提供的库之中，但我们可以劫持掉所有调用系统API的地方
    @Proxy(value = "d")
    @TargetClass(value = "android.util.Log", scope = Scope.SELF)
    public static int d(String tag, String msg) {
        UIWatchLogCat.dt("lancet", " android.util.Log d begin3 ");
        UIWatchLogCat.resetTime();
        //替换 msg 的内容
        msg = msg+" <---> ";
        int d = (int) Origin.call();
        UIWatchLogCat.dt("lancet", " android.util.Log d end3 ");
        return d;
    }

}
