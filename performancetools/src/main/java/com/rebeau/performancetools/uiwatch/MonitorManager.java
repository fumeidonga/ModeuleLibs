package com.rebeau.performancetools.uiwatch;

import android.content.Context;

import java.util.HashMap;

/**
 *
 */

public class MonitorManager {

    public static final String LOGCAT_MONITOR = "logcat";

    public static final String NORMAL_MONITOR = "normal";


    private static boolean hasHooked;

    private HashMap<String, AbsMonitor> monitorMap = new HashMap();

    private static MonitorManager Instance;

    public static MonitorManager getInstance() {
        if (Instance == null) {
            Instance = new MonitorManager();
        }
        return Instance;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    private MonitorManager() {

    }

    public static AbsMonitor getMonitor(String name) {
        return getInstance().createMonitor(name);
    }

    public static void startMonitors() {
        if (getInstance().context != null) {
            startMonitors(getInstance().context);
        }
    }

    public static void startMonitors(Context context) {

        getMonitor(LOGCAT_MONITOR).start(context);
        getMonitor(NORMAL_MONITOR).start(context);
    }

    private AbsMonitor createMonitor(String name) {
        AbsMonitor monitor;
        if (monitorMap.containsKey(name)) {
            monitor = monitorMap.get(name);
        } else if ((monitor = create(name)) != null) {
            monitorMap.put(name, monitor);
        }

        return monitor;
    }

    private AbsMonitor create(String name) {
        switch (name) {
            case LOGCAT_MONITOR:
                //return new LogcatMonitor();
            case NORMAL_MONITOR:
//                return new NormalMonitor();
            default:
                return null;
        }
    }

    public static void stopAllMonitors() {
        getMonitor(LOGCAT_MONITOR).stop();
        getMonitor(NORMAL_MONITOR).stop();
    }
}
