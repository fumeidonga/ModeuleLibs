
在了解启动流程前，先要了解下 Android          开机流程.md


我们先来看个开机的启动图, 如图中红色箭头

![](https://github.com/fumeidonga/markdownPic/blob/master/yuanli/android_start.png?raw=true)

再来看个 app启动的时序图

![](https://github.com/fumeidonga/markdownPic/blob/master/yuanli/launcher_app.png?raw=true)

## 概念
每个Android App都在一个独立空间里, 意味着其运行在一个单独的进程中, 拥有自己的VM, 被系统分配一个唯一的user ID.
Android App由很多不同组件组成, 这些组件还可以启动其他App的组件. 因此, Android App并没有一个类似程序入口的main()方法.

Android进程与Linux进程一样. 默认情况下, 每个apk运行在自己的Linux进程中. 另外, 默认一个进程里面只有一个线程---主线程.
 这个主线程中有一个Looper实例, 通过调用Looper.loop()从Message队列里面取出Message来做相应的处理.

应用程序进程 ： ActivityManagerService在启动应用程序时会检查这个应用程序需要的应用程序进程是否存在，不存在就会请求Zygote进程将需要的应用程序进程启动，
Zygote的Java框架层中，会创建一个Server端的Socket，这个Socket用来等待ActivityManagerService来请求Zygote来创建新的应用程序进程，Zygote进程通过fock自身
创建的应用程序进程，这样应用程序程序进程就会获得Zygote进程在启动时创建的虚拟机实例，还可以获得Binder线程池和消息循环，

system_server进程： 我们在手机启动时，在init线程里面创建的一个非常重要的service，Android中的所有服务都是由这个SystemServer fork出来的。

ActivityManagerService：（AMS）AMS是Android中最核心的服务之一，主要负责系统中四大组件的启动、切换、调度及应用进程的管理和调度等工作，其职责与操作系统中的进程管理和调度模块相类似，因此它在Android中非常重要，它本身也是一个Binder的实现类。

Instrumentation：监控应用程序和系统的交互；

ActivityThread：应用的入口类，通过调用main方法，开启消息循环队列。ActivityThread所在的线程被称为主线程；

ApplicationThread：ApplicationThread提供Binder通讯接口，AMS则通过代理调用此App进程的本地方法

ActivityManagerProxy：AMS服务在当前进程的代理类，负责与AMS通信。

ApplicationThreadProxy：ApplicationThread在AMS服务中的代理类，负责与ApplicationThread通信

















