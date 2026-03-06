package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddNotionRequest;
import com.pocketco.domain.admin.dto.AddNotionResponse;
import com.pocketco.domain.learning.dto.LearningNotionCompletionResponse;
import com.pocketco.domain.learning.dto.LearningNotionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface NotionService {
    AddNotionResponse addNotion(MultipartFile image, AddNotionRequest req) throws IOException;
    LearningNotionResponse getLearningNotions(Long topicId, String language, Long userId);
    LearningNotionCompletionResponse notionComplete(Long notionId, Long userId);
}