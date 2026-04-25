package com.pocketco.global.util.judge0;

import com.pocketco.domain.judge0.dto.Judge0IndividualRequest;
import com.pocketco.domain.judge0.dto.Judge0TokenResponse;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.problem.entity.TestCase;
import com.pocketco.domain.problem.entity.TimeLimit;
import com.pocketco.domain.problem.repository.TimeLimitRepository;
import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.Judge0Token;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Judge0SubmitWorker {
    private final Judge0TokenRepository judge0TokenRepository;
    private final TimeLimitRepository timeLimitRepository;
    private final Judge0Client judge0Client;

    @Transactional
    public void submitOne(Long judge0TokenId) {
        int claimed = judge0TokenRepository.updateStatusIfCurrent(judge0TokenId, 0, 15);

        if (claimed != 1) {
            return;
        }

        try {
            Judge0Token judge0Token = judge0TokenRepository.findById(judge0TokenId).orElseThrow();
            TestCase tc = judge0Token.getTestCase();
            History history = judge0Token.getHistory();

            Problem problem = history.getProblem();
            Language language = history.getLanguage();
            TimeLimit timeLimit = timeLimitRepository.findByProblem_IdAndLanguage_Id(problem.getId(), language.getId()).orElse(null);
            double timeLimitSec = 1.0;
            if (timeLimit != null) {
                timeLimitSec = timeLimit.getTimeLimitMs() / 1000.0;
            }

            Judge0IndividualRequest request = new Judge0IndividualRequest(
                    history.getSourceCode(),
                    language.getCode(),
                    timeLimitSec,
                    256000,
                    tc.getInput(),
                    tc.getOutput());

            Judge0TokenResponse response = judge0Client.submit(request);

            judge0TokenRepository.updateTokenAndStatus(judge0Token.getId(), response.token(), 1);
        } catch (Exception e) {
            judge0TokenRepository.updateStatus(judge0TokenId, 0);
            log.error("Judge0 제출 실패. jobId={}", judge0TokenId, e);
        }
    }
}