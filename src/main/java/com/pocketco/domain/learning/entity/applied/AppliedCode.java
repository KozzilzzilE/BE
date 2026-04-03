package com.pocketco.domain.learning.entity.applied;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.language.entity.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "applied_codes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"exercise_id", "language_id"})
)
public class AppliedCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applied_code_id")
    private Long id;

    @Lob
    @Column(name = "code_template", nullable = false)
    private String codeTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private AppliedExercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
}
