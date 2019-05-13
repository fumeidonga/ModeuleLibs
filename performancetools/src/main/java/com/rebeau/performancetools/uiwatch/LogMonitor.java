package com.rebeau.performancetools.uiwatch;

/**
 * 日志监控
 * 用于日志的存储和输出
 * LogMonitor通过handler对LogThread 进行操作，具体执行是LogThread
 *
 * @date 18-6-27
 */
public class LogMonitor {
    /**
     * 日志监听实例
     */
    private static volatile LogMonitor instance = null;

    private LogExecutorProxy logMonitorProxy;


    /**
     * 初始化日志打印类(内部开启两个线程一个用于缓存,一个用于输出)
     */
    private LogMonitor() {
        init();
    }

    private void init() {
        logMonitorProxy = LogExecutorProxy.getInstance();
    }

    public static LogMonitor getInstance() {
        if (instance == null) {
            synchronized (LogMonitor.class) {
                if (instance == null) {
                    instance = new LogMonitor();
                }
            }
        }
        return instance;
    }


    /**
     * 开启Log监听
     */
    public void startMonitor() {
        if (logMonitorProxy == null) {
            return;
        }
        logMonitorProxy.start();
    }

    /**
     * 关闭Log监听
     */
    public void stopMonitor() {
        if (logMonitorProxy != null) {
            logMonitorProxy.stop();
        }
    }


    /**
     * 开始输出并重新开始收集日志
     */
    public void startOutputAndResetCollectionMonitor() {
        if (logMonitorProxy == null) {
            return;
        }
        logMonitorProxy.startOutputAndRestartCollectionNotifier();
    }


    //-----------------基础信息---------------------

    /**
     * 设置缓存数量
     *
     * @param cacheDataSize 缓存数量,默认10
     */
    public LogMonitor setCacheDataSize(int cacheDataSize) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setCacheDataSize(cacheDataSize);
        }
        return this;
    }

    /**
     * 设置日志TAG
     *
     * @param tag tag
     */
    public LogMonitor setTag(String tag) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setTag(tag);
        }
        return this;
    }

    /**
     * 设置是否需要缓存文件
     *
     * @param needCacheToFile true:缓存本地 false:不缓存到本地
     */
    public LogMonitor setNeedCacheToFile(boolean needCacheToFile) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setNeedCacheToFile(needCacheToFile);
        }
        return this;
    }

    /**
     * 设置缓存的文件夹
     *
     * @param cacheFolder 文件夹
     */
    public LogMonitor setCacheFolder(String cacheFolder) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setCacheFolder(cacheFolder);
        }
        return this;
    }

    /**
     * 设置筛选关键词
     *
     * @param keyWords 关键词集合
     */
    public LogMonitor setKeyWords(String[] keyWords) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setKeyWords(keyWords);
        }
        return this;
    }
    /**
     * 设置排除关键词
     *
     * @param keyWords 关键词集合
     */
    public LogMonitor setExcludekeyWordsKeyWords(String[] keyWords) {
        if (logMonitorProxy != null) {
            logMonitorProxy.setExcludekeyWordsKeyWords(keyWords);
        }
        return this;
    }
}
