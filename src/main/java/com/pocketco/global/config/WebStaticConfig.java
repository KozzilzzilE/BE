package com.pocketco.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebStaticConfig implements WebMvcConfigurer {
    @Value("${storage.local.base-dir}")
    private String localBaseDir;

    private String absoluteBase() {
        Path base = Paths.get(localBaseDir);
        if (!base.isAbsolute()) {
            base = Paths.get(System.getProperty("user.home")).resolve(localBaseDir);
        }
        return base.toAbsolutePath().normalize().toString();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + absoluteBase() + "/");
    }
}