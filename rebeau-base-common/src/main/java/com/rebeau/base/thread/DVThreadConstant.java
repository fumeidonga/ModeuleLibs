package com.rebeau.base.thread;

/**
 * Created by Administrator on 2018/3/30.
 */

public class DVThreadConstant {

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final long KEEP_ALIVE = 10L;
    public static final String  THREAD_POOLNAME = "COMMON_MODULE";



}
