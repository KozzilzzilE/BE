package com.pocketco.domain.csProblem.application;

import com.pocketco.domain.admin.dto.AddCSProblemRequest;
import com.pocketco.domain.csProblem.dto.CSProblemRandomResponseDTO;
import com.pocketco.domain.csProblem.entity.CSProblem;
import com.pocketco.domain.csProblem.repository.CSProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CSProblemServiceImpl implements CSProblemService {
    private final CSProblemRepository csProblemRepository;

    @Override
    public Long addCSProblem(AddCSProblemRequest request) {
        CSProblem csProblem = CSProblem.builder()
                .question(request.question())
                .answer(request.answer())
                .explanation(request.explanation())
                .build();
        CSProblem saved = csProblemRepository.save(csProblem);
        return saved.getId();
    }

    @Override
    public List<CSProblemRandomResponseDTO> getRandomCSProblems(int count) {
        long totalCount = csProblemRepository.count();
        if (totalCount <= 0) {  // 없으면 빈 리스트 반환
            return List.of();
        }

        int tryCount = (int) Math.min(count, totalCount);

        // findRandomCSProblems() => 쿼리문으로 랜덤 조회, CSProblem 데이터가 많아지면(트래픽 증가 시) random_value 필드 추가하여 랜덤 조회하는 방식으로 확장
        List<CSProblemRandomResponseDTO> csProblemRandomList =
                csProblemRepository.findRandomCSProblems(tryCount).stream()
                        .map(p -> CSProblemRandomResponseDTO.builder()
                                .csProblemId(p.getId())
                                .question(p.getQuestion())
                                .answer(p.getAnswer())
                                .explanation(p.getExplanation())
                                .build())
                        .toList();

        return csProblemRandomList;
    }
}