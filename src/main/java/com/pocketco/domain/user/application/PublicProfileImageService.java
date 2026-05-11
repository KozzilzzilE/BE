package com.pocketco.domain.user.application;

import com.pocketco.domain.admin.dto.AddPublicProfileImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PublicProfileImageService {
    AddPublicProfileImageResponse addPublicProfileImage(MultipartFile image) throws IOException;
}