package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.HistoryStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findTop100ByStatusOrderByCreatedAtDesc(HistoryStatus status);
    List<History> findByUser_IdAndProblem_IdOrderByCreatedAtDesc(Long userId, Long problem);
    boolean existsByUser_IdAndProblem_IdAndStatus(Long userId, Long problem, HistoryStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update History h
        set h.status = :newStatus
        where h.id = :historyId
        and h.status = 'PROCESSING'
    """)
    int updateStatus(@Param("historyId") Long historyId, @Param("newStatus") HistoryStatus newStatus);
}