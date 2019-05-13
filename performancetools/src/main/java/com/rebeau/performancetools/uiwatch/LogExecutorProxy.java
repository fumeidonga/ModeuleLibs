package com.rebeau.performancetools.uiwatch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


/**
 * 日志通知者
 * 用于定时给LogExecutor发送各种指令
 * 为保证数据获取的准确性和时效性，
 * 需要启用线程定时获取
 */
public class LogExecutorProxy extends AbsLogExecutor {

    private final String TAG = "LogExecutorProxy";

    private static LogExecutorProxy instance;
    /**
     * 单例的LogExecutor
     */
    private AbsLogExecutor logExecutor;

    public static LogExecutorProxy getInstance() {
        if (instance == null) {
            synchronized (LogExecutorProxy.class) {
                if (instance == null) {
                    instance = new LogExecutorProxy();
                }
            }
        }
        return instance;
    }

    private LogExecutorProxy() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        logExecutor = LogExecutorExecutor.getInstance();
        mLogHandlerThread = new HandlerThread(TAG + "_Thread");
        mLogHandlerThread.start();

        mLogHandler = new Handler(mLogHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int type = msg.what;
                switch (type) {
                    case TYPE_ACTION_COLLECTION_LOG:
                        //通知LogExecutor处理收集任务
                        if (logExecutor != null) {
                            Thread mainThread = Looper.getMainLooper().getThread();
                            StackTraceElement[] stackInfo = mainThread.getStackTrace();
                            if(stackInfo != null){
                                logExecutor.sendObtainActionMessage(TYPE_ACTION_COLLECTION_LOG, 0,  stackInfo);
                            }
                        }
                        //通知自身下次收集
                        //sendCollectionMessage(DEFAULT_HANDLER_DELAY_TIME);
                        sendObtainActionMessage(TYPE_ACTION_COLLECTION_LOG, DEFAULT_HANDLER_DELAY_TIME, null);
                        break;
                    case TYPE_ACTION_OUTPUT_LOG:
                        //通知LogExecutor处理输出任务
                        if (logExecutor != null) {
                            logExecutor.sendObtainActionMessage(TYPE_ACTION_OUTPUT_LOG, 0, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 开始
     */
    public void start() {
        if (logExecutor == null || mLogHandlerThread == null) {
            init();
        }
        logExecutor.start();
        sendObtainActionMessage(TYPE_ACTION_COLLECTION_LOG, 0, null);
    }

    /**
     * 开始输出并重启收集通知
     */
    public void startOutputAndRestartCollectionNotifier() {
        if (mLogHandler != null) {
            mLogHandler.removeCallbacksAndMessages(null);
        }
        sendObtainActionMessage(TYPE_ACTION_OUTPUT_LOG, 0, null);
        sendObtainActionMessage(TYPE_ACTION_COLLECTION_LOG, 0, null);
    }

    /**
     * 停止
     */
    public void stop() {
        if (logExecutor != null) {
            logExecutor.stop();
            logExecutor = null;
        }
        if (mLogHandler != null) {
            mLogHandler.removeCallbacksAndMessages(null);
            mLogHandler = null;
        }
        if (mLogHandlerThread != null) {
            mLogHandlerThread.quit();
            mLogHandlerThread = null;
        }
    }

    /**
     *
     * @param actionType 类型，0 收集日志， 1， 输出日志
     * @param delay 延迟时间
     */
    public void sendObtainActionMessage(int actionType, long delay, Object stackInfo) {

        if (mLogHandler == null) {
            return;
        }
        Message outputMessage = Message.obtain(mLogHandler, actionType);
        mLogHandler.sendMessageDelayed(outputMessage, delay);
    }


    //----------------- set 基础信息 begin---------------------

    /**
     * 设置缓存数量
     *
     * @param cacheDataSize 缓存数量,默认10
     */
    public void setCacheDataSize(int cacheDataSize) {
        if (logExecutor != null) {
            logExecutor.setCacheDataSize(cacheDataSize);
        }
    }

    /**
     * 设置日志TAG
     *
     * @param tag tag
     */
    public void setTag(String tag) {
        if (logExecutor != null) {
            logExecutor.setTag(tag);
        }
    }

    /**
     * 设置是否需要缓存文件
     *
     * @param needCacheToFile true:缓存本地 false:不缓存到本地
     */
    public void setNeedCacheToFile(boolean needCacheToFile) {
        if (logExecutor != null) {
            logExecutor.setNeedCacheToFile(needCacheToFile);
        }
    }

    /**
     * 设置缓存的文件夹
     *
     * @param cacheFolder 文件夹
     */
    public void setCacheFolder(String cacheFolder) {
        if (logExecutor != null) {
            logExecutor.setCacheFolder(cacheFolder);
        }
    }

    /**
     * 设置筛选关键词
     *
     * @param keyWords 关键词集合
     */
    public void setKeyWords(String[] keyWords) {
        if (logExecutor != null) {
            logExecutor.setKeyWords(keyWords);
        }
    }
    /**
     */
    public void setExcludekeyWordsKeyWords(String[] keyWords) {
        if (logExecutor != null) {
            logExecutor.setExcludekeyWordsKeyWords(keyWords);
        }
    }

    //----------------- set 基础信息 end---------------------
}
