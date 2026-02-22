package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.Notion;
import org.springframework.data.repository.CrudRepository;

public interface NotionRepository extends CrudRepository<Notion, Long> {
    boolean existsByTopic_IdAndPageNo(Long topicId, Integer pageNo);
}