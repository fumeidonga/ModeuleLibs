package com.rebeau.performancetools.uiwatch.tese;

import android.view.Choreographer;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.performancetools.uiwatch.ThreadLogMonitor;

import java.util.concurrent.TimeUnit;

/**
 * author: hrl
 * date: 2019/4/22
 * description: Choreographer周期性的在UI重绘时候触发，在代码中记录上一次和下一次绘制的时间间隔，
 * 如果超过16ms，就意味着一次UI线程重绘的“丢帧”。
 * 丢帧的数量为间隔时间除以16，
 * 如果超过3，就开始有卡顿的感知 .
 */
public class BlockDetectByChoreographer {

    private static final float ONE_FRAME_TIME = 16.6f; // 1 Frame time cost
    private static final float MIN_FRAME_TIME = ONE_FRAME_TIME * 3; // 3 Frame time cost
    private static final float MAX_FRAME_TIME = 60 * ONE_FRAME_TIME;

    public static void start() {
        Choreographer.getInstance()
                .postFrameCallback(new Choreographer.FrameCallback() {

                    /**
                     * 上一帧的时间 单位 纳秒
                     */
                    long lastFrameTimeNanos = 0L;
                    /**
                     * 当前一帧的时间 单位 纳秒
                     */
                    long currentFrameTimeNanos = 0L;

                    @Override
                    public void doFrame(long frameTimeNanos) {
                        //DVLogUtils.dt(frameTimeNanos);
                        if (lastFrameTimeNanos == 0) {
                            lastFrameTimeNanos = frameTimeNanos;
                        }

                        currentFrameTimeNanos = frameTimeNanos;
                        //计算两帧的时间间隔
                        //纳秒 转换成 毫秒 是多少
                        long times = (currentFrameTimeNanos - lastFrameTimeNanos) / 1000000;
                        long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos - lastFrameTimeNanos, TimeUnit.NANOSECONDS);


                        //Android系统每隔16ms发出VSYNC信号(1000ms/60=16.66ms)
                        //丢帧的数量为间隔时间除以16，如果超过3，就开始有卡顿的感知

                        //如果时间间隔大于最小时间间隔，3*16ms，小于最大的时间间隔，60*16ms，就认为是掉帧
                        //此处解释一下原因： 因为正常情况下，两帧的间隔都是在16ms以内 ,、
                        // 如果我们统计到的两帧间隔时间大于三倍的普通绘制时间，我们就认为是出现了卡顿，
                        // 之所以设置最大时间间隔，是为了有时候页面不刷新绘制的时候，不做统计处理
                        if (diffMs > MIN_FRAME_TIME && diffMs < MAX_FRAME_TIME) {
                            int droppedCount = (int) (diffMs / 16.6);
                            RBLogUtil.dt("UI线程超时(超过16ms): " + diffMs + " ms" + " , 丢帧数: " + droppedCount);
                        }
                        /*if (diffMs > 16.6f) {
                            int droppedCount = (int) (diffMs / 16.6);
                            DVLogUtils.dt("UI线程超时(超过16ms): " + diffMs + " ms" + " , 丢帧数: " + droppedCount);
                        }*/

                        // 对线程卡顿的监控
                        if (ThreadLogMonitor.getInstance().isMonitor()) {
                            ThreadLogMonitor.getInstance().removeMonitor();
                        }
                        ThreadLogMonitor.getInstance().startMonitor();
                        Choreographer.getInstance().postFrameCallback(this);
                        lastFrameTimeNanos = frameTimeNanos;




                        /*if (LogMonitor.getInstance().isMonitor()) {
                            LogMonitor.getInstance().removeMonitor();
                        }
                        LogMonitor.getInstance().startMonitor();
                        Choreographer.getInstance() .postFrameCallback(this);*/
                    }
                });
    }
}
