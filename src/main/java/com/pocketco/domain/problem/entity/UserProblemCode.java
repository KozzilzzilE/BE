package com.pocketco.domain.problem.entity;

import com.pocketco.domain.baseEntity.BaseEntity; // 하은이가 알려준 경로로 수정! 👊✨
import com.pocketco.domain.user.entity.User;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자 정보

    private Long problemId; // 어떤 문제의 코드인지

    private String language; // 언어

    @Column(columnDefinition = "TEXT")
    private String sourceCode;

    public void updateSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}