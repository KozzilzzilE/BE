package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.domain.user.repository.projectionInterface.MainScreenCalendarProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    int countThisMonthSolved(
            @Param("userId") Long userId,
            @Param("status") HistoryStatus status,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select h 
    from History h 
    where h.id = :historyId
    """)
    Optional<History> findByIdForUpdate(@Param("historyId") Long historyId);

    @Query("""
        select count(distinct h.problem.id)
        from History h
        where h.user.id = :userId
          and h.status = 'ACCEPTED'
    """)
    long countSolvedProblemUnique(@Param("userId") Long userId);

    List<History> findTop10ByUser_IdOrderByCreatedAtDesc(Long userId);
}