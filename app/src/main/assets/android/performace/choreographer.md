



Choreographer

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

