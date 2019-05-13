package com.rebeau.performancetools.uiwatch.dao;

import android.content.Context;
import android.text.format.Formatter;

import com.rebeau.performancetools.UIWatchLogCat;
import com.rebeau.performancetools.uiwatch.AppUiWatcher;

/**
 * author: david
 * date: 2019/5/5
 * description: ${Desc} .
 */
public class NormalMonitorBean {

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    GTRAnalysisResult gtrAnalysisResult = null;
    public static long startTestTime;

    private long lastTime = 0;

    private long lastCPUApp = 0;
    private long lastCPUTotal = 0;

    private long lastFlowUpload = 0;
    private long lastFlowDownload = 0;

    public NormalMonitorBean() {
        startTestTime = System.currentTimeMillis();
        gtrAnalysisResult = new GTRAnalysisResult();
    }

    public void onCollectNormalInfo(long time, long cpuTotal, long cpuApp,
                                    String cpuThreads, int memory, long flowUpload,
                                    long flowDownload, String gtrThreads) {
        // 去掉gtr线程带来的影响
        long cpuApp_noGTR = cpuApp;
        String[] gtrThs = gtrThreads.split(",");
        String[] cpuThs = cpuThreads.split(",");
        for (String s : cpuThs) {
            String[] sdsd = s.split(":");
            for (String temp : gtrThs) {
                if (sdsd[0].equals(temp)) {
                    cpuApp_noGTR = cpuApp_noGTR - Integer.parseInt(sdsd[1]);
                }
            }
        }

        if (lastTime != 0) {
            gtrAnalysisResult.nowCPU = (cpuTotal - lastCPUTotal) == 0 ? 0
                    : (cpuApp_noGTR - lastCPUApp) * 100L / (cpuTotal - lastCPUTotal);
            gtrAnalysisResult.nowMemory = memory / 1024;
            gtrAnalysisResult.nowFlow = gtrAnalysisResult.nowFlow + (flowUpload + flowDownload - lastFlowUpload - lastFlowDownload);
            gtrAnalysisResult.nowFlowSpeed = (flowUpload + flowDownload - lastFlowUpload - lastFlowDownload) * 1000 / 1024 / (time - lastTime);


            gtrAnalysisResult.allCPUChartDatas.add(new DetailPointData((time - startTestTime) / 1000, gtrAnalysisResult.nowCPU));
            gtrAnalysisResult.allMemoryChartDatas.add(new DetailPointData((time - startTestTime) / 1000, gtrAnalysisResult.nowMemory));
            gtrAnalysisResult.allFlowChartDatas.add(new DetailPointData((time - startTestTime) / 1000, gtrAnalysisResult.nowFlow / 1024));

            long thisCpuApp = cpuApp_noGTR - lastCPUApp;
            long thisCpuTotal = cpuTotal - lastCPUTotal;

            long frontTime = time - lastTime;
            long frontFlowUpload = flowUpload - lastFlowUpload;
            long frontFlowDownload = flowDownload - lastFlowDownload;
//            if (frontTime >= 0 //时间需要正向
//                    && thisCpuApp >= 0 && thisCpuTotal > 0 //cpu需要正向
//                    && frontFlowUpload >= 0 && frontFlowDownload >= 0) {//流量需要正向
//                gtrAnalysisResult.frontTime += frontTime;
//                gtrAnalysisResult.frontCpuApp += thisCpuApp;
//                gtrAnalysisResult.frontCpuTotal += thisCpuTotal;
//
//                if (gtrAnalysisResult.frontCpuMax < thisCpuApp * 100L / thisCpuTotal) {
//                    gtrAnalysisResult.frontCpuMax = thisCpuApp * 100L / thisCpuTotal;
//                }
//
//                if (gtrAnalysisResult.frontMemoryMax < (long) memory / 1024) {
//                    gtrAnalysisResult.frontMemoryMax = (long) memory / 1024;
//                }
//
//                gtrAnalysisResult.frontMemoryAverage_Sum += (long) memory / 1024;
//                gtrAnalysisResult.frontMemoryAverage_Num++;
//                gtrAnalysisResult.frontFlowUpload += frontFlowUpload;
//                gtrAnalysisResult.frontFlowDownload += frontFlowDownload;
//            }
        }

        lastTime = time;

        lastCPUApp = cpuApp_noGTR;
        lastCPUTotal = cpuTotal;

        lastFlowUpload = flowUpload;
        lastFlowDownload = flowDownload;
        UIWatchLogCat.dt("normalwatch", "\n CPU : "+ gtrAnalysisResult.nowCPU + "%, nowMemory : "+ gtrAnalysisResult.nowMemory + "MB / " + AppUiWatcher.getInstance().getHeapstartsize() + ", net : "+ Formatter.formatFileSize(context, gtrAnalysisResult.nowFlow) + "");
    }
}
