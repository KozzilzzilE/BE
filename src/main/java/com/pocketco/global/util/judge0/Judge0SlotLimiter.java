package com.pocketco.global.util.judge0;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

@Component
public class Judge0SlotLimiter {
    private final Semaphore runSemaphore = new Semaphore(2, true);          // run 용 2개
    private final Semaphore submitSemaphore = new Semaphore(2, true);       // submit 용 2개

    public <T> T runSlot(Supplier<T> task) {
        return execute(runSemaphore, task);
    }

    public <T> T submitSlot(Supplier<T> task) {
        return execute(submitSemaphore, task);
    }

    public <T> T execute(Semaphore semaphore, Supplier<T> task) {
        boolean acquired = false;
        try {
            semaphore.acquire();
            acquired = true;
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Judge0 요청이 중단되었습니다", e);
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }
}