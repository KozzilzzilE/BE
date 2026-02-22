package com.pocketco.domain.learning.entity.notion;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "notions",
    uniqueConstraints = @UniqueConstraint(name = "uk_notion_topic_page", columnNames = {"topic_id", "page_no"})
)
public class Notion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notion_id")
    private Long id;

    @Column(name = "page_no", nullable = false)
    private Integer pageNo;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 150, nullable = false)
    private String point;

    @Lob
    private String detail;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}