package com.pocketco.domain.learning.entity.notion;

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
        name = "notion_completions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "notion_id"})
)
public class NotionCompletion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notion_completion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notion_id", nullable = false)
    private Notion notion;
}