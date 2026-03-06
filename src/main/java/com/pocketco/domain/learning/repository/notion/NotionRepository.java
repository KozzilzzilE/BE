package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.Notion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotionRepository extends JpaRepository<Notion, Long> {
    boolean existsByTopic_IdAndPageNo(Long topicId, Integer pageNo);
}