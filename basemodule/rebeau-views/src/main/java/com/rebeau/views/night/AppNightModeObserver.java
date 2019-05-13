package com.rebeau.views.night;

import java.util.Observable;
import java.util.Observer;

/**
 * App全局夜间模式观察者（利用sdk中的Observer）
 */
public interface AppNightModeObserver extends Observer {

    /**
     * 表示全局夜间模式已更改的回调
     * @param arg 为int值，取值对应阅读器中背景设置值
     */
    @Override
    void update(Observable o, Object arg);
}
