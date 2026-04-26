package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.domain.user.repository.projectionInterface.MainScreenCalendarProjection;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findTop30ByStatusOrderByCreatedAtAsc(HistoryStatus status);
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

    @Query(value = """
        select
            date(CONVERT_TZ(h.created_at, '+00:00', '+09:00')) as date,
            count(*) as count
        from user_problem_histories h
        where h.user_id = :userId
          and h.is_solved = false
          and h.status = 'ACCEPTED'
        group by date(CONVERT_TZ(h.created_at, '+00:00', '+09:00'))
        order by date
    """, nativeQuery = true)
    List<MainScreenCalendarProjection> countSolvedByDate(@Param("userId") Long userId);

    @Query("""
        select count(h)
        from History h
        where h.user.id = :userId
          and h.isSolved = false
          and h.status = :status
          and h.createdAt >= :startUtc
          and h.createdAt < :endUtc
    """)
    long countThisMonthSolved(
            @Param("userId") Long userId,
            @Param("status") HistoryStatus status,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc
    );
}