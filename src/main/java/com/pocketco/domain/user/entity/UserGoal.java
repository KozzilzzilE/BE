package com.pocketco.domain.user.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_goal_stats")
public class UserGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer dailyTarget;      // 하루 목표 문제 수
    private Integer weeklyTarget;     // 주간 목표
    private Integer monthlyTarget;    // 월간 목표

    private Integer currentStreak;    // 연속 공부 일수
    private Integer totalSolved;      // 총 푼 문제 수
}