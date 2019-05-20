package com.rebeau.performancetools.uiwatch;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * author: huruilong
 * date: 2019/4/22
 * description: ${Desc} .
 */
public abstract class AbsLogExecutor {

    /**
     * 数据收集
     */
    static final int TYPE_ACTION_COLLECTION_LOG = 0;
    /**
     * 数据输出
     */
    static final int TYPE_ACTION_OUTPUT_LOG = 1;

    /**
     * 默认Log的获取间隔
     */
    static final long DEFAULT_HANDLER_DELAY_TIME = 16;

    /**
     * 用于处理日志的线程
     */
    public HandlerThread mLogHandlerThread;
    /**
     * 用于处理日志的Handler
     */
    public Handler mLogHandler;
    //----------------base config----------------------
    /**
     * 堆栈信息最多保存条数,最少1 至多无限制,默认3
     */
    int cacheDataSize = 3;

    /**
     * 卡顿时输出日志的log,默认为 AppUiWatcher
     */
    String tag = "AppUiWatcher";

    /**
     * 是否开启缓存到文件,默认为true
     */
    boolean isNeedCacheToFile = true;

    /**
     * 缓存的文件夹地址
     */
    String cacheFolder = "AppUiWatcher";

    /**
     * 缓存文件名称
     */
    String cacheFileName = "UiWatcherLogData";

    /**
     * 关键词
     */
    String[] keyWords = null;
    String[] excludekeyWords = null;
    //------------- type config-------------------

    /**
     *
     * @param actionType 类型，0 收集日志， 1， 输出日志
     * @param stackInfo 堆栈消息
     * @return
     */
    public abstract void sendObtainActionMessage(int actionType, long delay,  Object stackInfo);

    public abstract void stop();

    public abstract void start();


    //--------------------------------

    /**
     * 设置缓存数量
     *
     * @param cacheDataSize 缓存数量
     */
    public void setCacheDataSize(int cacheDataSize) {
        this.cacheDataSize = cacheDataSize;
    }

    /**
     * 设置日志输出TAG
     *
     * @param tag tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 设置是否需要缓存到本地文件
     *
     * @param needCacheToFile true:需要 false:不需要
     */
    public void setNeedCacheToFile(boolean needCacheToFile) {
        isNeedCacheToFile = needCacheToFile;
    }

    /**
     * 设置缓存文件夹的名称
     *
     * @param cacheFolder 缓存文件夹名称
     */
    public void setCacheFolder(String cacheFolder) {
        this.cacheFolder = cacheFolder;
    }

    /**
     * 设置缓存文件名称
     *
     * @param cacheFileName 缓存文件名
     */
    public void setCacheFileName(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    /**
     * 设置过滤关键词 （排除不是关键词内的内容）
     *
     * @param keyWords 关键词
     */
    public void setKeyWords(String[] keyWords) {
        this.keyWords = keyWords;
    }
    /**
     * 设置过滤关键词 （排除不是关键词内的内容）
     *
     * @param excludekeyWords 关键词
     */
    public void setExcludekeyWordsKeyWords(String[] excludekeyWords) {
        this.excludekeyWords = excludekeyWords;
    }
}
