package com.rebeau.base.thread;

import com.rebeau.base.utils.RBLogUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/30.
 * 该类继承ThreadPoolExecutor类，覆盖了shutdown(), shutdownNow(), beforeExecute() 和 afterExecute()
 * 方法来统计线程池的执行情况
 */

public class ThreadPoolExecutorsExpand extends ThreadPoolExecutor {

    // 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
    private ConcurrentHashMap<String, Date> startTimes;

    // 线程池名称，一般以业务名称命名，方便区分
    private String poolName;

    /**
     *
     * @param corePoolSize 线程池核心线程数
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime 线程的最大空闲时间
     * @param unit 空闲时间的单位
     * @param workQueue 保存被提交任务的队列
     * @param poolName 线程池名称
     */
    public ThreadPoolExecutorsExpand(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        init();
    }

    /**
     *
     * @param corePoolSize 线程池核心线程数
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime 线程的最大空闲时间
     * @param unit 空闲时间的单位
     * @param workQueue 保存被提交任务的队列
     * @param threadFactory
     * @param poolName 线程池名称
     */
    public ThreadPoolExecutorsExpand(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        init();
    }

    public ThreadPoolExecutorsExpand(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        init();
    }

    public ThreadPoolExecutorsExpand(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        init();
    }

    private void init() {
        this.startTimes = new ConcurrentHashMap<>();
        this.poolName = poolName;
    }

    /**
     * 线程池延迟关闭时（等待线程池里的任务都执行完毕），统计线程池情况
     */
    @Override
    public void shutdown() {
        // 统计已执行任务、正在执行任务、未执行任务数量
        RBLogUtil.dt(String.format(this.poolName + " Going to shutdown. Executed tasks: %d, Running tasks: %d, Pending tasks: %d",
                this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size()));
        super.shutdown();
    }

    /**
     * 线程池立即关闭时，统计线程池情况
     */
    @Override
    public List<Runnable> shutdownNow() {
        // 统计已执行任务、正在执行任务、未执行任务数量
        RBLogUtil.dt(
                String.format(this.poolName + " Going to immediately shutdown. Executed tasks: %d, Running tasks: %d, Pending tasks: %d",
                        this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size()));
        return super.shutdownNow();
    }

    /**
     * 任务执行之前，记录任务开始时间
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTimes.put(String.valueOf(r.hashCode()), new Date());
    }

    /**
     * 任务执行之后，计算任务结束时间
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
        Date finishDate = new Date();
        long diff = finishDate.getTime() - startDate.getTime();
        // 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、
        // 池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
        RBLogUtil.dt(String.format(this.poolName
                        + "-pool-monitor: Duration: %d ms, PoolSize: %d, CorePoolSize: %d, Active: %d, Completed: %d, " +
                        "Task: %d, Queue: %d, LargestPoolSize: %d, MaximumPoolSize: %d,  KeepAliveTime: %d, isShutdown: %s, isTerminated: %s",
                diff, this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(), this.getCompletedTaskCount(), this.getTaskCount(),
                this.getQueue().size(), this.getLargestPoolSize(), this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS),
                this.isShutdown(), this.isTerminated()));
    }
}
