package com.pocketco.domain.user.application;

import com.pocketco.domain.admin.dto.AddPublicProfileImageResponse;
import com.pocketco.domain.user.entity.PublicProfileImage;
import com.pocketco.domain.user.repository.PublicProfileImageRepository;
import com.pocketco.global.util.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicProfileImageServiceImpl implements PublicProfileImageService {
    private final PublicProfileImageRepository publicProfileImageRepository;
    private final FileStorageService fileStorageService;

    @Override
    public AddPublicProfileImageResponse addPublicProfileImage(MultipartFile image) throws IOException {
        String img = null;
        if (image != null && !image.isEmpty()) {
            img = fileStorageService.save(image, "profiles");
        }

        PublicProfileImage publicProfileImage = PublicProfileImage.builder()
                .imgUrl(img)
                .build();

        PublicProfileImage saved = publicProfileImageRepository.save(publicProfileImage);

        return AddPublicProfileImageResponse.builder()
                .profileId(saved.getId())
                .imgUrl(fileStorageService.getUrl(saved.getImgUrl()))
                .build();
    }
}
