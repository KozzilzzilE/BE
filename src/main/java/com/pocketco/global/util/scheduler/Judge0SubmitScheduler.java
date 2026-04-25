package com.pocketco.global.util.scheduler;

import com.pocketco.domain.user.entity.Judge0Token;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import com.pocketco.global.util.judge0.Judge0SlotLimiter;
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
    private final Judge0TokenRepository judge0TokenRepository;
    private final Judge0SlotLimiter judge0SlotLimiter;
    private final Judge0SubmitWorker judge0SubmitWorker;

    // 1초
    @Scheduled(fixedDelay = 1000)
    public void submitPendingJobs() {
        List<Judge0Token> judge0Tokens = judge0TokenRepository.findTop2ByStatusIdOrderByCreatedAtAsc(0);

        for (Judge0Token judge0Token : judge0Tokens) {
            judge0SlotLimiter.submitSlot(() -> {
                judge0SubmitWorker.submitOne(judge0Token.getId());
                return null;
            });
        }
    }
}