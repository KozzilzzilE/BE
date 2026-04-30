package com.pocketco.global.util.scheduler;

import com.pocketco.global.util.jwt.InMemoryTokenBlacklist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistCleanupScheduler {
    private final InMemoryTokenBlacklist inMemoryTokenBlacklist;

    // 5분 간격
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void submitPendingJobsByScheduled() {
//        log.info("Cleaning up token blacklist");
        inMemoryTokenBlacklist.removeExpiredTokens();
    }
}