package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddNotionRequest;
import com.pocketco.domain.admin.dto.AddNotionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface NotionService {
    AddNotionResponse addNotion(MultipartFile image, AddNotionRequest req) throws IOException;
}