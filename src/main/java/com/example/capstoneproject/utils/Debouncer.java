package com.example.capstoneproject.utils;


import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Debouncer {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> lastExecution;

    public void debounce(HttpSession session, Runnable runnable, long delay) {
        if (Objects.nonNull(session.getAttribute("lastExecution"))) {
            if (lastExecution != null && !lastExecution.isDone()) {
                lastExecution.cancel(true);
                System.out.println("Cancel last debounce");
                lastExecution = scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
                session.setAttribute("lastExecution", lastExecution);
                System.out.println("Created new lastExecution");
            }
        } else {
            lastExecution = scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
            session.setAttribute("lastExecution", lastExecution);
            System.out.println("Created new lastExecution");
        }
    }

    public void cancelDebounce(HttpSession session) {
        if (Objects.nonNull(session.getAttribute("lastExecution"))) {
            if (lastExecution != null && !lastExecution.isDone()) {
                lastExecution.cancel(true);
                System.out.println("Cancel last debounce");
            }
            session.removeAttribute("lastExecution");
            session.removeAttribute("debouncer");
            System.out.println("Clear lastExecution");
        }
    }
}