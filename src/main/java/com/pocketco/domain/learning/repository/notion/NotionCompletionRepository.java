package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCompletion;
import org.springframework.data.repository.CrudRepository;

public interface NotionCompletionRepository extends CrudRepository<NotionCompletion, Long> {
}