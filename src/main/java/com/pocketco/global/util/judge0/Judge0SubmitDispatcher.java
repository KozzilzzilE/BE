package com.pocketco.global.util.judge0;

import com.pocketco.domain.user.entity.Judge0Token;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class Judge0SubmitDispatcher {
    private final Judge0TokenRepository judge0TokenRepository;
    private final Judge0SlotLimiter judge0SlotLimiter;
    private final Judge0SubmitWorker judge0SubmitWorker;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Async("judge0TriggerExecutor")
    public void triggerAsync() { trigger(); }

    public void trigger() {
        if (!running.compareAndSet(false, true)) {
            return;
        }

        try {
            List<Judge0Token> judge0Tokens = judge0TokenRepository.findTop5ByStatusIdOrderByCreatedAtAsc(0);

            for (Judge0Token judge0Token : judge0Tokens) {
                judge0SlotLimiter.submitSlot(() -> {
                    judge0SubmitWorker.submitOne(judge0Token.getId());
                    return null;
                });
            }
        } finally {
            running.set(false);
        }
    }
}