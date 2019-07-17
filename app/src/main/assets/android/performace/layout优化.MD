
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
    
    Android系统每隔16ms发出VSYNC信号(1000ms/60=16.66ms)，触发对UI进行渲染， 如果每次渲染都成功，这样就能够达到流畅的画面所需要的60fps，
    为了能够实现60fps，这意味着计算渲染的大多数操作都必须在16ms内完成

[参考文章](https://www.jianshu.com/p/71cba1711de0)

### 1. 绘制原理

   我们知道手机是有许多像素点构成的通过显示每个像素点不同的颜色，然后组成各种图像;
   Activity的界面之所以可以被绘制到屏幕上其中有一个很重要的过程就是栅格化

   手机的画面渲染依赖于cpu & gpu

1. CPU 的作用

* 负责计算显示的内容，eg：视图创建，布局计算，图片解码，文本绘制等,将对象处理为多维图形,纹理

2. GPU 的作用
GPU会获取图形数据进行渲染，然后硬件负责把渲染后的内容呈现到屏幕上，他们两者不停的进行协作

* 负责格栅化操作(UI 元素绘制到屏幕上)

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/xuanran.png?raw=true)

### 2. 优化工具
1. Systrace 中的frames & Alerts ， 这里面有些提示信息，frames中绿色代表正常，其他颜色表示丢帧

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrace_frames.png?raw=true)


![](https://github.com/fumeidonga/markdownPic/blob/master/performance/systrae_alerts.png?raw=true)

2. Layout Inspector 布局检查器

![](https://github.com/fumeidonga/markdownPic/blob/master/performance/layoutinstract.png?raw=true)

3. Choreographer

   具体的可查看 performace/choreographer.md

   [kɔːriˈɑːɡrəfər]

    检测UI丢帧跟卡顿，网上的文章还是很多的, 我们查看 AppChoreoFrameCallback.java

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

[高频采集堆栈的方案 UiWatcher](https://github.com/guohaiyang1992/UiWatcher),查看AppUiWatcher

[Choreographer](https://developer.android.com/reference/android/view/Choreographer)

[那些年我们用过的显示性能指标](https://zhuanlan.zhihu.com/p/22239486)

[广研 Android 卡顿监控系统](https://mp.weixin.qq.com/s/MthGj4AwFPL2JrZ0x1i4fw)

[通过Choreographer检测UI丢帧和卡顿](https://blog.csdn.net/zhangphil/article/details/81129246)

[Choreographer 解析](https://www.jianshu.com/p/dd32ec35db1d)

[Android系统Choreographer机制实现过程](https://blog.csdn.net/yangwen123/article/details/39518923)

[从 FrameCallback 理解 Choreographer 原理及简单帧率监控应用](https://juejin.im/entry/58c83f3f8ac247072018d926)

[Choreographer GIT上的实现方式1](https://github.com/wasabeef/Takt)

[Choreographer GIT上的实现方式2](https://github.com/friendlyrobotnyc/TinyDancer)

### 3. 布局加载原理
0. setContentView()流程

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

> 问题

部分属性不支持 (可以修改X2C框架)

失去系统兼容性


#### 3） 优化布局及复杂度

1. view的绘制流程： measure 、layout、 draw
- measure: 为整个 View 树计算实际的大小，即设置实际的高（对应属性：mMeasureHeight）和宽（对应属性：mMeasureWidth），
  每个 View 的控件的实际宽高都是由父视图和本身视图所决定的
  
- layout：为将整个根据子视图的大小以及布局参数将 View 树放到合适的位置上

- draw：利用前两部得到的参数，将视图显示在屏幕上  

首先会创建一个DecorView, 一个ViewRootImpl -> performTraversals() -> performMeasure() -> measure()测量是从外到内开始，先是最外层等viewGroup，到里面的view，
内部使用了一个 for 循环对子视图进行遍历，分别调用了子视图的 measure() 方法，layout则是要设置坐标轴

<pre>
设置本 View 视图的最终大小。
如果该 View 对象是个 ViewGroup 类型，需要重写该 onMeasure() 方法，对其子视图进行遍历 measure() 过程。
measureChildren()，内部使用了一个 for 循环对子视图进行遍历，分别调用了子视图的 measure() 方法。
measureChild()，为指定的子视图 measure，会被 measureChildren 调用。
measureChildWidthMargins()，为指定的子视图考虑了 margin 和 padding 的 measure。
</pre>
去掉不必要的背景，去掉渐变框，去掉滚动条，在一定程度上是能加快绘制速度的

所以我们有了优化的方向，

1. 从内优化
 
	* 减少层级， 这样会加快 View 的循环遍历过程
	
	* 去除不必要的背景， 减少复杂shape使用，由于 在 draw 的步骤中，会单独绘制背景。因此去除不必要的背景会加快 View 的绘制
	
	* 尽可能少的使用 margin、padding。在测量和布局的过程中，对有 margin 和 padding 的 View 会进行单独的处理步骤，这样会花费时间。我们可以在父 View 中设置 margin 和 padding，从而避免在子 View 中每个单独设置和配置
	
	* 慎用渐变。能减少 draw 的流程
	
	* 去除不必要的 scrollbar。这样能减少 draw 的流程，慎用渐变， 也能减少 draw 的流程
	
	* 布局view group的选择

	* 避免层级叠加

2. 从外优化	

	* 布局嵌套过于复杂。这会直接 View 的层级变多
	
	* View 的过渡绘制
	
	* View 的频繁重新渲染
	
	* UI 线程中进行耗时操作。在 Android 4.0 之后，不允许在 UI 线程做网络操作
	
	* 冗余资源及错误逻辑导致加载和执行缓慢。简单的说，就是代码写的烂。
	
	* 频繁触发 GC，导致渲染受阻。当系统在短时间内有大量对象销毁，会造成内存抖动，频繁触发 GC 线程，而 GC 线程的优先级高于 UI 线程，因而会造成渲染受阻

	* 减少不必要的infalte、使用OpenGL
	
	* 用SurfaceView或TextureView代替普通View
	
	* 自定义view使用clipRect 屏蔽被遮盖的view绘制
	
	    使用clipRect指定绘制区域，告诉系统哪些需要绘制，哪些不需要
	
	    canvas.clipRect(...)


#### 4） < include> 、< viewstub>标签、< merge>标签

include标签常用于将布局中的公共部分提取出来供其他layout共用，通过提高布局的复用性，
以实现布局模块化，从而减少测量、绘制时间，但是在使用了include后可能导致布局嵌套过多，多余不必要的layout节点

ViewStub在visible / inflate后会被移除， 被它所包含的layout所替代， 这个在写代码的时候需要注意下

merge标签是作为include标签的一种辅助扩展来使用的，它的主要作用是为了防止在引用布局文件时产生多余的布局嵌套。
减少测量、绘制时间。Android解析和展示一个布局需要消耗时间，布局嵌套的越多，那么解析起来也就越耗时，性能也就越差

merge标签可用于两种典型情况：

	- 布局顶结点是FrameLayout且不需要设置background或padding等属性，可以用merge代替，因为Activity内容试图的parent view就是个FrameLayout，所以可以用merge消除只剩一个
	- 某布局作为子布局被其他布局include时，使用merge当作该布局的顶节点，这样在被引入时顶结点会自动被忽略，而将其子节点全部合并到主布局中