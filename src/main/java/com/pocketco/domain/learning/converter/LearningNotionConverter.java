package com.pocketco.domain.learning.converter;

import com.pocketco.domain.learning.dto.LearningNotionCode;
import com.pocketco.domain.learning.dto.LearningNotion;
import com.pocketco.domain.learning.entity.notion.Notion;
import com.pocketco.domain.learning.entity.notion.NotionCode;

public class LearningNotionConverter {
    public static LearningNotion toNotionLResponse(Notion notion, String imgStaticUrl, NotionCode notionCode, String language, boolean completed, boolean hasAnyCode) {
        LearningNotion.LearningNotionBuilder builder = LearningNotion.builder()
                .notionId(notion.getId())
                .pageNo(notion.getPageNo())
                .title(notion.getTitle())
                .point(notion.getPoint())
                .detail(notion.getDetail())
                .imgUrl(imgStaticUrl)
                .notionCompleted(completed);

        // 개념 페이지에 코드 자체가 없을 때
        if (!hasAnyCode) {
            return builder.exampleCode(null).build();
        }

        // 개념 페이지에 예제 코드가 존재하지만 해당 언어 코드는 아직 준비되지 않았을 때
        if (notionCode == null) {
            return builder.exampleCode(
                    LearningNotionCode.builder()
                            .language(language)
                            .content("아직 해당 언어의 예제 코드는 준비되지 않았습니다")
                            .build()
            ).build();
        }

        // 개념 페이지에 선택한 언어의 코드가 있을 떄
        return builder.exampleCode(
                LearningNotionCode.builder()
                        .language(notionCode.getLanguage().getName())
                        .content(notionCode.getContent())
                        .build()
        ).build();
    }
}