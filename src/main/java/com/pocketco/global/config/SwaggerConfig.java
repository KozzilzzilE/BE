package com.pocketco.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        // API 기본 설정
        Info info = new Info()
                .title("pocketco API Document")
                .version("1.0")
                .description(
                        "환영합니다! [pocketco]는 이동하는 시간에도 알고리즘 및 코딩 테스트 준비를 도와주는 모바일 앱입니다.\n"
                );

        // JWT 인증 방식 설정
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER));


        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080")) // 추가적인 서버 URL 설정 가능
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}