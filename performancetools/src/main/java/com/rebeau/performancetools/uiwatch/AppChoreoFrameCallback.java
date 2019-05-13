package com.rebeau.performancetools.uiwatch;

import android.annotation.SuppressLint;
import android.view.Choreographer;

import java.util.concurrent.TimeUnit;

/**
 * 用于监听帧率的回调
 *
 * @version v1.0
 * @date 2018/7/1
 */
@SuppressLint("NewApi")
public class AppChoreoFrameCallback implements Choreographer.FrameCallback {

    /**
     * 上一帧的时间 单位 纳秒
     */
    long lastFrameTimeNanos = 0L;
    /**
     * 当前一帧的时间 单位 纳秒
     */
    long currentFrameTimeNanos = 0L;

    /**
     * 最小允许的跳过帧率的数量
     */
    private int minSkipFrameCount = 3;

    private static final float ONE_FRAME_TIME = 16.6f; // 1 Frame time cost
    private static float MIN_FRAME_TIME = ONE_FRAME_TIME * 3; // 3 Frame time cost
    private static final float MAX_FRAME_TIME = 60 * ONE_FRAME_TIME;

    /**
     * 是否退出,默认false
     */
    private boolean isExit = false;

    /**
     * 构造方法
     */
    public AppChoreoFrameCallback(int minSkipFrameCount) {
        this.minSkipFrameCount = minSkipFrameCount;
        if(minSkipFrameCount < 1) {
            minSkipFrameCount = 3;
        }
        MIN_FRAME_TIME = ONE_FRAME_TIME * minSkipFrameCount; // 3 Frame time cost
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        //首次初始化last时间,并开启监控
        if (lastFrameTimeNanos == 0) {
            lastFrameTimeNanos = frameTimeNanos;
            //LogMonitor.getInstance().startMonitor();
        }

        //初始化当前时间,计算帧率时间差,计算跳过的帧率,超出限制输出log,并重置
        currentFrameTimeNanos = frameTimeNanos;

        //计算两帧的时间间隔
        //纳秒 转换成 毫秒 是多少
        long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos - lastFrameTimeNanos, TimeUnit.NANOSECONDS);

        //Android系统每隔16ms发出VSYNC信号(1000ms/60=16.66ms)
        //丢帧的数量为间隔时间除以16，如果超过3，就开始有卡顿的感知

        //如果时间间隔大于最小时间间隔，3*16ms，小于最大的时间间隔，60*16ms，就认为是掉帧
        //此处解释一下原因： 因为正常情况下，两帧的间隔都是在16ms以内 ,、
        // 如果我们统计到的两帧间隔时间大于三倍的普通绘制时间，我们就认为是出现了卡顿，
        // 之所以设置最大时间间隔，是为了有时候页面不刷新绘制的时候，不做统计处理
        if (diffMs > MIN_FRAME_TIME && diffMs < MAX_FRAME_TIME) {
            int droppedCount = (int) (diffMs / ONE_FRAME_TIME);
            //DVLogUtils.dt("UI线程超时(超过16ms): " + diffMs + " ms" + " , 丢帧数: " + droppedCount);
            LogMonitor.getInstance().startOutputAndResetCollectionMonitor();
        }
        /*

        int skipFrameCount = (int) (diffMs / ONE_FRAME_TIME);
        if (skipFrameCount > minSkipFrameCount) {
            LogMonitor.getInstance().startOutputAndResetCollectionMonitor();
        }

        if (diffMs > ONE_FRAME_TIME) {
            int droppedCount = (int) (diffMs / ONE_FRAME_TIME);
            DVLogUtils.dt("UI线程超时(超过16ms): " + diffMs + " ms" + " , 丢帧数: " + droppedCount);
            ThreadLogMonitor.getInstance().startOutputAndResetCollectionMonitor();
        }*/

        // 对线程卡顿的监控
        if (ThreadLogMonitor.getInstance().isMonitor()) {
            ThreadLogMonitor.getInstance().removeMonitor();
        }
        ThreadLogMonitor.getInstance().startMonitor();

        //将当前的时间设置为last时间,用于下次计算,并重新注册
        lastFrameTimeNanos = frameTimeNanos;
        //没退出的时候通知下次,否则不通知
        if (!isExit) {
            Choreographer.getInstance().postFrameCallback(this);
        }

    }

    /**
     * 设置是否退出
     *
     * @param exit true:退出 false:不退出
     */
    public void setExit(boolean exit) {
        isExit = exit;
    }
}
