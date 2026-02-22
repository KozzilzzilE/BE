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
    @Value("${storage.local.base-dir")
    private String baseDir;

    private Path resolveBaseDir() {
        Path base = Paths.get(baseDir);
        if (!base.isAbsolute()) {
            // 사용자 홈 하위에 고정 저장
            base = Paths.get(System.getProperty("user.home")).resolve(baseDir);
        }
        return base;
    }

    @Override
    public String save(MultipartFile file, String path) throws IOException {
        Path base = resolveBaseDir();
        Path folder = base.resolve(path);
        Files.createDirectories(folder);

        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(fn -> fn.contains("."))
                .map(fn -> fn.substring(fn.lastIndexOf(".")))
                .orElse("");
        String fileName = UUID.randomUUID().toString() + ext;

        Path target = folder.resolve(fileName);
        try (var in = file.getInputStream()) {
            Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        return "/static/" + path + "/" + fileName;
    }
}