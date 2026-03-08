package com.pocketco.domain.language.api;

import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.dto.LanguageListResponse;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("/lists")
    public BaseResponse<LanguageListResponse> listLanguages() {
        LanguageListResponse results = languageService.getLanguagesList();
        return BaseResponse.onSuccess(SuccessStatus.LANGUAGES_LISTS_SUCCESS, results);
    }
}