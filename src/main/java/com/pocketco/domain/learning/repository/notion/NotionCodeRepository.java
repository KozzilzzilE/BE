package com.pocketco.domain.learning.repository.notion;

import com.pocketco.domain.learning.entity.notion.NotionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotionCodeRepository extends JpaRepository<NotionCode, Long> {
    boolean existsByNotion_IdAndLanguage_Id(Long notionId, Long languageId);
    List<NotionCode> findByNotion_IdInAndLanguage_Id(List<Long> notionIds, Long languageId);
    List<NotionCode> findByNotion_IdIn(List<Long> notionIds);
}