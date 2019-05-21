


##  ANR

Application Not Responding

#### 1. 分类

* KeyDispatchTimeout 按键交互响应超时，默认 5s

* BroadcastTimeout 超时，默认前台10s，后台60s

* ServiceTimeOut 超时，默认前台20s，后台200s

* ContentProvider  超时，默认前台20s，后台200s

主线程里面都尽量要避免耗时的操作,比如下面这些都是执行在主线程的

    Activity的所有生命周期回调都是执行在主线程的.
    Service默认是执行在主线程的.
    BroadcastReceiver的onReceive回调是执行在主线程的.
    没有使用子线程的looper的Handler的handleMessage, post(Runnable)是执行在主线程的.
    AsyncTask的回调中除了doInBackground, 其他都是执行在主线程的.
    View的post(Runnable)是执行在主线程的.

#### 2. ANR机制
Android对于不同的ANR类型(Broadcast, Service, InputEvent)都有一套监测机制，

在监测到ANR以后，需要显示ANR对话框、输出日志(发生ANR时的进程函数调用栈、CPU使用情况等)。
整个ANR机制的代码也是横跨了Android的几个层：app -> framework -> Native

##### 2.1 ANR监测机制

###### 2.1.1 Srevice处理超时

当Service的生命周期开始时，通过AMS.MainHandler抛出一个定时消息，当Service的生命周期结束时，
之前抛出的SERVICE_TIMEOUT_MSG消息在这个方法中会被清除。 如果在超时时间内，SERVICE_TIMEOUT_MSG
没有被清除，那么，AMS.MainHandler就会响应这个消息， 然后报告ANR，最终调用AMS.appNotResponding()方法

###### 2.1.2 Broadcast处理超时
广播消息的调度
AMS维护了两个广播队列BroadcastQueue:

foreground queue，前台队列的超时时间是10秒

background queue，后台队列的超时时间是60秒

所有发送的广播都会进入到队列中等待调度,，在发送广播时，可以通过Intent.FLAG_RECEIVER_FOREGROUND参数
将广播投递到前台队列。 AMS线程会不断地从队列中取出广播消息派发到各个接收器,

。。。


###### 2.1.3 Input处理超时
InputDispatcher - InputChannel - 窗口
InputChannel其实是封装后的Linux管道(Pipe)。 每一个窗口都会有一个独立的InputChannel，窗口需要将这个
InputChannel注册到InputDispatcher


<pre>
场景1: 窗口处于paused状态，不能处理输入事件

“Waiting because the [targetType] window is paused.”

场景2: 窗口还未向InputDispatcher注册，无法将事件派发到窗口

“Waiting because the [targetType] window’s input channel is not registered with the input dispatcher. The window may be in the process of being removed.”

场景3: 窗口和InputDispatcher的连接已经中断，即InputChannel不能正常工作

“Waiting because the [targetType] window’s input connection is [status]. The window may be in the process of being removed.”

场景4: InputChannel已经饱和，不能再处理新的事件

“Waiting because the [targetType] window’s input channel is full. Outbound queue length: %d. Wait queue length: %d.”

场景5: 对于按键类型(KeyEvent)的输入事件，需要等待上一个事件处理完毕

“Waiting to send key event because the [targetType] window has not finished processing all of the input events that were previously delivered to it. Outbound queue length: %d. Wait queue length: %d.”

场景6: 对于触摸类型(TouchEvent)的输入事件，可以立即派发到当前的窗口，因为TouchEvent都是发生在用户当前可见的窗口。但有一种情况， 如果当前应用由于队列有太多的输入事件等待派发，导致发生了ANR，那TouchEvent事件就需要排队等待派发。

“Waiting to send non-key event because the %s window has not finished processing certain input events that were delivered to it over %0.1fms ago. Wait queue length: %d. Wait queue head age: %0.1fms.”
</pre>


##### 2.2 ANR报告机制



#### 3. 原因
- 主线程阻塞或主线程数据读取

避免死锁的出现，使用子线程来处理耗时操作或阻塞任务。尽量避免在主线程query provider、不要滥用SharePreferenceS

<pre>
主线程被其他线程lock，导致死锁

DALVIK THREADS:
"main" prio=5 tid=3 TIMED_WAIT
  | group="main" sCount=1 dsCount=0 s=0 obj=0x400143a8
  | sysTid=691 nice=0 sched=0/0 handle=-1091117924
  at java.lang.Object.wait(Native Method)
  - waiting on <0x1cd570> (a android.os.MessageQueue)
  at java.lang.Object.wait(Object.java:195)
</pre>


<pre>
主线程做耗时的操作：比如数据库读写

"main" prio=5 tid=1 Native
held mutexes=
native: #16 pc 0000fa29 /system/lib/libsqlite.so (???)
native: #17 pc 0000fad7 /system/lib/libsqlite.so (sqlite3_prepare16_v2+14)
native: #18 pc 0007f671 /system/lib/libandroid_runtime.so (???)
native: #19 pc 002b4721 /system/framework/arm/boot-framework.oat (Java_android_database_sqlite_SQLiteConnection_nativePrepareStatement__JLjava_lang_String_2+116)
at android.database.sqlite.SQLiteDatabase.openInner(SQLiteDatabase.java:808)
locked <0x0db193bf> (a java.lang.Object)
at android.database.sqlite.SQLiteDatabase.open(SQLiteDatabase.java:793)
</pre>

- CPU满负荷，I/O阻塞

文件读写或数据库操作放在子线程异步操作。

<pre>
这个时候你看到的trace信息可能会包含这样的信息:

Process:com.anly.githubapp
...
CPU usage from 3330ms to 814ms ago:
6% 178/system_server: 3.5% user + 1.4% kernel / faults: 86 minor 20 major
4.6% 2976/com.anly.githubapp: 0.7% user + 3.7% kernel /faults: 52 minor 19 major
0.9% 252/com.android.systemui: 0.9% user + 0% kernel
...

100%TOTAL: 5.9% user + 4.1% kernel + 89% iowait
</pre>

- 内存不足

<pre>
这时trace信息可能是这样的:
// 以下trace信息来自网络, 用来做个示例
Cmdline: android.process.acore

DALVIK THREADS:
"main"prio=5 tid=3 VMWAIT
|group="main" sCount=1 dsCount=0 s=N obj=0x40026240self=0xbda8
| sysTid=1815 nice=0 sched=0/0 cgrp=unknownhandle=-1344001376
atdalvik.system.VMRuntime.trackExternalAllocation(NativeMethod)
atandroid.graphics.Bitmap.nativeCreate(Native Method)
atandroid.graphics.Bitmap.createBitmap(Bitmap.java:468)
atandroid.view.View.buildDrawingCache(View.java:6324)
atandroid.view.View.getDrawingCache(View.java:6178)

...

MEMINFO in pid 1360 [android.process.acore] **
native dalvik other total
size: 17036 23111 N/A 40147
allocated: 16484 20675 N/A 37159
free: 296 2436 N/A 2732
</pre>

- 各大组件ANR

例如：注意BroadcastReciever的onRecieve()、后台Service和ContentProvider也不要执行太长时间的任务



[理解Android ANR的触发原理](http://gityuan.com/2016/07/02/android-anr/)



#### 4. 排查分析的思路：

1. 查看events_log
    
    从日志中搜索关键字：am_anr，找到出现ANR的时间点、进程PID、ANR类型。
    
    如日志：
    <pre>

    07-20 15:36:36.472  1000  1520  1597 I am_anr  : [0,1480,com.xxxx.moblie,952680005,
    Input dispatching timed out (AppWindowToken{da8f666 token=Token{5501f51 ActivityRecord
    {15c5c78 u0 com.xxxx.moblie/.ui.MainActivity t3862}}}, Waiting because no window has 
    focus but there is a focused application that may eventually add a window when it 
    finishes starting up.)]
    
    从上面的log我们可以看出： 应用com.xxxx.moblie 在07-20 15:36:36.472时间，发生了一次
        KeyDispatchTimeout类型的ANR：
    ANR时间：07-20 15:36:36.472
    进程pid：1480
    进程名：com.xxxx.moblie
    ANR类型：KeyDispatchTimeout 
    </pre>

2. traces.txt 
    
    adb pull data/anr/traces.txt 
    <pre>
    包括进程名、进程号、包名、系统build号、ANR 类型等等
    
    CPU使用信息，包括活跃进程的CPU 平均占用率、IO情况等等.
    
    "load average"，它的意思是"系统的平均负荷",它们的意思分别是1分钟、5分钟、15分钟内系统的平均负荷
    
    Load: 40.53 / 27.06 / 20.66
    
    当CPU完全空闲的时候，平均负荷为0；当CPU工作量饱和的时候，平均负荷为1。
    那么很显然，"load average"的值越低，比如等于0.2或0.3，就说明电脑的工作量越小，系统负荷比较轻。
        
 
    "main" tid=线程内部id  prio=线程优先级  Native(线程状态):
     | group="线程所属的线程组"  sCount=线程挂起次数  dsCount=用于调试的线程挂起次数 obj=当前线程关联的java线程对象 self=当前线程地址
     | sysTid=线程真正意义上的tid  nice=调度有优先级 cgrp=进程所属的进程调度组 sched=调度策略 handle=函数处理地址
     | state=线程状态 schedstat= CPU调度时间统计( 759680053 37680105100 3160 ) utm=用户态/内核态的CPU时间 stm=20 core=该线程的最后运行所在核 HZ=时钟频率
     | stack=线程栈的地址区间 stackSize=栈的大小
     | held mutexes=所持有mutex类型，有独占锁exclusive和共享锁shared两类
     
     "main" tid=1 :
     | group="main" sCount=1 dsCount=0 obj=0x738cae28 self=0xf4827400
     | sysTid=28568 nice=10 cgrp=bg_non_interactive sched=0/0 handle=0xf7495bec
     | state=S schedstat=( 759680053 37680105100 3160 ) utm=55 stm=20 core=3 HZ=100
     | stack=0xff27a000-0xff27c000 stackSize=8MB
     | held mutexes=
     
     nice值越小则优先级越高。此处nice=10,
     
     schedstat=( 186667489018 37680105100 3160 ) utm=12112 stm=6554
     schedstat括号中的3个数字依次是Running、Runable、Switch，紧接着的是utm和stm
    
    Running时间：CPU运行的时间，单位ns
    Runable时间：RQ队列的等待时间，单位ns
    Switch次数：CPU调度切换次数
    utm: 该线程在用户态所执行的时间，单位是jiffies，jiffies定义为sysconf(_SC_CLK_TCK)，默认等于10ms
    stm: 该线程在内核态所执行的时间，单位是jiffies，默认等于10ms
    
    utm + stm = (12112 + 6554) ×10 ms = 186666ms utm + stm = schedstat第一个参数值
     
    </pre>

3. 报错的log

    main log，通过检索”ANR in “关键字，可以找到ANR的信息，日志的上下文会包含CPU的使用情况
    dropbox，通过检索”anr”类型，可以找到ANR的信息
    
    android系统会自动帮我们生成一个log日志输出文件，在data/system/dropbox/下，真机测试需要root权限，
    模拟器在DDMS下可以查看
    
    [6类ANR实例剖析](https://www.cnblogs.com/Jokeyyu/p/9101879.html)

#### 5. 我们以bugly上的ANR为例

[demo](https://bugly.qq.com/v2/crash-reporting/blocks/4c87c7482a/1754087?pid=1)

在跟踪数据下，一般会有三个附件信息

1. trace.zip 这个就是traces文件，我们可以复制下来，
    
    <pre>
    
    有些log是没有线程优先级跟线程状态的
    
    "main" tid=1 :
     | group="main" sCount=1 dsCount=0 obj=0x738cae28 self=0xf4827400
     | sysTid=28568 nice=10 cgrp=bg_non_interactive sched=0/0 handle=0xf7495bec
     | state=S schedstat=( 759680053 37680105100 3160 ) utm=55 stm=20 core=3 HZ=100
     | stack=0xff27a000-0xff27c000 stackSize=8MB
     | held mutexes=
     native: #00 pc 00013494 /system/lib/libc.so (syscall 28)
     native: #01 pc 000a9c73 /system/lib/libart.so (_ZN3art17ConditionVariable4WaitEPNS_6ThreadE 82)
     native: #02 pc 0026b26f /system/lib/libart.so (artFindNativeMethod 1342)
     native: #03 pc 000a1895 /system/lib/libart.so (art_jni_dlsym_lookup_stub 4)
     native: #04 pc 00e39de5 /data/dalvik-cache/arm/data@app@com.kmxs.reader-2@base.apk@classes.dex (Java_com_networkbench_nbslens_nativecrashlib_NativeInterface_initNativeCrash__ 80)
     at com.networkbench.nbslens.nativecrashlib.NativeInterface.initNativeCrash(Native method)
     at com.networkbench.agent.impl.crash.NativeCrashInterface.initNativeCrash(SourceFile:41)
     at com.networkbench.agent.impl.NBSAppAgent.start(SourceFile:302)
     - locked <@addr=0x23116430> (a com.networkbench.agent.impl.NBSAppAgent)
     at com.kmxs.reader.b.b.k.b(InitTingYunTask.java:34)
     at com.kmxs.reader.b.a.c.a.run(DispatchRunnable.java:50)
     at com.kmxs.reader.b.a.b.g(TaskDispatcher.java:128)
     at com.kmxs.reader.b.a.b.b(TaskDispatcher.java:114)
     at com.kmxs.reader.app.MainApplication.initTask(MainApplication.java:142)
     at com.kmxs.reader.app.MainApplication.onCreate(MainApplication.java:116)
    </pre>                      

2. valueMapOthers.txt 

3. anrMessage.txt 
    
    列出了一些基本信息
    <pre>
    ANR in com.kmxs.reader, time=86807575
    Reason: executing service com.kmxs.reader/com.liulishuo.filedownloader.services.FileDownloadService$SharedMainProcessService
    Load: 40.53 / 27.06 / 20.66
    Android time :[2019-05-17 20:13:15.17] [86814.367]
    CPU usage from 5690ms to 318ms ago:
    
    100% TOTAL: 78% user  21% kernel  0% iowait  0% softirq
    最后一句表明了:
    CPU占用100%, 满负荷了.
    其中绝大数是被user 占用了.
    </pre>
    
    我们可以看到CPU使用量接近100%，说明当前设备很忙，有可能是CPU饥饿导致了ANR，FileDownloadService是在application初始化时调用，
    再结合trace.zip文件
    
     locked <@addr=0x23116430> (a com.networkbench.agent.impl.NBSAppAgent)
    
    就可以猜测应该是初始化时主线程做耗时的操作,cpu使用过高导致了ANR，然后再具体问题具体分析



[demo2](https://bugly.qq.com/v2/crash-reporting/blocks/4c87c7482a/1754452?pid=1)

<pre>

# main(1)

Input dispatching timed out

ANR Input dispatching timed out (Waiting because the focused window has not finished processing the input events that were previously delivered to it.)

解析原始
1 android.widget.TextView.setText(TextView.java:3685)
2 android.widget.TextView.setText(TextView.java:3671)
3 android.widget.TextView.setText(TextView.java:3646)
4 com.kmxs.reader.readerad.g.c(ViewManager.java:209)
5 com.kmxs.reader.readerad.g.d(ViewManager.java:379)
6 com.kmxs.reader.readerad.d.a(OverSlideViewManager.java:109)
7 com.kmxs.reader.readerad.g.g(ViewManager.java:646)
8 com.kmxs.reader.readerad.ReaderLayout.onDrawInScrolling(ReaderLayout.java:321)
9 com.kmxs.reader.readerad.ReaderLayout.onAnimationAbort(ReaderLayout.java:508)
10 com.kmxs.reader.readerad.a.a.a(AnimationProvider.java:406)
11 com.kmxs.reader.readerad.widget.ReaderLayoutWidget.startManualScrolling(ReaderLayoutWidget.java:215)
12 com.kmxs.reader.readerad.widget.ReaderLayoutWidget.onInterceptTouchEvent(ReaderLayoutWidget.java:99)

ANR in com.kmxs.reader (com.kmxs.reader/org.geometerplus.android.fbreader.FBReader)
PID: 26083
Reason: Input dispatching timed out (Waiting because the focused window has not finished processing the input events that were previously delivered to it.)
Load: 90.06 / 122.32 / 105.9
CPU usage from 0ms to 14957ms later:
 0% 12048/kworker/0:0H: 0% user  0% kernel
 0% 5930/sogou.mobile.explorer.fangbei:patch_service: 0% user  0% kernel
 0% 6046/com.readboy.MyMp3: 0% user  0% kernel
 0% 6077/logcat: 0% user  0% kernel
99% TOTAL: 20% user  59% kernel  18% iowait  1.6% softirq
</pre>

说明这CPU不足导致无法相应下一个input events导致ANR。我们再结合代码查看发现是点击事件ACTION_DOWN时触发翻页，并且填充内容或者广告数据时发生的ANR
我们再看看堆栈信息  

4 com.kmxs.reader.readerad.g.c(ViewManager.java:209) 对应到代码里面,然后再具体问题具体分析：
        
adViewHolder.tv_no_ad_read.setText(CommonMethod.watchVideoFreeAdDesc());这边涉及到动画的执行，sp文件的读取


[demo3](https://www.baidu.com)
<pre>

W Watchdog: *** WATCHDOG KILLING SYSTEM PROCESS: Blocked in monitor com.android.server.wm.WindowManagerService on foreground thread (android.fg)

Watchdog告诉我们Monitor Checker超时了，具体在哪呢？ 名为android.fg的线程在WindowManagerService的monitor()方法被阻塞了。这里隐含了两层意思：

WindowManagerService实现了Watchdog.Monitor这个接口，并将自己作为Monitor Checker的对象加入到了Watchdog的监测集中

monitor()方法是运行在android.fg线程中的。Android将android.fg设计为一个全局共享的线程，意味着它的消息队列可以被其他线程共享， Watchdog的Monitor Checker就是使用的android.fg线程的消息队列。因此，出现Monitor Checker的超时，肯定是android.fg线程阻塞在monitor()方法上。

我们打开system_server进程的traces，检索 android.fg 可以快速定位到该线程的函数调用栈：
"android.fg" prio=5 tid=25 Blocked
  | group="main" sCount=1 dsCount=0 obj=0x12eef900 self=0x7f7a8b1000
  | sysTid=973 nice=0 cgrp=default sched=0/0 handle=0x7f644e9000
  | state=S schedstat=( 3181688530 2206454929 8991 ) utm=251 stm=67 core=1 HZ=100
  | stack=0x7f643e7000-0x7f643e9000 stackSize=1036KB
  | held mutexes=
  at com.android.server.wm.WindowManagerService.monitor(WindowManagerService.java:13125)
  - waiting to lock <0x126dccb8> (a java.util.HashMap) held by thread 91
  at com.android.server.Watchdog$HandlerChecker.run(Watchdog.java:204)
  at android.os.Handler.handleCallback(Handler.java:815)
  
  android.fg线程调用栈告诉我们几个关键的信息：

这个线程当前的状态是Blocked，阻塞
由Watchdog发起调用monitor()，这是一个Watchdog检查，阻塞已经超时
waiting to lock <0x126dccb8>： 阻塞的原因是monitor()方法中在等锁<0x126dccb8>
held by thread 91： 这个锁被编号为91的线程持有，需要进一步观察91号线程的状态。

可以在traces.txt文件中检索 tid=91 来快速找到91号线程的函数调用栈信息：
"Binder_C" prio=5 tid=91 Native
  | group="main" sCount=1 dsCount=0 obj=0x12e540a0 self=0x7f63289000
  | sysTid=1736 nice=0 cgrp=default sched=0/0 handle=0x7f6127c000
  | state=S schedstat=( 96931835222 49673449591 260122 ) utm=7046 stm=2647 core=2 HZ=100
  | stack=0x7f5ffbc000-0x7f5ffbe000 stackSize=1008KB
  | held mutexes=
  at libcore.io.Posix.writeBytes(Native method)
  at libcore.io.Posix.write(Posix.java:258)
  at libcore.io.BlockGuardOs.write(BlockGuardOs.java:313)
  at libcore.io.IoBridge.write(IoBridge.java:537)
  at java.io.FileOutputStream.write(FileOutputStream.java:186)
  at com.android.internal.util.FastPrintWriter.flushBytesLocked(FastPrintWriter.java:334)
  at com.android.internal.util.FastPrintWriter.flushLocked(FastPrintWriter.java:355)
  at com.android.internal.util.FastPrintWriter.appendLocked(FastPrintWriter.java:303)
  at com.android.internal.util.FastPrintWriter.print(FastPrintWriter.java:466)
  - locked <@addr=0x134c4910> (a com.android.internal.util.FastPrintWriter$DummyWriter)
  at com.android.server.wm.WindowState.dump(WindowState.java:1510)
  at com.android.server.wm.WindowManagerService.dumpWindowsNoHeaderLocked(WindowManagerService.java:12279)
  at com.android.server.wm.WindowManagerService.dumpWindowsLocked(WindowManagerService.java:12266)
  at com.android.server.wm.WindowManagerService.dump(WindowManagerService.java:12654)
  - locked <0x126dccb8> (a java.util.HashMap)
  at android.os.Binder.dump(Binder.java:324)
  at android.os.Binder.onTransact(Binder.java:290)
  
  91号线程的名字是Binder_C，它的函数调用栈告诉我们几个关键信息：

Native，表示线程处于运行状态(RUNNING)，并且正在执行JNI方法
在WindowManagerService.dump()方法申请了锁<0x126dccb8>，这个锁正是android.fg线程所等待的
FileOutputStream.write()表示Binder_C线程在执行IO写操作，正式因为这个写操作一直在阻塞，导致线程持有的锁不能释放

</pre>

[demo4](https://duanqz.github.io/2015-10-12-Watchdog-Analysis#31-%E6%97%A5%E5%BF%97%E8%8E%B7%E5%8F%96)


分析cpu、io、锁等

JPS

jstack pid
























## CPU性能
很多时候在使用APP的时候，手机可能会发热发烫。这是因为CPU使用率过高，CPU过于繁忙，会使整个手机无法响应用户，
整体性能降低，用户体验就会很差，也容易引起ANR等等一系列问题

在Linux系统下，CPU利用率分为用户态、系统态、空闲态，分别表示CPU处于用户态执行的时间，系统内核执行的时间，和空闲系统进程执行的时间

<table>
    <tr>
      <th></th>
      <th>用户态</th>
      <th>系统态</th>
      <th>空闲态</th>
    </tr>
    <tr>
      <td>CPU利用率</td>
      <td>用户态执行的时间</td>
      <td>系统内核执行的时间</td>
      <td>空闲系统进程执行的时间</td>
    </tr>
    <tr>
      <td colspan="4">平时所说的CPU利用率是指：CPU执行非系统空闲进程的时间 / CPU总的执行时间。</td>
    </tr>
</table>
	
cpu时间 = user + nice + system + idle + iowait + irq + softirq

<table>
    <tr>
      <th>列名</th>
      <th>描述</th>
    </tr>
    <tr>
      <td>user</td>
      <td>从系统启动开始累计到当前时刻，处于用户态的运行时间，不包含 nice值为负进程</td>
    </tr>
    <tr>
      <td>nice</td>
      <td>从系统启动开始累计到当前时刻，nice值为负的进程所占用的CPU时间</td>
    </tr>
    <tr>
      <td>idle</td>
      <td>从系统启动开始累计到当前时刻，除IO等待时间以外的其它等待时间</td>
    </tr>
    <tr>
      <td>iowait</td>
      <td>从系统启动开始累计到当前时刻，IO等待时间</td>
    </tr>
    <tr>
      <td>system</td>
      <td>从系统启动开始累计到当前时刻，处于核心态的运行时间</td>
    </tr>
    <tr>
      <td>irq</td>
      <td>从系统启动开始累计到当前时刻，硬中断时间</td>
    </tr>
    <tr>
      <td>softirq</td>
      <td>从系统启动开始累计到当前时刻，软中断时间</td>
    </tr>
</table>









