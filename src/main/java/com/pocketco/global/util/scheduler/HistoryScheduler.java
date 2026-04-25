package com.pocketco.global.util.scheduler;

import com.pocketco.domain.user.application.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryScheduler {
    private final HistoryService historyService;

    // 10초
    @Scheduled(fixedDelay = 1200)
    public void syncHistory() {
        historyService.syncProcessingHistories();
    }
}