package com.lmartino.bank.domain;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MultithreadTestSupport {
    // concurrent threads
    public static final int DEFAULT_THREAD_COUNT = 5;

    private final ExecutorService executor;
    private final int threadCount;
    private final int iterationCount;


    public MultithreadTestSupport(int iterationCount) {
        this(DEFAULT_THREAD_COUNT, iterationCount);
    }

    public MultithreadTestSupport(int threadCount, int iterationCount) {
        // threadCount == concurrent threads
        // iterationCount == number of iterations
        // total threads = threadCount * iterationCount
        this.threadCount = threadCount;
        this.iterationCount = iterationCount;
        this.executor = Executors.newCachedThreadPool();
    }

    public int totalActionCount() {
        return threadCount * iterationCount;
    }

    public void init(final Runnable action) throws InterruptedException {
        spawnThreads(action).await();
    }

    public void blitz(long timeoutMs, final Runnable action) throws InterruptedException, TimeoutException {
        if (!spawnThreads(action).await(timeoutMs, MILLISECONDS)) {
            throw new TimeoutException("timed out waiting for blitzed actions to complete successfully");
        }
    }

    private CountDownLatch spawnThreads(final Runnable action) {
        final CountDownLatch finished = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        repeat(action);
                    }
                    finally {
                        finished.countDown();
                    }
                }
            });
        }
        return finished;
    }

    private void repeat(Runnable action) {
        for (int i = 0; i < iterationCount; i++) {
            action.run();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

}
