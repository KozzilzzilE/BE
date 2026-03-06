package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotionCompletionRepository extends JpaRepository<NotionCompletion, Long> {
}