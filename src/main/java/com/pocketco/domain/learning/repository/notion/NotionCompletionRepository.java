package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotionCompletionRepository extends JpaRepository<NotionCompletion, Long> {
    List<NotionCompletion> findByNotion_IdInAndUser_Id(List<Long> notionId, Long userId);
}