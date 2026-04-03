package com.pocketco.domain.learning.entity.applied;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "applied_exercises",
        uniqueConstraints = @UniqueConstraint(name = "uk_applied_topic_order", columnNames = {"topic_id", "order_no"})
)
public class AppliedExercise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}