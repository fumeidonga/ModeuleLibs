
[systrace](http://developer.android.com/tools/debugging/systrace.html)

systrace是Android4.1版本之后推出的，对系统Performance分析的工具
systrace的功能包括跟踪系统的I/O操作、内核工作队列、CPU负载以及Android各个子系统的运行状况等
Systrace允许你监视和跟踪Android系统的行为(trace)。它会告诉你系统都在哪些工作上花费时间、CPU周期都用在哪里，甚至你可以看到每个线程、进程在指定时间内都在干嘛。它同时还会突出观测到的问题，从垃圾回收到渲染内容都可能是问题对象，甚至提供给你建议的解决方案。


### 1.以命令行的形式运行 run command line

[官网：](http://developer.android.com/tools/help/systrace.html#options)

1）安装python
https://www.python.org/downloads/windows/

2）安装pip
https://pypi.org/project/pip/#files
1.	下载pip-19.0.3.tar.gz
2.	解压，然后进入cmd，进入到解压的目录并执行下面的命令
python setup.py install
3.	安装完成后，在控制台输入 python -m pip --version 命令，
如果显示‘pip’不是内部命令，也不是可运行的程序，说明，缺少环境变量，需要添加python环境变量
C:\Python27\Scripts
C:\Python27

3）安装pywin32

pip install pywin32

python <环境根目录>\Scripts\pywin32_postinstall.py –install

4）cmd 命令行

cmd 进入到sdk/platform-tools/systrace目录

python systrace.py [options] [categories]

python systrace.py [参数] [类别]

各种参数：
要查看已连接设备支持的类别列表
python systrace.py --list-categories 或者
python systrace.py –l

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_1.png?raw=true)


![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_3.png?raw=true)

例如：

python systrace.py -o mynewtrace.html sched freq idle am wm gfx view binder_driver hal dalvik camera input res app

这里根据提示停止



4.3以上

$ cd android-sdk/platform-tools/systrace

python systrace.py –a com.kmxs.reader -o mynewtrace.html sched freq idle am wm gfx view dalvik input res app

$ python systrace.py –b 32768 –t 5 –a com.kmxs.reader -o mynewtrace.html sched freq idle am wm gfx view dalvik input res app

这里5s自动停止


4.2以下

first
$ cd android-sdk/platform-tools/systrace
$ python systrace.py --set-tags=gfx,view,wm
then
$ adb shell stop
$ adb shell start

### 2. 图形界面使用
首先，我们等安装python环境，并且4.1 以上
在android studio(3.0)中，打开Android Device Monitor （DDMS）

然后选择进程 ，点击systrace按钮：

在弹出的菜单框中填写以下几项东西


Destination File: 文件的保存位置
Trace Duration: 时间 默认是5S
Trace Buffer size:缓冲的大小，默认不限制
勾选需要跟踪的项

### 3.在代码中插入自定义的类型

在release版本中，我们需要添加以下代码这样就可以手动开启App自定义Label的Trace功能，在非debuggable的版本中也适用
在Application的`attachBaseContext` 中添加
Class<?> trace = Class.forName("android.os.Trace");
Method setAppTracingAllowed = trace.getDeclaredMethod("setAppTracingAllowed", boolean.class);
setAppTracingAllowed.invoke(null, true);

### 4. Android 9 以上，
On devices running Android 9 (API level 28) or higher, you can use a system app called System Tracing to Record system traces on device.

https://developer.android.com/studio/profile/systrace-on-device




## 分析systrace
Google Chrome浏览器可以打开systrace，如果打不开，可以通过chrome://tracing/，然后load systrace。

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_2.png?raw=true)


https://www.jianshu.com/p/3b9650307633

https://maoao530.github.io/2017/02/06/systrace/


























