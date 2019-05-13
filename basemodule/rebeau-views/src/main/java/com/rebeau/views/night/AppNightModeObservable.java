package com.rebeau.views.night;

import com.rebeau.base.cache.DataManager;
import com.rebeau.base.config.Constants;

import java.util.Observable;

/**
 * App全局夜间模式被观察者（利用sdk中的Observable）
 */
public class AppNightModeObservable extends Observable {

    private int mBgMode;

    private static class Single {
        public static volatile AppNightModeObservable instance = new AppNightModeObservable();
    }

    private AppNightModeObservable() {
        mBgMode = DataManager.getInstance().getInt(Constants.ACTIVITY_NIGHT_MODEL);
    }

    public static AppNightModeObservable getInstance() {
        return Single.instance;
    }

    public int getBgMode() {
        return mBgMode;
    }

    /**
     * 更新阅读背景模式
     * @param bgMode
     */
    public void setBgMode(int bgMode) {
        if (mBgMode != bgMode) {
            mBgMode = bgMode;
            setChanged();
            notifyObservers(bgMode);
        }
    }

    /**
     * 判断当前是否为夜间模式
     * @return
     */
    public boolean isNightMode() {
        return mBgMode == Constants.BG_NIGHT;
    }

    /**
     * 判断当前是否为深色
     * @return
     */
    public boolean isDarkMode() {
        return mBgMode == Constants.BG_NIGHT;
    }
}
