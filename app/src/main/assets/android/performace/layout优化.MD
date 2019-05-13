
### 相关概念
1. 格栅化

    将图片等矢量资源,转化为一格格像素点的像素图,显示到屏幕上.
    
    所谓的栅格化就是绘制那些Button，Shape，Path，String，Bitmap等组件最基础的操作.
    
    它把那些组件拆分到不同的像素上进行显示，说的俗气一点，就是解决那些复杂的XML布局文件和标记语言，使之转化成用户能看懂的图像，
    
    但是这不是直接转换的，XML布局文件需要在CPU中首先转换为多边形或者纹理，
    然后再传递给GPU进行格栅化，对于栅格化，跟OpenGL有关，格栅化是一个特别费时的操作。

    ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/geshanhua.png?raw=true)

2. 60FPS  &  16ms

    是指画面每秒传输帧数, 安卓中固定是60fps
    
    每秒60fps，那么每一帧的间隔时间就是 1000ms / 60 = 16.66ms.
    
    Android系统每隔16ms发出VSYNC信号(1000ms/60=16.66ms)，触发对UI进行渲染， 如果每次渲染都成功，这样就能够达到流畅的画面所需要的60fps，为了能够实现60fps，这意味着计算渲染的大多数操作都必须在16ms内完成

[参考文章](https://www.jianshu.com/p/71cba1711de0)

### 1. 绘制原理

   手机的画面渲染依赖于cpu & gpu

1. CPU 的作用

* 负责计算显示的内容，eg：视图创建，布局计算，图片解码，文本绘制等,将对象处理为多维图形,纹理

2. GPU 的作用

* 负责格栅化操作(UI 元素绘制到屏幕上)

    ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/xuanran.png?raw=true)

### 2. 优化工具
1. Systrace 中的frames & Alerts ， 这里面有些提示信息，frames中绿色代表正常，其他颜色表示丢帧

    ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_frames.png?raw=true)


    ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrae_alerts.png?raw=true)

2. Layout Inspector 布局检查器

    ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/layoutinstract.png?raw=true)

3. Choreographer

   [kɔːriˈɑːɡrəfər]

    检测UI丢帧跟卡顿，网上的文章还是很多的, 我们查看 BlockDetectByChoreographer

	<pre>
	public class FPSFrameCallback implements Choreographer.FrameCallback {
	    ....
	    @Override
	    public void doFrame(long frameTimeNanos) {
	        
	        //注册下一帧回调
	        Choreographer.getInstance().postFrameCallback(this);
	    }
	}
	
	Choreographer.getInstance().postFrameCallback(new FPSFrameCallback(System.nanoTime()));
	
	</pre>

[高频采集堆栈的方案 UiWatcher](https://github.com/guohaiyang1992/UiWatcher)， 查看AppUiWatcher

[Choreographer](https://developer.android.com/reference/android/view/Choreographer)

[那些年我们用过的显示性能指标](https://zhuanlan.zhihu.com/p/22239486)

[广研 Android 卡顿监控系统](https://mp.weixin.qq.com/s/MthGj4AwFPL2JrZ0x1i4fw)

[通过Choreographer检测UI丢帧和卡顿](https://blog.csdn.net/zhangphil/article/details/81129246)

[Choreographer 解析](https://www.jianshu.com/p/dd32ec35db1d)

[Android系统Choreographer机制实现过程](https://blog.csdn.net/yangwen123/article/details/39518923)

[从 FrameCallback 理解 Choreographer 原理及简单帧率监控应用](https://juejin.im/entry/58c83f3f8ac247072018d926)


### 3. 布局加载原理

1. LayoutInflater 解析xml文件，然后从解析出来的对象里面进行view的创建，
这里就涉及到IO的操作，这是一个性能点

>  final View temp = createViewFromTag(root, name, inflaterContext, attrs);

   ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/createviw1.png?raw=true)
    代码如下：
   ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/createveiw.png?raw=true)

2. 当factory2 factory都为空，进入createView，里面用到了反射，这是一个性能点

针对这两点， 我们可以异步Inflate：

 LayoutInflater创建view的 Hook，就是利用Factory来进行拦截，再做相应的处理

 定制创建view的过程: 全局替换自定义TextView等控件。


### 4. 获取界面布局耗时

获取每个界面的加载耗时

#### 1）常规方式
复写 setContentView(), 


#### 2）AOP / ArtHook 方式
   切Activity的 setContentView() 方法，

    @Around("execution(* android.app.Activity.setContentView(..))")

#### 3）获取任意控件加载耗时
   使用LayoutInflate Factory， 这个过程一般来说是不慢的


### 5. 优化方法
#### 1） 异步Inflate AsyncLayoutInflater
首先添加support，"com.android.support：asynclayoutinflater:$versions.support"

android.support.v4.view.AsyncLayoutInflater
> 作用

异步inflate主要解决xml读取以及view从创建过程慢，用来解析繁重复杂的布局文件

AsyncLayoutInflater使用workThread加载布局文件，加载完后回调主线程，直接返回View

> 限制条件
<pre>
1. 使用异步 inflate，那么需要这个 layout 的 parent 的 generateLayoutParams 函数是线程安全的；
2. 所有构建的 View 中必须不能创建 Handler 或者是调用 Looper.myLooper；
（因为是在异步线程中加载的，异步线程默认没有调用 Looper.prepare ）；
3. 异步转换出来的 View 并没有被加到 parent view中，AsyncLayoutInflater 是调用了 LayoutInflater.inflate(int, ViewGroup, false)，因此如果需要加到 parent view 中，就需要我们自己手动添加；
4. AsyncLayoutInflater 不支持设置 LayoutInflater.Factory 或者 LayoutInflater.Factory2；
5. 不支持加载包含 Fragment 的 layout；
6. 如果 AsyncLayoutInflater 失败，那么会自动回退到UI线程来加载布局；</pre>

   这种方式只是缓解IO,反射等问题，并没有从根本上解决

[AsyncLayoutInflater 限制及改进](https://www.jianshu.com/p/f0c0eda06ae4)


#### 2） X2C框架
从根本上解决IO,反射问题

> 原理

APT编译期间，将XML翻译成java代码

 [X2C 使用传送门1](https://www.jianshu.com/p/c1b9ce20ceb3)

 [X2C 使用传送门2](https://github.com/iReaderAndroid/X2C/blob/master/README_CN.md)

导入依赖

    annotationProcessor 'com.zhangyue.we:x2c-apt:1.1.2'
    implementation 'com.zhangyue.we:x2c-lib:1.0.6'

> 问题

部分属性不支持 (可以修改X2C框架)

失去系统兼容性


#### 3） 优化布局层级及复杂度

view的绘制流程： measure 、layout、 draw

* 减少层级

* 布局view group的选择

* merge

* viewstub

* onDraw 中避免耗时



#### 4） 避免过度绘制

* 去掉多余背景色，减少复杂shape使用

* 避免层级叠加

* 自定义view使用clipRect 屏蔽被遮盖的view绘制

    使用clipRect指定绘制区域，告诉系统哪些需要绘制，哪些不需要

    canvas.clipRect(...)