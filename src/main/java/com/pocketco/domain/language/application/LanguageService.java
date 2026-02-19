package com.pocketco.domain.language.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.language.entity.Language;

public interface LanguageService {
    Language findLanguageId(String name);
    AddLanguageResponse addLanguage(AddLanguageRequest req);
}