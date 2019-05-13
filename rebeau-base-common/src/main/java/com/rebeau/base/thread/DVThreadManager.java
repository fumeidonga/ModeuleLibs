package com.rebeau.base.thread;

import android.os.Handler;
import android.os.HandlerThread;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2018/3/30.
 */

public class DVThreadManager {

    //使用volatile关键字防止重排序，因为 new Instance()是一个非原子操作，可能创建一个不完整的实例
    private static volatile DVThreadManager singleton3;

    private ThreadPoolExecutorsExpand threadPoolExecutorsExpand;

    private DVThreadManager() {
        threadPoolExecutorsExpand = initThreadPoolExecutorsExpand();
        threadMap = new HashMap<String, ThreadBean>();
    }

    public static DVThreadManager getThreadManager() {
        // Double-Check idiom
        if (singleton3 == null) {
            synchronized (DVThreadManager.class) {       // 1
                // 只需在第一次创建实例时才同步
                if (singleton3 == null) {       // 2
                    singleton3 = new DVThreadManager();      // 3
                }
            }
        }
        return singleton3;
    }

    private ThreadPoolExecutorsExpand initThreadPoolExecutorsExpand() {
        return new ThreadPoolExecutorsExpand(
                DVThreadConstant.CORE_POOL_SIZE,
                DVThreadConstant.MAXIMUM_POOL_SIZE,
                DVThreadConstant.KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new EventThreadFactory(DVThreadConstant.THREAD_POOLNAME),
                DVThreadConstant.THREAD_POOLNAME);
    }

    /**
     * 对外提供一个执行任务的方法
     */
    public void execute(Runnable r) {
        if(threadPoolExecutorsExpand == null || threadPoolExecutorsExpand.isShutdown()) {
            initThreadPoolExecutorsExpand();
        }

        if(threadPoolExecutorsExpand == null || threadPoolExecutorsExpand.isShutdown()) {
            return;
        }
        threadPoolExecutorsExpand.execute(r);
    }

    // @ ---------------------------------use handthread---------------------------------------

    private Map<String, ThreadBean> threadMap;


    /**运行线程
     * @param name
     * @param runnable
     * @return
     */
    public Handler runThread(String name, Runnable runnable) {
        if (RBStringUtil.isEmpty(name) == false || runnable == null) {
            RBLogUtil.dt( "runThread  StringUtil.isNotEmpty(name, true) == false || runnable == null >> return");
            return null;
        }
        name = name.trim();
        RBLogUtil.d( "\n runThread  name = " + name);

        Handler handler = getHandler(name);
        if (handler != null) {
            RBLogUtil.d( "handler != null >>  destroyThread(name);");
            destroyThread(name);
        }

        HandlerThread thread = new HandlerThread(name);
        thread.start();//创建一个HandlerThread并启动它
        handler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler
        handler.post(runnable);//将线程post到Handler中

        threadMap.put(name, new ThreadBean(name, thread, runnable, handler));

        RBLogUtil.d( "runThread  added name = " + name + "; threadMap.size() = " + threadMap.size() + "\n");
        return handler;
    }

    /**获取线程Handler
     * @param name
     * @return
     */
    private Handler getHandler(String name) {
        ThreadBean tb = getThread(name);
        return tb == null ? null : tb.getHandler();
    }

    /**获取线程
     * @param name
     * @return
     */
    private ThreadBean getThread(String name) {
        return name == null ? null : threadMap.get(name);
    }


    /**销毁线程
     * @param nameList
     * @return
     */
    public void destroyThread(List<String> nameList) {
        if (nameList != null) {
            for (String name : nameList) {
                destroyThread(name);
            }
        }
    }
    /**销毁线程
     * @param name
     * @return
     */
    public void destroyThread(String name) {
        destroyThread(getThread(name));
    }
    /**销毁线程
     * @param tb
     * @return
     */
    private void destroyThread(ThreadBean tb) {
        if (tb == null) {
            RBLogUtil.d( "destroyThread  tb == null >> return;");
            return;
        }

        destroyThread(tb.getHandler(), tb.getRunnable());
        if (tb.getName() != null) { // StringUtil.isNotEmpty(tb.getName(), true)) {
            threadMap.remove(tb.getName());
        }
    }
    /**销毁线程
     * @param handler
     * @param runnable
     * @return
     */
    public void destroyThread(Handler handler, Runnable runnable) {
        if (handler == null || runnable == null) {
            RBLogUtil.d( "destroyThread  handler == null || runnable == null >> return;");
            return;
        }

        try {
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            RBLogUtil.d( "onDestroy try { handler.removeCallbacks(runnable);...  >> catch  : " + e.getMessage());
        }
    }


    /**结束ThreadManager所有进程
     */
    public void finish() {
        if (threadMap == null || threadMap.keySet() == null) {
            RBLogUtil.d( "finish  threadMap == null || threadMap.keySet() == null >> threadMap = null; >> return;");
            threadMap = null;
            return;
        }
        List<String> nameList = new ArrayList<String>(threadMap.keySet());//直接用Set在系统杀掉应用时崩溃
        if (nameList != null) {
            for (String name : nameList) {
                destroyThread(name);
            }
        }
        threadMap = null;
        RBLogUtil.d( "\n finish  finished \n");
    }


    /**线程类
     */
    private static class ThreadBean {

        private String name;
        @SuppressWarnings("unused")
        private HandlerThread thread;
        private Runnable runnable;
        private Handler handler;

        public ThreadBean(String name, HandlerThread thread, Runnable runnable, Handler handler) {
            this.name = name;
            this.thread = thread;
            this.runnable = runnable;
            this.handler = handler;
        }

        public String getName() {
            return name;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public Handler getHandler() {
            return handler;
        }
    }

}
