


<pre>
<code>

public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue)

public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory)

public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler)

public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
</code>
</pre>

##### corePoolSize
核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存

keepAliveTime限制。除非将allowCoreThreadTimeOut设置为true。

##### maximumPoolSize
线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小

的LinkedBlockingDeque时，这个值无效。

##### keepAliveTime
非核心线程的闲置超时时间，超过这个时间就会被回收。

##### Timeunit
指定keepAliveTime的单位，如TimeUnit.SECONDS。当将allowCoreThreadTimeOut

设置为true时对corePoolSize生效。
<pre>
     TimeUnit.DAYS;               //天
     TimeUnit.HOURS;             //小时
     TimeUnit.MINUTES;           //分钟
     TimeUnit.SECONDS;           //秒
     TimeUnit.MILLISECONDS;      //毫秒
     TimeUnit.MICROSECONDS;      //微妙
     TimeUnit.NANOSECONDS;       //纳秒
     </pre>

##### workQueue
线程池中的任务队列.
常用的有三种队列，SynchronousQueue,LinkedBlockingDeque,ArrayBlockingQueue。
<pre>
ArrayBlockingQueue;//内部维护一个数组，FIFO策略
LinkedBlockingQueue;//队列使用FIFO策略，内部维护了一个单向链表，
    默认最大容量是Integer.MAX_VALUE，动态生成节点
    所以，线程池最多只有corePoolSize个thread被创建，其他都会在queue中被阻塞
    适用场景，确保每个请求都能被执行，不被拒绝
SynchronousQueue;//相当于队列长度为0，因此只要达到 maximumPoolSize
    就会拒绝新提交的任务
</pre>
##### threadFactory

<code>

newCachedThreadPool()

newFixedThreadPool

newScheduledThreadPool
</code>

