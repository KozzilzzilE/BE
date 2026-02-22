package com.pocketco.global.util.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Profile("local")
@Service
public class LocalFileStorageServiceImpl implements FileStorageService {
    @Value("${storage.local.base-dir}")
    private String baseDir;

    @Value("${app.public-base-url:http://localhost:8080}")
    private String publicBaseUrl;

    private Path resolveBaseDir() {
        Path base = Paths.get(baseDir);
        if (!base.isAbsolute()) {
            // 사용자 홈 하위에 고정 저장
            base = Paths.get(System.getProperty("user.home")).resolve(baseDir);
        }
        return base;
    }

    @Override
    public String save(MultipartFile file, String dir) throws IOException {
        Path base = resolveBaseDir();
        Path folder = base.resolve(dir);
        Files.createDirectories(folder);

        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(fn -> fn.contains("."))
                .map(fn -> fn.substring(fn.lastIndexOf(".")))
                .orElse("");
        String filename = UUID.randomUUID() + ext;

        Path target = folder.resolve(filename);
        try (var in = file.getInputStream()) {
            Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        // /static/** 로 노출되도록 매핑
        return publicBaseUrl + "/static/" + dir + "/" + filename;
    }
}