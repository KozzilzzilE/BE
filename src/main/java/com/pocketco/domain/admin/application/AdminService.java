package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request);
    AddTopicResponse addTopic(Long userId, AddTopicRequest request);
    AddNotionResponse addNotion(Long userId, MultipartFile image, AddNotionRequest request) throws IOException;
    List<AddAppliedResponse> addApplied(Long userId, List<AddAppliedRequest> request);
}