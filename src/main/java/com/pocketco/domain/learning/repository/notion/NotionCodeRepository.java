package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotionCodeRepository extends JpaRepository<NotionCode, Long> {
    boolean existsByNotion_IdAndLanguage_Id(Long notionId, Long languageId);
}