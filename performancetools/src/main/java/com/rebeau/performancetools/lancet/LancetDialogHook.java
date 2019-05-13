package com.rebeau.performancetools.lancet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.rebeau.base.utils.RBLogUtil;

import java.util.LinkedList;
import java.util.List;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * author: hrl
 * date: 2019/4/18
 * description: ${Desc} .
 */
public class LancetDialogHook {



    @TargetClass(value = "android.app.Dialog", scope = Scope.LEAF)
    @Insert(value = "show", mayCreateSuper = true)
    public void showDialog() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("show");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.app.Dialog", scope = Scope.LEAF)
    @Insert(value = "hide", mayCreateSuper = true)
    public void hideDialog() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("hide");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.app.Dialog", scope = Scope.LEAF)
    @Insert(value = "dismiss", mayCreateSuper = true)
    public void dismissDialog() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("dismiss");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "show", mayCreateSuper = true)
    public void showDialogFragment(FragmentManager manager, String tag) {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("show");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "show", mayCreateSuper = true)
    public int showDialogFragment(FragmentTransaction transaction, String tag) {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("show");
        RBLogUtil.dt(msgList);
        return (int) Origin.call();
    }

    @TargetClass(value = "android.support.v4.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "show", mayCreateSuper = true)
    public void showSupportDialogFragment(android.support.v4.app.FragmentManager manager, String tag) {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("show");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.support.v4.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "show", mayCreateSuper = true)
    public int showSupportDialogFragment(android.support.v4.app.FragmentTransaction transaction, String tag) {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("show");
        RBLogUtil.dt(msgList);
        return (int) Origin.call();
    }

    @TargetClass(value = "android.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "dismiss", mayCreateSuper = true)
    public void dismissDialogFragment() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("dismiss");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.support.v4.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "dismiss", mayCreateSuper = true)
    public void dismissSupportDialogFragment() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("dismiss");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "dismissAllowingStateLoss", mayCreateSuper = true)
    public void dismissAllowingDialogFragment() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("dismissAllowingStateLoss");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }

    @TargetClass(value = "android.support.v4.app.DialogFragment", scope = Scope.LEAF)
    @Insert(value = "dismissAllowingStateLoss", mayCreateSuper = true)
    public void dismissAllowingSupportDialogFragment() {
        List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("dismissAllowingStateLoss");
        RBLogUtil.dt(msgList);
        Origin.callVoid();
    }
}
