package com.pocketco.domain.user.entity;

import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
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
@Table(
        name = "user_problem_histories",
        indexes = {
                @Index(
                        name = "idx_history_user_problem_status",
                        columnList = "user_id, problem_id, status"
                )
        }
)
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status", nullable = false)
    @ColumnDefault("'NOT_REQUESTED'")
    private AICodeReviewStatus aiStatus = AICodeReviewStatus.NOT_REQUESTED;

    @Lob
    @Column(name = "ai_review", columnDefinition = "TEXT")
    private String aiReview;

    @Lob
    @Column(name = "ai_improvement", columnDefinition = "TEXT")
    private String aiImprovement;

    @Lob
    @Column(name = "ai_code", columnDefinition = "TEXT")
    private String aiCode;

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