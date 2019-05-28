
在了解启动流程前，先要了解下 Android          开机流程.md


我们先来看个图, 如图中红色箭头

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/android_start.png?raw=true)

## 概念
每个Android App都在一个独立空间里, 意味着其运行在一个单独的进程中, 拥有自己的VM, 被系统分配一个唯一的user ID.
Android App由很多不同组件组成, 这些组件还可以启动其他App的组件. 因此, Android App并没有一个类似程序入口的main()方法.

Android进程与Linux进程一样. 默认情况下, 每个apk运行在自己的Linux进程中. 另外, 默认一个进程里面只有一个线程---主线程.
 这个主线程中有一个Looper实例, 通过调用Looper.loop()从Message队列里面取出Message来做相应的处理.




















