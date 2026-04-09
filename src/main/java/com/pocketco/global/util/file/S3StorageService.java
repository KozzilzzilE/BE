package com.pocketco.global.util.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class S3StorageService implements FileStorageService {

    private final S3Client s3;

    @Value("${storage.s3.bucket}")
    private String bucket;

    @Value("${storage.s3.base-url}")
    private String baseUrl;

    @Override
    public String save(MultipartFile file, String dir) throws IOException {
        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(fn -> fn.contains("."))
                .map(fn -> fn.substring(fn.lastIndexOf(".")))
                .orElse("");
        String key = dir + "/" + UUID.randomUUID() + ext;

        PutObjectRequest.Builder req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .cacheControl("public, max-age=31536000");

        s3.putObject(req.build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }

    @Override
    public String getUrl(String key) {
        return baseUrl + "/" + key; // 최종 접근 URL (프론트가 바로 사용)
    }
}