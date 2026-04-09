package com.pocketco.domain.csProblem.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cs_problems")
public class CSProblem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cs_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private boolean answer;

    @Lob
    @Column(nullable = false)
    private String explanation;
}