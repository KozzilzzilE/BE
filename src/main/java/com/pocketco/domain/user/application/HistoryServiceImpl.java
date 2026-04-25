package com.pocketco.domain.user.application;

import com.pocketco.domain.judge0.application.Judge0Service;
import com.pocketco.domain.judge0.dto.Judge0ResultResponse;
import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.domain.user.entity.Judge0Token;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final Judge0TokenRepository judge0TokenRepository;
    private final Judge0Service judge0Service;

    @Override
    public void syncProcessingHistories() {
        List<History> historyList = historyRepository.findTop30ByStatusOrderByCreatedAtAsc(HistoryStatus.PROCESSING);

        for (History history : historyList) {
            try {
                List<Judge0Token> tokens = judge0TokenRepository.findByHistory_Id(history.getId());
                if (tokens.isEmpty()) {
                    log.info("스케줄링) DB에 저장된 토큰이 없는 기록입니다. history id {}", history.getId());
                    continue;
                }
                boolean hasNotSubmitted = tokens.stream()
                        .anyMatch(t -> t.getToken() == null
                                || t.getToken().isBlank() || t.getStatusId() == 0 || t.getStatusId() == 9);
                if (hasNotSubmitted) continue;

                List<String> tokenStrings = tokens.stream()
                        .map(Judge0Token::getToken)
                        .toList();
                Judge0ResultResponse response = judge0Service.getJudge0ResultStatus(tokenStrings);
                if (!response.allDone()) {
                    continue;
                }

                int updated = historyRepository.updateStatus(history.getId(), response.newStatus());
                if (updated == 1) {
                    log.info("스케줄링) History 상태 업데이트 완료. historyId={}", history.getId());
                }
            } catch (Exception e) {
                log.error("스케줄링) 갱신 실패, historyId = {}", history.getId(), e);
            }
        }
    }
}
