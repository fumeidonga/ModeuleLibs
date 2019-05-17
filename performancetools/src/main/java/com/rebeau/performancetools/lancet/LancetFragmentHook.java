package com.rebeau.performancetools.lancet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.TextView;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.performancetools.UIWatchLogCat;

import java.util.LinkedList;
import java.util.List;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.ImplementedInterface;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

public class LancetFragmentHook {

    private static final String SUPPORT_FRAGMENT_CLASS = "com.rebeau.commons.fragment.BaseAppFragment";



    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onCreateView", mayCreateSuper = true)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UIWatchLogCat.resetTime();
        View rootView = (View) Origin.call();
        //要考虑到有些Fragment是没有View的
        if (rootView != null) {
            //在这里放入Fragment对象的名称
            rootView.setTag(TROJAN_FRAG_KEY, getSupportFragInfo((Fragment) This.get()));
        }
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onCreateView end ");
        return rootView;
    }

    private static String getSupportFragInfo(Fragment fragment) {
        StringBuilder info = new StringBuilder();
        String className = fragment.getClass().getName();
        info.append(className);
        if (fragment.getTag() != null) {
            info.append(":");
            info.append(fragment.getTag());
        }
        return info.toString();
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onCreate", mayCreateSuper = true)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onCreate:Bundle" + (savedInstanceState == null ? "=null" : "!=null"));
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onCreate end ");
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onStart", mayCreateSuper = true)
    public void onStart() {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onStart");
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onStart end ");
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onResume", mayCreateSuper = true)
    public void onResume() {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onResume");
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onResume end ");
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onPause", mayCreateSuper = true)
    public void onPause() {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onPause");
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onPause end ");
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onHiddenChanged", mayCreateSuper = true)
    public void onHiddenChanged() {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onHiddenChanged");
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onHiddenChanged end ");
    }

    @TargetClass(value = SUPPORT_FRAGMENT_CLASS, scope = Scope.LEAF)
    @Insert(value = "onStop", mayCreateSuper = true)
    public void onStop() {
        UIWatchLogCat.resetTime();
        /*List<String> msgList = new LinkedList<>();
        msgList.add(This.get().getClass().getName());
        msgList.add("onStop");
        UIWatchLogCat.di(msgList);*/
        Origin.callVoid();
        UIWatchLogCat.di(This.get().getClass().getName() + " ---> onStop end ");
    }


    //最大层级数
    private static final int MAX_HIERARCHY_COUNT = 5;

    //注意:这个tag不能随意设置，要满足两点:第一，唯一性;第二，app中的res id不会用到;第三,framework中的res id不会用到。
    //View.setTag()方法的注释如下
    /**
     * * Sets a tag associated with this view and a key. A tag can be used
     * to mark a view in its hierarchy and does not have to be unique within
     * the hierarchy. Tags can also be used to store data within a view
     * without resorting to another data structure.
     * <p>
     * The specified key should be an id declared in the resources of the
     * application to ensure it is unique (see the <a
     * href="{@docRoot}guide/topics/resources/more-resources.html#Id">ID resource type</a>).
     * Keys identified as belonging to
     * the Android framework or not associated with any package will cause
     * an {@link IllegalArgumentException} to be thrown.
     */
    private static final int TROJAN_FRAG_KEY = 0x8f123456;

    //如果要知道这个View是属于哪个Activity或者Fragment中，要怎么做呢?
    //有一个思路就是重写Fragment的onCreateView方法，并且对该View进行setTag,将Fragment的对象名放入其中
    //到后面再向上检索(即一路getParent()),一直到某个View中getTag()不为空则取出
    @Insert("onClick")
    @ImplementedInterface(value = "android.view.View$OnClickListener", scope = Scope.LEAF)
    public void onClick(View v) throws Throwable {
        hookClick(v);
        Origin.callVoid();
    }

    public static void hookClick(View v) {
        if (v == null) {
            return;
        }
        List<String> msgList = new LinkedList<>();
        msgList.add(v.getClass().getName());
        if (v.getId() != View.NO_ID) {
            try {
                msgList.add(v.getResources().getResourceName(v.getId()));
            } catch (Exception e) {
                RBLogUtil.et("error");
                msgList.add("~");
            }
        } else {
            msgList.add("~");
        }
        //还要获取到View在哪个页面，即如果是在Fragment中则打印出Fragment信息，否则打印出Activity信息
        String pageInfo = getPageInfo(v);
        if (TextUtils.isEmpty(pageInfo)) {
            msgList.add(This.get().toString());
        } else {
            msgList.add(pageInfo);
        }
        if (v instanceof TextView
                && ((TextView) v).getText() != null
                && ((TextView) v).getText().toString() != null) {
            msgList.add(((TextView) v).getText().toString());
        } else {
            msgList.add("~");
        }
        //如果是ListView或RecycleView中的某个item,则需要打印出它是第几个ItemView,现详细一点的话甚至需要打印出ItemView中TextView中的信息
        //争光报了bug,先注释这个功能
        //setIndexIfNeed(v, msgList);

        UIWatchLogCat.di(msgList);

    }

    /**
     * 如果是ListView或RecyclerView的ItemView的话就返回index
     *
     * @param view
     * @return
     */
    private static void setIndexIfNeed(View view, List<String> msgList) {
        if (view == null) {
            return;
        }
        int index = 0;
        ViewParent parent = view.getParent();
        while (true) {
            if (index > MAX_HIERARCHY_COUNT) {
                return;
            }
            if (null == parent || !(parent instanceof View)) {
                return;
            }
            if (parent instanceof ListView) {
                msgList.add(getItemInfo((View) parent, view));
                return;
            }
            if (parent instanceof RecyclerView) {
                msgList.add(getItemInfo((View) parent, view));
                return;
            }
            parent = parent.getParent();
            ++index;
        }

    }

    private static String getItemInfo(View parent, View view) {
        if (parent == null || view == null) {
            return "";
        }
        StringBuilder info = new StringBuilder();
        info.append(parent.getClass().getName());
        info.append("@");
        info.append(getHexId(parent));
        info.append("-->Item[");
        if (parent instanceof ListView) {
            info.append((((ListView) parent).getPositionForView(view)));
        } else {
            info.append(((RecyclerView) parent).getChildAdapterPosition(view));
        }
        info.append("]");
        return info.toString();
    }

    private static String getHexId(View view) {
        return Integer.toHexString(view.getId());
    }


    /**
     * 获取页面信息
     *
     * @param view
     * @return
     */
    private static String getPageInfo(View view) {
        String fragInfo = getFragmentTag(view);
        if (!TextUtils.isEmpty(fragInfo)) {
            return fragInfo;
        }
        return getActivityName(view.getContext());
    }

    private static String getActivityName(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return context.getClass().getName();
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return "";
    }

    /**
     * 一路向上回溯
     *
     * @param view
     * @return
     */
    private static String getFragmentTag(View view) {
        View parent = view;
        ViewParent tmpParent;
        String fragTag;
        while (true) {
            fragTag = (String) parent.getTag(TROJAN_FRAG_KEY);
            if (TextUtils.isEmpty(fragTag)) {
                tmpParent = parent.getParent();
                if (null == tmpParent || !(tmpParent instanceof View)) {
                    break;
                }
                parent = (View) tmpParent;
            } else {
                break;
            }
        }
        return fragTag;
    }

}