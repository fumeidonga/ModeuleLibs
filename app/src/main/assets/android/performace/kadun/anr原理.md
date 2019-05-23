

#### 1. ANR机制
ANR的本质是一个性能问题，即主线程中的耗时操作造成主线程堵塞，导致应用失去响应能力

Android对于不同的ANR类型(Broadcast, Service, InputEvent)都有一套监测机制，

在监测到ANR以后，需要显示ANR对话框、输出日志(发生ANR时的进程函数调用栈、CPU使用情况等)。
整个ANR机制的代码也是横跨了Android的几个层：app -> framework -> Native

##### 1.1 ANR监测机制
[ANR机制以及问题分析](https://duanqz.github.io/2015-10-12-ANR-Analysis)

Framework层： ANR机制的核心

    frameworks/base/services/core/java/com/android/server/am/ActivityManagerService.java
    frameworks/base/services/core/java/com/android/server/am/BroadcastQueue.java
    frameworks/base/services/core/java/com/android/server/am/ActiveServices.java
    frameworks/base/services/core/java/com/android/server/input/InputManagerService.java
    frameworks/base/services/core/java/com/android/server/wm/InputMonitor.java
    frameworks/base/core/java/android/view/InputChannel
    frameworks/base/services/core/java/com/android/internal/os/ProcessCpuTracker
    
    Native层：输入事件派发机制。针对InputEvent类型的ANR
   
    frameworks/base//services/core/jni/com_android_server_input_InputManagerService.cpp
    frameworks/native/services/inputflinger/InputDispatcher.cpp

###### 1.1.1 Srevice处理超时

当Service的生命周期开始时，通过AMS.MainHandler抛出一个定时消息，当Service的生命周期结束时，
之前抛出的SERVICE_TIMEOUT_MSG消息在这个方法中会被清除。 如果在超时时间内，SERVICE_TIMEOUT_MSG
没有被清除，那么，AMS.MainHandler就会响应这个消息， 然后报告ANR，最终调用AMS.appNotResponding()方法

ActivityService.java

ActivityManagerService.java 等


###### 1.1.2 Broadcast处理超时

当ActivityManager里面的BroadcastQueue.BroadcastHandler收到BROADCAST_TIMEOUT_MSG消息是触发ANR,
最终调用AMS.appNotResponding()方法

广播消息的调度
AMS维护了两个广播队列BroadcastQueue:

foreground queue，前台队列的超时时间是10秒

background queue，后台队列的超时时间是60秒

所有发送的广播都会进入到队列中等待调度,，在发送广播时，可以通过Intent.FLAG_RECEIVER_FOREGROUND参数
将广播投递到前台队列。 AMS线程会不断地从队列中取出广播消息派发到各个接收器,
当要派发广播时，AMS会调用BroadcastQueue.scheduleBroadcastsLocked()

。。。

broadcast跟service超时机制大抵相同，但有一个非常隐蔽的技能点，那就是通过静态注册的广播超时会
受SharedPreferences(简称SP)的影响
<pre>
public final void finish() {
    if (mType == TYPE_COMPONENT) {
        final IActivityManager mgr = ActivityManager.getService();
        if (QueuedWork.hasPendingWork()) {
            //当SP有未同步到磁盘的工作，则需等待其完成，才告知系统已完成该广播
            QueuedWork.queue(new Runnable() {
                public void run() {
                    sendFinished(mgr);
                }
            }, false);
        } else {
            sendFinished(mgr);
        }
    } else if (mOrderedHint && mType != TYPE_UNREGISTERED) {
        final IActivityManager mgr = ActivityManager.getService();
        sendFinished(mgr);
    }
}
</pre>

###### 1.1.3 Input处理超时
InputDispatcher - InputChannel - 窗口
InputChannel其实是封装后的Linux管道(Pipe)。 每一个窗口都会有一个独立的InputChannel，窗口需要将这个
InputChannel注册到InputDispatcher


###### 1.1.4 ContentProvider
ContentProvider Timeout是位于”ActivityManager”线程中的AMS.MainHandler收到CONTENT_PROVIDER_PUBLISH_TIMEOUT_MSG消息时触发。

Provider是在进程创建的过程中设置超时,进程创建后会调用attachApplicationLocked()进入system_server进程

##### 1.2 ANR报告机制























