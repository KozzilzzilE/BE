package com.pocketco.global.util.scheduler;

import com.pocketco.domain.user.entity.Judge0Token;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import com.pocketco.global.util.judge0.Judge0SlotLimiter;
import com.pocketco.global.util.judge0.Judge0SubmitDispatcher;
import com.pocketco.global.util.judge0.Judge0SubmitWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Judge0SubmitScheduler {
    private final Judge0SubmitDispatcher judge0SubmitDispatcher;

    // 1초
    @Scheduled(fixedDelay = 1000)
    public void submitPendingJobsByScheduled() {
        judge0SubmitDispatcher.trigger();
    }
}