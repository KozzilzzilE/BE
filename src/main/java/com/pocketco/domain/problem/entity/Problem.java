package com.pocketco.domain.problem.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "problems")
public class Problem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "difficulty_order", nullable = false)
    private Integer difficultyOrder;

    @Lob // 아주 긴 텍스트를 담을 수 있게 해줍니다.
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Lob
    @Column(name = "constraints", columnDefinition = "TEXT", nullable = false)
    private String constraints;

    @Lob
    @Column(name = "line_solution", columnDefinition = "TEXT",nullable = false)
    private String lineSolution;

    @Lob
    @Column(name = "solution_text", columnDefinition = "TEXT",nullable = false)
    private String solutionText;
}