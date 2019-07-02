
## 简介
[systrace](http://developer.android.com/tools/debugging/systrace.html)

systrace是Android4.1版本之后推出的，对系统Performance分析的工具
systrace的功能包括跟踪系统的I/O操作、内核工作队列、CPU负载以及Android各个子系统的运行状况等
Systrace允许你监视和跟踪Android系统的行为(trace)。它会告诉你系统都在哪些工作上花费时间、CPU周期都用在哪里，甚至你可以看到每个线程、进程在指定时间内都在干嘛。它同时还会突出观测到的问题，从垃圾回收到渲染内容都可能是问题对象，甚至提供给你建议的解决方案。

一般用途

- 布局优化时，查看frames & Alerts ， 这里面有些提示信息，frames中绿色代表正常，其他颜色表示丢帧

- 卡顿优化时，查看cpu的运行程度，有没有跑满等


## 获取systrace文件
### 1.以命令行的形式运行 run command line

[官网：](http://developer.android.com/tools/help/systrace.html#options)

1. 安装python

    https://www.python.org/downloads/windows/

2. 安装pip

    https://pypi.org/project/pip/#files

	1.	下载pip-19.0.3.tar.gz
	2.	解压，然后进入cmd，进入到解压的目录并执行下面的命令
	python setup.py install
	3.	安装完成后，在控制台输入 python -m pip --version 命令，
	如果显示‘pip’不是内部命令，也不是可运行的程序，说明，缺少环境变量，需要添加python环境变量
	C:\Python27\Scripts
	C:\Python27

3. 安装pywin32

	pip install pywin32
	
	python <环境根目录>\Scripts\pywin32_postinstall.py –install

4. cmd 命令行

	cmd 进入到sdk/platform-tools/systrace目录
	
	python systrace.py [options] [categories]
	
	python systrace.py [参数] [类别]
	
	要查看已连接设备支持的类别列表
	python systrace.py --list-categories 或者
	python systrace.py –-help

	[类别]
	
	![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_1.png?raw=true)
	
	[参数]
	
	<table>
	    <tr>
	      <th>参数</th>
	      <th>作用</th>
	    </tr>
	    <tr>
	      <td>-h | --help </td>
	      <td>帮助信息</td>
	    </tr>
	    <tr>
	      <td>-l | --list-categories  </td>
	      <td>查看已连接设备支持的类别</td>
	    </tr>
	    <tr>
	      <td>-o file.html  </td>
	      <td>需要保存的文件名，默认trace.html</td>
	    </tr>
	    <tr>
	      <td>-t N | --time=N    </td>
	      <td>跟踪设备的时间，没有的话则根据提示来</td>
	    </tr>
	    <tr>
	      <td>-b N | --buf-size=N </td>
	      <td>缓冲区的大小</td>
	    </tr>
	    <tr>
	      <td>-a  | --app=app_name </td>
	      <td>app name</td>
	    </tr>
	    <tr>
	      <td>-k functions | --ktrace=functions   </td>
	      <td>跟踪特定内核的函数</td>
	    </tr>
	    <tr>
	      <td>--from-file=file-path </td>
	      <td></td>
	    </tr>
	    <tr>
	      <td>-e device-serial | --serial=device-serial</td>
	      <td></td>
	    </tr>
	</table>
	
	
	例如：
	
	python systrace.py  -o mynewtrace.html sched freq idle am wm gfx view binder_driver hal dalvik camera input res app

	python systrace.py –a com.kmxs.reader -o mynewtrace.html sched freq idle am wm gfx view binder_driver hal dalvik camera input res app

	python systrace.py –b 32768 –t 5 -a com.rebeau.technology -o mynewtrace.html sched freq idle am wm gfx view binder_driver hal  dalvik camera input res app
	
	这里根据提示停止
	
	
	4.3以上
	
	$ cd android-sdk/platform-tools/systrace
	
	python systrace.py  -o mynewtrace.html sched freq idle am wm gfx view dalvik input res app

	python systrace.py –a com.kmxs.reader -o mynewtrace.html sched freq idle am wm gfx view dalvik input res app

	$ python systrace.py –b 32768 –t 5 –a com.kmxs.reader -o mynewtrace.html sched freq idle am wm gfx view dalvik input res app
	
	这里5s自动停止


### 2.在代码中插入自定义的类型

在release版本中，我们需要添加以下代码这样就可以手动开启App自定义Label的Trace功能，在非debuggable的版本中也适用
在Application的`attachBaseContext` 中添加

<pre>
Class<?> trace = Class.forName("android.os.Trace");
Method setAppTracingAllowed = trace.getDeclaredMethod("setAppTracingAllowed", boolean.class);
setAppTracingAllowed.invoke(null, true);

Trace.beginSection(String sectionName)
Trace.EndSection()

</pre>


### 3. Android 9 以上，

[官网](https://developer.android.com/studio/profile/systrace/on-device)

Android 9以上可以直接使用系统提供的功能来进行systrace文件的生成


## 分析systrace
Google Chrome浏览器可以打开systrace，如果打不开，可以通过chrome://tracing/，然后load systrace。


<table>
  <thead>
    <tr>
      <th>常用操作</th>
      <th>作用</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>w</td>
      <td><strong>放大</strong></td>
    </tr>
    <tr>
      <td>s</td>
      <td><strong>缩小</strong></td>
    </tr>
    <tr>
      <td>a</td>
      <td><strong>左移</strong></td>
    </tr>
    <tr>
      <td>d</td>
      <td><strong>右移</strong></td>
    </tr>
    <tr>
      <td>f</td>
      <td><strong>放大</strong>当前选定区域</td>
    </tr>
    <tr>
      <td>m</td>
      <td><strong>标记</strong>当前选定区域</td>
    </tr>
    <tr>
      <td>v</td>
      <td>高亮<strong>VSync</strong></td>
    </tr>
    <tr>
      <td>g</td>
      <td>切换是否显示<strong>60hz</strong>的网格线</td>
    </tr>
    <tr>
      <td>0</td>
      <td>恢复trace到<strong>初始态</strong>，这里是数字0而非字母o</td>
    </tr>

    <tr>
      <td>h</td>
      <td>切换是否显示详情</td>
    </tr>
    <tr>
      <td>/</td>
      <td>搜索关键字</td>
    </tr>
    <tr>
      <td>enter</td>
      <td>显示搜索结果，可通过← →定位搜索结果</td>
    </tr>
    <tr>
      <td>`</td>
      <td>显示/隐藏脚本控制台</td>
    </tr>
    <tr>
      <td>?</td>
      <td>显示帮助功能</td>
    </tr>
  </tbody>
</table>
	
一般来说，我们可以先通过查找，然后按M键，定位

[Inefficient View alpha usage](https://www.jianshu.com/p/3b9650307633)

[分析1](https://developer.android.com/studio/profile/systrace/navigate-report)

[分析2](https://source.android.com/devices/tech/debug/systrace)

#### CPU

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_kennel.png?raw=true)

[catapult](https://github.com/catapult-project/catapult/)





















