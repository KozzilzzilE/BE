package com.pocketco.domain.aiCodeReview.application;

import com.pocketco.domain.aiCodeReview.dto.AICodeReviewGetDTO;
import com.pocketco.domain.aiCodeReview.dto.AICodeReviewPostDTO;
import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
import com.pocketco.domain.aiCodeReview.event.AICodeReviewRequestedEvent;
import com.pocketco.domain.aiCodeReview.exception.HistoryAccessDeniedException;
import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.exception.HistoryNotFoundException;
import com.pocketco.domain.user.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AICodeReviewServiceImpl implements AICodeReviewService {
    private final HistoryRepository historyRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AICodeReviewPostDTO createAICodeReview(Long userId, Long historyId) {
        History history = historyRepository.findByIdForUpdate(historyId).orElseThrow(HistoryNotFoundException::new);
        if (!history.getUser().getId().equals(userId)) {
            throw new HistoryAccessDeniedException();
        }
        AICodeReviewStatus currentStatus = history.getAiStatus();

        // 의도하지 않게 더블 클릭 돼었을 때(이미 진행중인 경우)
        if (currentStatus == AICodeReviewStatus.PROCESSING || currentStatus == AICodeReviewStatus.ACCEPTED) {
            return AICodeReviewPostDTO.builder()
                    .historyId(history.getId())
                    .aiStatus(currentStatus)
                    .build();
        }
        history.setAiStatus(AICodeReviewStatus.PROCESSING);

        // ai 비동기 요청
        eventPublisher.publishEvent(new AICodeReviewRequestedEvent(history.getId()));

        return AICodeReviewPostDTO.builder()
                .historyId(history.getId())
                .aiStatus(history.getAiStatus())
                .build();
    }

    @Override
    public AICodeReviewGetDTO getAICodeReview(Long userId, Long historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(HistoryNotFoundException::new);
        if (!history.getUser().getId().equals(userId)) {
            throw new HistoryAccessDeniedException();
        }
        return AICodeReviewGetDTO.builder()
                .historyId(history.getId())
                .aiStatus(history.getAiStatus())
                .aiReview(history.getAiReview())
                .aiImprovement(history.getAiImprovement())
                .aiCode(history.getAiCode())
                .build();
    }
}