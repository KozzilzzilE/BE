package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;

public interface AdminService {
    AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request);
    AddTopicResponse addTopic(Long userId, AddTopicRequest request);
}