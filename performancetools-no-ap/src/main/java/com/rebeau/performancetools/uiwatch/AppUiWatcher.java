package com.rebeau.performancetools.uiwatch;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 用于观察Android Ui的卡顿情况
 * 原理：通过监听渲染信号之间的时间差来确定是否卡顿,并采用高频采样确保数据的准确性
 * @see <a href="https://mp.weixin.qq.com/s/MthGj4AwFPL2JrZ0x1i4fw">广研 Android 卡顿监控系统</a>
 * <p>
 */
public class AppUiWatcher {


    /**
     * 单例对象
     */
    private static volatile AppUiWatcher instance;

    /**
     * 私有化
     */
    private AppUiWatcher() {

    }

    //-----------------对外静态方法---------------------

    /**
     * 创建UiWatcher
     */
    public static AppUiWatcher getInstance() {
        if (instance == null) {
            synchronized (AppUiWatcher.class) {
                if (instance == null) {
                    instance = new AppUiWatcher();
                }
            }
        }
        return instance;
    }


    //-----------------对外公有方法---------------------

    /**
     * 帧率阈值(默认1),当跳帧超出此阈值时报警,输出日志并缓存(视开关情况)
     *
     * @param minSkipFrameCount 帧率阈值
     */
    public AppUiWatcher minSkipFrameCount(int minSkipFrameCount) {
        return this;
    }


    /**
     * tag,用于日志输出,标识，默认是UiWatcher
     *
     * @param tag tag
     */
    public AppUiWatcher tag(String tag) {
        return this;
    }

    /**
     * 是否需要缓存日志到文件
     *
     * @param isNeedCacheToFile true:缓存 false:不缓存
     */
    public AppUiWatcher isNeedCacheToFile(boolean isNeedCacheToFile) {
        return this;
    }

    /**
     * 缓存文件夹
     *
     * @param cacheFolder 文件夹
     */
    public AppUiWatcher cacheFolder(String cacheFolder) {
        return this;
    }

    /**
     * 缓存堆栈数量
     *
     * @param cacheDataSize 缓存帧率数量
     */
    public AppUiWatcher cacheSize(int cacheDataSize) {
        return this;
    }

    /**
     * 待筛选的关键词
     *
     * @param keyWords 关键词
     */
    public AppUiWatcher keyWords(String... keyWords) {
        return this;
    }

    /**
     * 用于开启监听，必执行方法！！
     */
    @SuppressLint("NewApi")
    public void startWatch(Context context) {

    }

    @SuppressLint("NewApi")
    public void stopWatch() {
    }
}
