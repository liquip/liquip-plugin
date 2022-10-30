package com.github.sqyyy.liquip.example.util;

import java.util.concurrent.*;

public class AsyncQueue implements AutoCloseable {
    private final ThreadPoolExecutor executorService;

    public AsyncQueue(int corePoolSize, int maxPoolSize) {
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1024);
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 3, TimeUnit.HOURS, queue);
    }

    public Future<?> submit(Task task) {
        return executorService.submit(task::run);
    }

    public void tryPrestart() {
        executorService.prestartCoreThread();
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
