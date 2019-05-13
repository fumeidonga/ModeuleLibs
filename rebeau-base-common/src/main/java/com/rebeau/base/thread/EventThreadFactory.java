package com.rebeau.base.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/3/30.
 * 生成线程池所用的线程，只是改写了线程池默认的线程工厂，传入线程池名称，便于问题追踪
 */

public class EventThreadFactory implements ThreadFactory {

    private final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    /**
     * 初始化线程工厂
     *
     * @param poolName 线程池名称
     */
    public EventThreadFactory(String poolName) {
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = poolName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

}
