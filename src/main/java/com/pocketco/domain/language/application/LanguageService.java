package com.pocketco.domain.language.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.language.dto.LanguageListResponse;
import com.pocketco.domain.language.entity.Language;

import java.util.List;
import java.util.Map;

public interface LanguageService {
    Language findLanguageId(String name);
    AddLanguageResponse addLanguage(AddLanguageRequest req);
    Map<Long, Language> validateAndGetLanguageMap(List<Long> languageIds);
    LanguageListResponse getLanguagesList();
    Long getLanguageId(String languageName);
}