package com.rebeau.performancetools.uiwatch;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.rebeau.base.utils.RBDateUtil;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.performancetools.UIWatchLogCat;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * 日志收集和输出
 */

public class LogExecutorExecutor extends AbsLogExecutor {
    private final String TAG = "LogExecutorExecutor";
    /**
     * 实例
     */
    private static LogExecutorExecutor instance;
    /**
     * 日志堆栈信息的构造builder
     */
    private StringBuilder logStackInfoBuilder;

    /**
     * log堆栈信息队列,只保存限制的条数,防止内存占用过大
     */
    private List<String> logStackInfoQueue;

    /**
     * 单例创建实例
     */
    public static LogExecutorExecutor getInstance() {
        if (instance == null) {
            synchronized (LogExecutorExecutor.class) {
                if (instance == null) {
                    instance = new LogExecutorExecutor();
                }
            }
        }
        return instance;
    }

    private LogExecutorExecutor() {
        init();
    }

    private void init() {
        mLogHandlerThread = new HandlerThread(TAG + "_Thread");
        logStackInfoBuilder = new StringBuilder();
        logStackInfoQueue = new LinkedList<>();
    }


    /**
     * 开启线程
     */
    public void start() {
        if (mLogHandlerThread == null) {
            init();
        }
        mLogHandlerThread.start();

        mLogHandler = new Handler(mLogHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int type = msg.what;
                Object stackInfo = msg.obj;
                switch (type) {
                    case TYPE_ACTION_COLLECTION_LOG:
                        //收集堆栈信息 待缓存堆栈信息
                        startCollectionLogTask(stackInfo);
                        break;
                    case TYPE_ACTION_OUTPUT_LOG:
                        //输出堆栈信息
                        startOutputLogTask();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 开始输出日志任务
     */
    private void startOutputLogTask() {
        //校验缓存信息的队列
        if (logStackInfoQueue == null || logStackInfoQueue.isEmpty()) {
            return;
        }
        //将获取的队列内的内容遍历获取
        logStackInfoBuilder.delete(0, logStackInfoBuilder.length());
        logStackInfoBuilder.append(" \n");
        logStackInfoBuilder.append(" \n");
        logStackInfoBuilder.append("~~~~~~~~~~~~~~~~~~~start~~~~~~~~~~~~~~~~~~~~~~");
        logStackInfoBuilder.append(" \n");
        logStackInfoBuilder.append(" \n");
        for (String stackInfo : logStackInfoQueue) {
            logStackInfoBuilder.append(stackInfo);
            logStackInfoBuilder.append("\n");
        }
        logStackInfoBuilder.append("~~~~~~~~~~~~~~~~~~~end~~~~~~~~~~~~~~~~~~~~~~");
        logStackInfoBuilder.append("\n");
        logStackInfoBuilder.append(" \n");
        //清除原队列数据
        logStackInfoQueue.clear();
        //获取全部的堆栈信息
        String allStackInfo = logStackInfoBuilder.toString();
        //输出信息并视情况缓存
        UIWatchLogCat.printLog(tag, allStackInfo);
        //检测是否需要存储到本地
        if (isNeedCacheToFile) {
            saveAllStackInfoToFile(allStackInfo);
        }
    }

    /**
     * 保存所有的堆栈信息到文件
     *
     * @param allStackInfo 所有的堆栈信息
     */
    private void saveAllStackInfoToFile(String allStackInfo) {
        RandomAccessFile rfile = null;
        //获取文件通道
        FileChannel channel;
        try {
            //根据配置生成文件夹地址
            final String finalFileRootFolderPath = Environment.getExternalStorageDirectory() + "/" + cacheFolder;
            final String finalFileFolderPath = finalFileRootFolderPath + "/" + RBDateUtil.getFileFolderNameByTime();
            //校验文件夹是否存在,不存在则创建
            File fileFolder = new File(finalFileFolderPath);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
            //校验文件是否存在
            String cacheFilePath = finalFileFolderPath + "/" + cacheFileName + ".txt";
            File cacheFile = new File(cacheFilePath);
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            //追加文件写入新的堆栈信息
            //获取文件
            rfile = new RandomAccessFile(cacheFilePath, "rw");
            //获取文件通道
            channel = rfile.getChannel();
            channel.position(channel.size());
            //写入缓冲区
            byte[] allStackInfoBytes = allStackInfo.getBytes();
            ByteBuffer buff = ByteBuffer.wrap(allStackInfoBytes);
            buff.put(allStackInfoBytes);
            buff.flip();
            //写入文件
            channel.write(buff);
        } catch (IOException e) {
            RBLogUtil.et("error");
        } finally {
            //关闭流,刷新到文件
            if (rfile != null) {
                try {
                    rfile.close();
                } catch (IOException e) {
                    RBLogUtil.et("error");
                }
            }
        }

    }

    /**
     *
     * @param actionType 类型，0 收集日志， 1， 输出日志
     * @param stackInfo 堆栈消息
     * @return
     */
    public void sendObtainActionMessage(int actionType, long delay, Object stackInfo){
        Message outputMsg = Message.obtain(mLogHandler, actionType, stackInfo);

        if (mLogHandler == null || outputMsg == null || outputMsg.getTarget() == null) {
            return;
        }
        mLogHandler.sendMessage(outputMsg);
    }

    /**
     * 开始日志收集任务
     *
     * @param stackInfo 堆栈信息对象
     */
    private void startCollectionLogTask(Object stackInfo) {
        //校验数据类型是否正确
        if (stackInfo == null || !(stackInfo instanceof StackTraceElement[])) {
            return;
        }
        StackTraceElement[] stackTraceElements = (StackTraceElement[]) stackInfo;

        //检验堆栈信息
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return;
        }

        //初始化Log信息
        logStackInfoBuilder.delete(0, logStackInfoBuilder.length());
        logStackInfoBuilder.append("---------------------------------------------------");
        logStackInfoBuilder.append("\n");

        //追加堆栈信息
        boolean hasUsefulInfo = false;
        for (StackTraceElement mStackInfo : stackTraceElements) {
            String info = mStackInfo.toString();
            if (checkInfoUseful(info)) {
                logStackInfoBuilder.append(info);
                logStackInfoBuilder.append("\n");
                hasUsefulInfo = true;
            }
        }
        logStackInfoBuilder.append("---------------------------------------------------");
        logStackInfoBuilder.append("\n");

        //判断是否需要添加
        //获取当前堆栈信息,存储到队列
        String currentStackInfo = logStackInfoBuilder.toString();
        //获取上一个堆栈信息比较是否相同
        String lastInfo = logStackInfoQueue.isEmpty() ? "" : logStackInfoQueue.get(logStackInfoQueue.size() - 1);
        //不相同且有有效内容
        boolean isNeedAdd = hasUsefulInfo && !currentStackInfo.equals(lastInfo);
        if (isNeedAdd) {
            //将当前堆栈信息存储到队列中
            logStackInfoQueue.add(currentStackInfo);
        }
        //判断存储队列是否已经超出限制,视情况移除队列前的内容,在队尾增加
        if (logStackInfoQueue.size() > cacheDataSize) {
            logStackInfoQueue.remove(0);
        }
    }

    /**
     * 校验信息是否有效
     */
    private boolean checkInfoUseful(String info) {

        //校验内容是否有效,无效直接
        if (TextUtils.isEmpty(info)) {
            return false;
        }

        //未设置关键词默认为全通过
        if (keyWords == null || keyWords.length == 0) {
            return true;
        }
        //过滤信息
        /*if (excludekeyWords == null || excludekeyWords.length == 0) {
            return true;
        }*/
        //校验是否包含关键词,包含返回true,反之返回false
        for (String keyWord : keyWords) {
            if (info.contains(keyWord)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭执行
     */
    public void stop() {
        if (mLogHandler != null) {
            mLogHandler.removeCallbacksAndMessages(null);
            mLogHandler = null;
        }
        if (mLogHandlerThread != null) {
            mLogHandlerThread.quit();
            mLogHandlerThread = null;
        }
    }
}
