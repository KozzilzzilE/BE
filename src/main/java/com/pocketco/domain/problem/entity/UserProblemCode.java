package com.pocketco.domain.problem.entity;

import com.pocketco.domain.baseEntity.BaseEntity; // 하은이가 알려준 경로로 수정! 👊✨
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.language.entity.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProblemCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "code", columnDefinition = "TEXT")
    private String sourceCode;

    public void updateSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}