package com.pocketco.domain.learning.entity.applied;

import com.pocketco.domain.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "applied_blank_problems")
public class AppliedBlankProblem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blank_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String content;

    private Integer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_code_id", nullable = false)
    private AppliedCode exerciseCode;
}