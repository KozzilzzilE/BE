package com.pocketco.domain.user.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_problem_histories")
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Lob
    @Column(name = "source_code", columnDefinition = "TEXT", nullable = false)
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryStatus status;

    @Column(name = "is_solved", nullable = false)
    @ColumnDefault("false")
    private boolean isSolved;

    @Column(name = "is_solution_viewed", nullable = false)
    @ColumnDefault("false")
    private boolean isSolutionViewed;

    @Column(name = "is_goal_met", nullable = false)
    @ColumnDefault("false")
    private boolean isGoalMet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
}