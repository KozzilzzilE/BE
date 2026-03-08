package com.pocketco.domain.admin.converter;

import com.pocketco.domain.admin.dto.AddNotionResponse;
import com.pocketco.domain.learning.entity.notion.Notion;

public class AdminConverter {

    public static AddNotionResponse toAddNotionResponse(Notion notion, int codeCount) {
        return AddNotionResponse.builder()
                .notionId(notion.getId())
                .pageNo(notion.getPageNo())
                .title(notion.getTitle())
                .imgUrl(notion.getImgUrl())
                .codeCount(codeCount)
                .build();
    }
}