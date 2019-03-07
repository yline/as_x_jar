package net.sourceforge.simcpux.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService sFixedThreadPool;
    private static ExecutorService sSingleThreadPool;

    /**
     * 固定大小线程池，暂定5个
     */
    public static void fixedThreadExecutor(Runnable runnable) {
        if (sFixedThreadPool == null) {
            sFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        sFixedThreadPool.execute(runnable);
    }

    /**
     * 单线程池
     */
    public static void singleThreadExecutor(Runnable runnable) {
        if (sSingleThreadPool == null) {
            sSingleThreadPool = Executors.newSingleThreadExecutor();
        }
        sSingleThreadPool.execute(runnable);
    }
}
