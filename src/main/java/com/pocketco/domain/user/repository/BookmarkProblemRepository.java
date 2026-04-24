package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.BookmarkProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkProblemRepository extends JpaRepository<BookmarkProblem, Long> {
    int countByProblem_Id(Long problemId);
    boolean existsByUser_IdAndProblem_Id(Long userId, Long problemId);
}