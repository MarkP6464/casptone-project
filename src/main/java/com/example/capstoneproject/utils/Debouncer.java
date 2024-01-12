package com.example.capstoneproject.utils;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Debouncer {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> lastExecution;

    public void debounce(Runnable runnable, long delay) {
        if (lastExecution != null && !lastExecution.isDone()) {
            lastExecution.cancel(true);
        }

        lastExecution = scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}