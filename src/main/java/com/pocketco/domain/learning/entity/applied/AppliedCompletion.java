package com.pocketco.domain.learning.entity.applied;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "applied_completions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "exercise_id"})
)
public class AppliedCompletion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applied_completion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private AppliedExercise exercise;
}