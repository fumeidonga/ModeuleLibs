package com.rebeau.performancetools;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */

public class UIWatchLogCat {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean isDebug = true;
    public static boolean isneedtofile = true;
    private static long timeMillions = 0L;

    public static void resetTime() {
        if (timeMillions == 0L) {
            timeMillions = System.currentTimeMillis();
        }
    }

    public static void dt(String tag, Object content) {
        if (!isDebug) {
            return;
        }
        //StackTraceElement caller = getCallerStackTraceElement();
//        String tag = generateTag(caller);
//        String tag = "";

        Log.w(tag, content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
        if("lancet".equals(tag) && isneedtofile){
            saveAllStackInfoToFile("AppUiWatcher", "lancet", content.toString());
        }
    }

    public static void di(Object content) {
        if (!isDebug) {
            return;
        }
        //StackTraceElement caller = getCallerStackTraceElement();
//        String tag = generateTag(caller);
        String tag = "";

        Log.w("lancet", content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
        if("lancet".equals(tag) && isneedtofile){
            saveAllStackInfoToFile("AppUiWatcher", "lancet", content.toString());
        }
    }

    private static long getTimeUse() {
        long t = 0;
        if (timeMillions == 0L) {
            timeMillions = System.currentTimeMillis();
        }
        t = System.currentTimeMillis() - timeMillions;
        timeMillions = System.currentTimeMillis();
        return t;
    }

    /**
     * 文字最大限制
     */
    private static final int MAX_SIZE = 2000;
    /**
     * 默认日期格式
     */
    public static final SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取当前的时间 格式化：YYYY-MM-DD
     *
     * @return 获取当前的格式化后的时间
     */
    public static String getFileFolderNameByTime() {
        return formatYMD.format(new Date());
    }
    /**
     * 输出日志
     *
     * @param tag tag
     * @param msg msg
     */
    public static void printLog(String tag, String msg) {
        // 获取当前log的长度
        int msgSize = msg.length();
        if (msgSize <= MAX_SIZE) {
            Log.w(tag, msg);
            return;
        }
        //如果超出最大值,看需要输出几次
        int count = msgSize / MAX_SIZE;
        int remainder = msgSize % MAX_SIZE;
        if (remainder != 0) {
            count++;
        }
        //遍历输出
        for (int i = 0; i < count; i++) {
            int start = i * MAX_SIZE;
            int end = (i + 1) * MAX_SIZE;
            end = Math.min(end, msgSize);
            Log.w(tag, msg.substring(start, end));
        }

        //savelog2File(msg + Thread.currentThread().getName());
    }


    // 用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * 保存所有的堆栈信息到文件
     *
     * @param allStackInfo 所有的堆栈信息
     */
    public static void saveAllStackInfoToFile(String cacheFolder, String cacheFileName, String allStackInfo) {

        String time = formatter.format(new Date());

        allStackInfo = "\n" + time + "  " + allStackInfo + "\n";
        RandomAccessFile rfile = null;
        //获取文件通道
        FileChannel channel;
        try {
            //根据配置生成文件夹地址
            final String finalFileRootFolderPath = Environment.getExternalStorageDirectory() + "/Logs" + File.separator + cacheFolder;
            final String finalFileFolderPath = finalFileRootFolderPath + "/" + getFileFolderNameByTime();
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
            e.printStackTrace();
        } finally {
            //关闭流,刷新到文件
            if (rfile != null) {
                try {
                    rfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
