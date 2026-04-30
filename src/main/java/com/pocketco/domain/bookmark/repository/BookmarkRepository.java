package com.pocketco.domain.bookmark.repository;

import com.pocketco.domain.bookmark.entity.Bookmark;
import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndProblem(User user, Problem problem);
    boolean existsByUserAndProblem(User user, Problem problem);
    List<Bookmark> findByUserOrderByCreatedAtDesc(User user);

    // 특정 문제의 총 찜 개수 조회
    Long countByProblem(Problem problem);
}