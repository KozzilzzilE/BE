package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.problem.dto.ProblemAllResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    boolean existsByTitle(String title);

    List<Problem> findAllByTopicIdOrderByDifficultyOrderAscIdAsc(Long topicId);

    @Query("""
        select new com.pocketco.domain.problem.dto.ProblemAllResponseDTO$ProblemItemDTO(
            p.id, 
            p.title, 
            p.difficulty, 
            (case when p.difficulty = 'EASY' then '쉬움' 
                  when p.difficulty = 'NORMAL' then '보통' 
                  when p.difficulty = 'HARD' then '어려움' 
                  else '미정' end),
            (select count(b) from BookmarkProblem b where b.problem = p),
            (select count(b) > 0 from BookmarkProblem b where b.problem = p and b.user.id = :userId),
            (select count(h) > 0 from History h where h.problem = p and h.user.id = :userId and h.status = 'ACCEPTED'),
            t.name, 
            t.displayName
        )
        from Problem p
        left join p.topic t
        """)
    Page<ProblemAllResponseDTO.ProblemItemDTO> findAllProblemsWithUserStatus(@Param("userId") Long userId, Pageable pageable);
}