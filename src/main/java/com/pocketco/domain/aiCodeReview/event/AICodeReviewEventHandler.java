package com.pocketco.domain.aiCodeReview.event;

import com.pocketco.domain.aiCodeReview.application.AICodeReviewWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AICodeReviewEventHandler {

    private final AICodeReviewWorker aiCodeReviewWorker;

    @Async("aiExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(AICodeReviewRequestedEvent event) {
        aiCodeReviewWorker.process(event.historyId());
    }
}