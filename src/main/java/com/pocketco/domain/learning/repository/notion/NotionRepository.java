package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.Notion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotionRepository extends JpaRepository<Notion, Long> {
    boolean existsByTopic_IdAndPageNo(Long topicId, Integer pageNo);
    List<Notion> findByTopic_IdOrderByPageNoAsc(Long topicId);
}