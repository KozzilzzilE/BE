package com.pocketco.domain.bookmark.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.problem.entity.Problem;
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
        name = "user_bookmark_problems",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "problem_id"})
)
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}