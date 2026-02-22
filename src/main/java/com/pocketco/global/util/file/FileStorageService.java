package com.pocketco.global.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    // 파일 저장 후 접근 가능한 절대 URL 반환
    String save(MultipartFile file, String path) throws IOException;
}