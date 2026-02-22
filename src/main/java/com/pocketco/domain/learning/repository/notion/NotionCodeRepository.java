package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCode;
import org.springframework.data.repository.CrudRepository;

public interface NotionCodeRepository extends CrudRepository<NotionCode, Long> {
    boolean existsByNotion_IdAndLanguage_Id(Long notionId, Long languageId);
}