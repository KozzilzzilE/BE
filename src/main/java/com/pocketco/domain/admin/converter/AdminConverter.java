package com.pocketco.domain.admin.converter;

import com.pocketco.domain.admin.dto.AddNotionResponse;
import com.pocketco.domain.learning.entity.notion.Notion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {
    private static String publicBaseUrl;

    @Value("${app.public-base-url}")
    public void setPublicBaseUrl(String url) { publicBaseUrl = url; }

    public static AddNotionResponse toAddNotionResponse(Notion notion, int codeCount) {
        return AddNotionResponse.builder()
                .notionId(notion.getId())
                .pageNo(notion.getPageNo())
                .title(notion.getTitle())
                .imgUrl(publicBaseUrl + notion.getImgUrl())
                .codeCount(codeCount)
                .build();
    }
}