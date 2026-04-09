package com.pocketco.domain.problem.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.language.entity.Language;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "problem_time_limits")
public class TimeLimit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_limit_id")
    private Long id;

    @Column(name = "time_limit_ms", nullable = false)
    @ColumnDefault("1000")
    private Integer timeLimitMs = 1000;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}